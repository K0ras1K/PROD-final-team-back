package ru.droptableusers.sampleapi.utils

import ru.droptableusers.sampleapi.data.enums.ValidationStatus
import java.util.regex.Pattern

object Validation {
    fun validateField(
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

    fun validatePassword(password: String): ValidationStatus {
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
