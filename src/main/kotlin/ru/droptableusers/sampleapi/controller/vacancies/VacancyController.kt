package ru.droptableusers.sampleapi.controller.vacancies

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.models.inout.output.ErrorResponse
import ru.droptableusers.sampleapi.data.models.inout.output.vacancies.VacancyRespond
import ru.droptableusers.sampleapi.database.persistence.SearchingForPersistence
import ru.droptableusers.sampleapi.database.persistence.TagsPersistence
import ru.droptableusers.sampleapi.database.schema.SearchingForTable

class VacancyController(val call: ApplicationCall){
    suspend fun get(){
        runBlocking {
            val vacancyId = call.parameters["id"]?.toInt()
            if (vacancyId != null){
                val vac = SearchingForPersistence().select(vacancyId)
                val tagIdList = SearchingForPersistence().selectTagIds(vacancyId)
                if (vac != null){
                    val tagList = TagsPersistence().getTagsByIdList(tagIdList).map{
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
}