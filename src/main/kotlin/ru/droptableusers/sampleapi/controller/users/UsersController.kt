package ru.droptableusers.sampleapi

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import org.mindrot.jbcrypt.BCrypt
import ru.droptableusers.sampleapi.data.enums.ValidationStatus
import ru.droptableusers.sampleapi.data.models.base.UserModel
import ru.droptableusers.sampleapi.data.models.inout.input.users.LoginInputModel
import ru.droptableusers.sampleapi.data.models.inout.input.users.RegisterInputModel
import ru.droptableusers.sampleapi.data.models.inout.output.ErrorResponse
import ru.droptableusers.sampleapi.data.models.inout.output.TokenRespondOutput
import ru.droptableusers.sampleapi.database.persistence.UserPersistence
import java.util.*
import java.util.regex.Pattern

class UsersController(val call: ApplicationCall) {
    suspend fun register() {
        runBlocking {
            val receive = call.receive<RegisterInputModel>()
            println("after receive")

            val validations = mutableMapOf<String, ValidationStatus>()

            println("before validation")
            validations["username"] = validateField(receive.username, 12, "[a-zA-Z0-9-]+")
            validations["password"] = validatePassword(receive.password)

            for (field in validations) {
                if (field.value != ValidationStatus.ACCEPTED) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("${field.value} Поле: ${field.key}"))
                    return@runBlocking
                }
            }
            println("after validation")
            println(validations)

            if (UserPersistence().selectByUsername(receive.username) != null) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse("Пользователь с таким логином уже зарегистрирован!"))
                return@runBlocking
            }

            println("before hash")
            val passwordHash = BCrypt.hashpw(receive.password, BCrypt.gensalt())
            val targetUserData =
                UserModel(
                    username = receive.username,
                    password = passwordHash,
                    regTime = System.currentTimeMillis(),
                    tgLogin = receive.tgLogin,
                    firstName = receive.firstName,
                    lastName = receive.lastName,
                    birthdayDate = receive.birthdayDate,
                )
            UserPersistence().insert(targetUserData)
            println("after insert")

            val token =
                JWT.create()
                    .withClaim("username", targetUserData.username)
                    .withClaim("passwordHash", targetUserData.password)
                    .withExpiresAt(Date(System.currentTimeMillis() + 60 * 60 * 60 * 24 * 60))
                    .sign(Algorithm.HMAC256(ApplicationConstants.SERVICE_SECRET_TOKEN))
            call.respond(HttpStatusCode.Created, TokenRespondOutput(token))
        }
    }

    suspend fun login() {
        runBlocking {
            val receive = call.receive<LoginInputModel>()

            if (UserPersistence().selectByUsername(receive.username) == null) {
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Пользователь с указанным логином и паролем не найден"))
                return@runBlocking
            }

            val selectedUser = UserPersistence().selectByUsername(receive.username)!!

            if (!BCrypt.checkpw(receive.password, selectedUser.password)) {
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Пользователь с указанным логином и паролем не найден"))
                return@runBlocking
            }

            val token =
                JWT.create()
                    .withClaim("username", selectedUser.username)
                    .withClaim("passwordHash", selectedUser.password)
                    .withExpiresAt(Date(System.currentTimeMillis() + 60 * 60 * 60 * 24 * 60))
                    .sign(Algorithm.HMAC256(ApplicationConstants.SERVICE_SECRET_TOKEN))
            call.respond(TokenRespondOutput(token))
        }
    }

    private fun validateField(
        field: String,
        maxLength: Int?,
        pattern: String?,
    ): ValidationStatus {
        if (field == "") {
            return ValidationStatus.ACCEPTED
        }
        if (maxLength != null && field.length > maxLength) {
            return ValidationStatus.INVALID_LENGTH
        }
        if (pattern != null && !Pattern.matches(pattern, field)) {
            return ValidationStatus.INVALID_FORMAT
        }
        return ValidationStatus.ACCEPTED
    }

    private fun validatePassword(password: String): ValidationStatus {
        val lowercasePattern = Regex("[a-z]")
        val uppercasePattern = Regex("[A-Z]")
        val digitPattern = Regex("\\d")

        return when {
            password.length < 6 -> ValidationStatus.INVALID_LENGTH
            password.length > 100 -> ValidationStatus.INVALID_LENGTH
            !lowercasePattern.containsMatchIn(
                password,
            ) || !uppercasePattern.containsMatchIn(password) || !digitPattern.containsMatchIn(password) -> ValidationStatus.INVALID_FORMAT
            else -> ValidationStatus.ACCEPTED
        }
    }
}
