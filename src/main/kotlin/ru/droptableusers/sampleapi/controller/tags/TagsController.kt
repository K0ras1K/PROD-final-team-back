package ru.droptableusers.sampleapi.controller.tags

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.models.inout.output.ErrorResponse
import ru.droptableusers.sampleapi.data.models.inout.output.tags.TagsOutput
import ru.droptableusers.sampleapi.database.persistence.TagsPersistence
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence

class TagsController(call: ApplicationCall): AbstractController(call) {

    private fun tagListByUserId(userId: Int): List<String>{
        val tags = TagsPersistence().getTagsByIdList(UserPersistence().selectTagIds(userId))
        return tags.map{it.tagString}
    }

    suspend fun getUserTags(){
        runBlocking {
           val userId = call.parameters["userId"]?.toInt()
            if(userId != null){
                call.respond(HttpStatusCode.OK, TagsOutput(tagListByUserId(userId)))
                return@runBlocking
            }
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Не вписал userId"))
        }
    }

    suspend fun getTeamsTags(){
        runBlocking {
            val teamId = call.parameters["teamId"]?.toInt()
            if(teamId != null){
                val tags = mutableSetOf<String>()
                TeamsPersistence().selectTeammates(teamId).forEach{
                    tags.addAll(tagListByUserId(it))
                }
                call.respond(HttpStatusCode.OK, TagsOutput(tags.toList()))
                return@runBlocking
            }
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Не вписал userId"))
        }
    }

}