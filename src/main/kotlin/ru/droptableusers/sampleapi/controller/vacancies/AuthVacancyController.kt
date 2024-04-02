package ru.droptableusers.sampleapi.controller.vacancies

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.models.inout.input.vacancies.VacancyGenerateInputModel
import ru.droptableusers.sampleapi.data.models.inout.output.ErrorResponse
import ru.droptableusers.sampleapi.data.models.inout.output.vacancies.VacancyRespond
import ru.droptableusers.sampleapi.database.persistence.SearchingForPersistence
import ru.droptableusers.sampleapi.database.persistence.TagsPersistence
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence

class AuthVacancyController(call: ApplicationCall) : AbstractController(call) {
    suspend fun generateVacancies(){
        runBlocking {
            val vacancyInput = call.receive<VacancyGenerateInputModel>()
            val template = TeamsPersistence().selectTeamTemplate()

            if (template == null){
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Тэмплейт команды еще не создан"))
                return@runBlocking
            }

            for (i in 0..<template.slots.size){
                template.slots[i].possibleItems.forEach {
                    val vacId = SearchingForPersistence().insert(id, i)
                    vacancyInput.vacancyTemplates
                        .find { sm -> sm.major == it}!!.tagIds.forEach {
                            tagId -> SearchingForPersistence().addTag(vacId, tagId)
                        }

                }
            }

            val user = UserPersistence().selectById(id)
            val teamId = TeamsPersistence().selectByUserId(id)
            if (user?.major != null && teamId != null){
                val sfm = SearchingForPersistence().selectFirstByMajor(user.major)
                if (sfm != null) SearchingForPersistence().deleteBySlotIndex(teamId, sfm.slotIndex)
            }

            call.respond(HttpStatusCode.OK)

        }
    }

    suspend fun get(){
        runBlocking {
            val teamId = call.parameters["teamId"]!!.toInt()
            call.respond(HttpStatusCode.OK, SearchingForPersistence().selectByTeamId(teamId).map {
                VacancyRespond(teamId, TagsPersistence().getTagsByIdList(SearchingForPersistence().selectTagIds(teamId)).map { it.tagString })
            })
        }
    }
}