package ru.droptableusers.sampleapi.controller.teams

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import ru.droptableusers.sampleapi.analytics.ml.KNN
import ru.droptableusers.sampleapi.data.models.base.TeamModel
import ru.droptableusers.sampleapi.data.models.inout.input.teams.TagsTeamRequest
import ru.droptableusers.sampleapi.data.models.inout.output.teams.SmallTeamRespondModel
import ru.droptableusers.sampleapi.database.persistence.SearchingForPersistence
import ru.droptableusers.sampleapi.database.persistence.TagsPersistence
import ru.droptableusers.sampleapi.database.persistence.TeamsPersistence

/**
 * Public teams controller
 *
 * @property call
 * @constructor Create empty Public teams controller
 */
class PublicTeamsController(val call: ApplicationCall) {
    /**
     * Load all
     *
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
    // TODO add to Route
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

            val allTeams =
                KNN.sort(teamsAndTags, receive.tags.toSet())
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
}
