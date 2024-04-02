package ru.droptableusers.sampleapi.controller.vacancies

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.data.models.inout.input.vacancies.VacancyInputModel
import ru.droptableusers.sampleapi.data.models.inout.output.ErrorResponse
import ru.droptableusers.sampleapi.data.models.inout.output.vacancies.VacancyRespond
import ru.droptableusers.sampleapi.database.persistence.SearchingForPersistence
import ru.droptableusers.sampleapi.database.persistence.TagsPersistence

class VacancyController(val call: ApplicationCall) {
    suspend fun get() {
        runBlocking {
            val vacancyId = call.parameters["id"]?.toInt()
            if (vacancyId != null) {
                val vac = SearchingForPersistence().select(vacancyId)
                val tagIdList = SearchingForPersistence().selectTagIds(vacancyId)
                if (vac != null) {
                    val tagList =
                        TagsPersistence().getTagsByIdList(tagIdList).map {
                            it.tagString
                        }
                    call.respond(HttpStatusCode.OK, VacancyRespond(vac.teamId, tagList))
                }
                call.respond(HttpStatusCode.NotFound, ErrorResponse("Vacancy with $vacancyId id not found"))
                return@runBlocking
            }
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("id == null"))
        }
    }

//    suspend fun add() {
//        runBlocking {
//            val vacancyInput = call.receive<VacancyInputModel>()
//
//            SearchingForPersistence().insert(vacancyInput.teamId)
//            vacancyInput.tagList.forEach {
//                SearchingForPersistence().addTagSearchingFor(it, vacancyInput.teamId)
//            }
//
//            call.respond(HttpStatusCode.OK)
//        }
//    }

    suspend fun delete() {
        runBlocking {
            val vacancyId = call.parameters["id"]?.toInt()

            if (vacancyId == null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("id == null"))
                return@runBlocking
            }

            if (!SearchingForPersistence().deleteById(vacancyId)) {
                call.respond(
                    HttpStatusCode.Accepted,
                    ErrorResponse("Не существовало или ошибка, но пусть будет accepted"),
                )
                return@runBlocking
            }

            call.respond(HttpStatusCode.OK)
        }
    }
}
