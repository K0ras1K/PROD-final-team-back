package ru.droptableusers.sampleapi.controller.tags

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.data.models.inout.input.tags.AddUserTagsInputModel
import ru.droptableusers.sampleapi.data.models.inout.input.tags.CreateTagsInputModel
import ru.droptableusers.sampleapi.data.models.inout.input.tags.DeleteTagsInputModel
import ru.droptableusers.sampleapi.data.models.inout.input.tags.RemoveUserInputModel
import ru.droptableusers.sampleapi.data.models.inout.output.ErrorResponse
import ru.droptableusers.sampleapi.data.models.inout.output.tags.TagObjectOutput
import ru.droptableusers.sampleapi.data.models.inout.output.tags.TagsOutput
import ru.droptableusers.sampleapi.database.persistence.TagsPersistence
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence

class TagsController(val call: ApplicationCall) {
    private fun tagListByUserId(userId: Int): List<TagObjectOutput> {
        val tags = TagsPersistence().getTagsByIdList(UserPersistence().selectTagIds(userId))
        return tags.map {
            TagObjectOutput(it.id, it.tagString)
        }
    }

    suspend fun getUserTags() {
        runBlocking {
            val userId = call.parameters["userId"]?.toInt()
            if (userId != null) {
                call.respond(HttpStatusCode.OK, TagsOutput(tagListByUserId(userId)))
                return@runBlocking
            }
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Не вписал userId"))
        }
    }

    suspend fun addUserTags() {
        runBlocking {
            val tagsInput = call.receive<AddUserTagsInputModel>()
            tagsInput.tagIdList.forEach {
                UserPersistence().addTag(tagsInput.userId, it)
            }
            call.respond(HttpStatusCode.OK)
        }
    }

    suspend fun removeUserTag() {
        runBlocking {
            val removeInput = call.receive<RemoveUserInputModel>()
            UserPersistence().removeTag(removeInput.userId, removeInput.tagId)
            call.respond(HttpStatusCode.OK)
        }
    }

    suspend fun getTeamsTags() {
        runBlocking {
            val teamId = call.parameters["teamId"]?.toInt()
            if (teamId != null) {
                val tags = mutableSetOf<TagObjectOutput>()
                TeamsPersistence().selectTeammates(teamId).forEach {
                    tags.addAll(tagListByUserId(it))
                }
                call.respond(HttpStatusCode.OK, TagsOutput(tags.toList()))
                return@runBlocking
            }
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Не вписал userId"))
        }
    }

    suspend fun getAllTags() {
        runBlocking {
            call.respond(
                HttpStatusCode.OK,
                TagsOutput(
                    TagsPersistence().getAllTags().map {
                        TagObjectOutput(it.id, it.tagString)
                    },
                ),
            )
        }
    }

    suspend fun createTags() {
        runBlocking {
            val createInput = call.receive<CreateTagsInputModel>()

            createInput.tagList.forEach {
                TagsPersistence().insert(it)
            }

            call.respond(HttpStatusCode.Created)
        }
    }

    suspend fun deleteTags() {
        runBlocking {
            val createInput = call.receive<DeleteTagsInputModel>()

            createInput.tagIdList.forEach {
                TagsPersistence().delete(it)
            }

            call.respond(HttpStatusCode.OK)
        }
    }
}
