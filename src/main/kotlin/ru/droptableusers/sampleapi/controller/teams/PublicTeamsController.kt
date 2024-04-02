package ru.droptableusers.sampleapi.controller.teams

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.analytics.ml.KNN
import ru.droptableusers.sampleapi.controller.AbstractController
import ru.droptableusers.sampleapi.data.models.base.TeamModel
import ru.droptableusers.sampleapi.data.models.base.UserModel
import ru.droptableusers.sampleapi.data.models.inout.input.teams.TagsTeamRequest
import ru.droptableusers.sampleapi.data.models.inout.output.ErrorResponse
import ru.droptableusers.sampleapi.data.models.inout.output.teams.SmallTeamRespondModel
import ru.droptableusers.sampleapi.data.models.inout.output.users.ProfileOutputResponse
import ru.droptableusers.sampleapi.database.persistence.*

/**
 * Public teams controller
 *
 * @property call
 * @constructor Create empty Public teams controller
 */
class PublicTeamsController(val call: ApplicationCall) {

    /**
     * Load all
     */
    suspend fun loadAll() {
        runBlocking {
            val limit = call.request.queryParameters["limit"]!!.toInt()
            val offset = call.request.queryParameters["offset"]!!.toLong()
            val allTeams =
                TeamsPersistence().selectAll(limit, offset).map {
                    SmallTeamRespondModel(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        iconUrl = it.iconUrl,
                        bannerUrl = it.bannerUrl,
                        membersCount = TeamsPersistence().selectTeammates(it.id).size,
                    )
                }
            call.respond(HttpStatusCode.OK, allTeams)
        }
    }

    /**
     * Load all by m l
     *
     */
    suspend fun loadAllByML() {
        runBlocking {
            val receive = call.receive<TagsTeamRequest>()
            val teams = TeamsPersistence().selectAll()
            val teamsAndTags = mutableMapOf<TeamModel, Set<String>>()
            teams.forEach {
                val tags = mutableSetOf<String>()
                val searchingForModels = SearchingForPersistence().selectByTeamId(it.id)
                searchingForModels.forEach { searchingForModel ->
                    val tagsIds = SearchingForPersistence().selectTagIds(searchingForModel.id)
                    val tagsList = TagsPersistence().getTagsByIdList(tagsIds).map { tagModel -> tagModel.tagString }
                    tags.addAll(tagsList)
                }
                teamsAndTags[it] = tags
            }
            val tags = TagsPersistence().getTagsByIdList(receive.tags).map { it.tagString }
            val allTeams =
                KNN.sort(teamsAndTags, tags.toSet())
                    .map {
                        SmallTeamRespondModel(
                            id = it.id,
                            name = it.name,
                            description = it.description,
                            iconUrl = it.iconUrl,
                            bannerUrl = it.bannerUrl,
                            membersCount = TeamsPersistence().selectTeammates(it.id).size,
                        )
                    }
            call.respond(HttpStatusCode.OK, allTeams)
        }
    }

    suspend fun getTeamTemplate(){
        runBlocking {
            val template = TeamsPersistence().selectTeamTemplate()
            if (template != null) {
                call.respond(HttpStatusCode.OK, template)
                return@runBlocking
            }else{
                call.respond(HttpStatusCode.NotFound, ErrorResponse("Темплейт отсутствует"))
                return@runBlocking
            }

        }
    }


}

fun <T> List<T>.safeSubList(
    fromIndex: Int,
    toIndex: Int,
): List<T> {
    if (fromIndex >= this.size) {
        return listOf<T>()
    }
    return this.subList(fromIndex, toIndex.coerceAtMost(this.size))
}
