package ru.droptableusers.sampleapi.database.persistence

import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.droptableusers.sampleapi.data.enums.Major
import ru.droptableusers.sampleapi.data.models.base.SearchingForModel
import ru.droptableusers.sampleapi.database.schema.SearchingForTable
import ru.droptableusers.sampleapi.database.schema.SearchingForTagsTable

class SearchingForPersistence {
    private fun resultRowToSearchingFor(resultRow: ResultRow) =
        SearchingForModel(
            id = resultRow[SearchingForTable.id].value,
            teamId = resultRow[SearchingForTable.teamId].value,
            slotIndex = resultRow[SearchingForTable.slotIndex],
            major = resultRow[SearchingForTable.major]
        )

    fun selectByTeamId(teamId: Int): List<SearchingForModel> {
        return try {
            transaction {
                SearchingForTable.selectAll()
                    .where { SearchingForTable.teamId.eq(teamId) }
                    .map(::resultRowToSearchingFor)
            }
        } catch (e: Exception) {
            listOf()
        }
    }

    fun select(id: Int): SearchingForModel? {
        return try {
            transaction {
                SearchingForTable.selectAll()
                    .where { SearchingForTable.id.eq(id) }
                    .single()
                    .let(::resultRowToSearchingFor)
            }
        } catch (e: Exception) {
            null
        }
    }

    fun insert(teamId: Int, slotIndex: Int): Int {
        return try {
            transaction {
                SearchingForTable.insert {
                    it[SearchingForTable.teamId] = teamId
                    it[SearchingForTable.slotIndex] = slotIndex
                }.resultedValues!!.first()[SearchingForTable.id].value
            }
        } catch (e: Exception) {
            -1
        }
    }

    fun deleteById(id: Int): Boolean {
        return try {
            transaction {
                SearchingForTable.deleteWhere { SearchingForTable.id.eq(id) } > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun deleteBySlotIndex(teamId: Int, slotIndex: Int): Boolean {
        return try {
            transaction {
                SearchingForTable.deleteWhere { SearchingForTable.teamId.eq(teamId) and
                        SearchingForTable.slotIndex.eq(slotIndex)} > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun deleteByTeamId(teamId: Int): Boolean {
        return try {
            transaction {
                SearchingForTable.deleteWhere { SearchingForTable.teamId.eq(teamId) } > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun selectFirstByMajor(major: Major): SearchingForModel? {
        return try {
            transaction {
                SearchingForTable.selectAll()
                    .where { SearchingForTable.major.eq(major) }
                    .single()
                    .let(::resultRowToSearchingFor)
            }
        } catch (e: Exception){
            null
        }
    }

    fun addTagSearchingFor(
        tagId: Int,
        searchingForId: Int,
    ) {
        try {
            transaction {
                SearchingForTagsTable.insert {
                    it[SearchingForTagsTable.searchingForId] = searchingForId
                    it[SearchingForTagsTable.tagId] = tagId
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun selectTagIds(searchingForId: Int): List<Int> {
        return try {
            transaction {
                SearchingForTagsTable.selectAll()
                    .where { SearchingForTagsTable.searchingForId.eq(searchingForId) }
                    .map { it[SearchingForTagsTable.searchingForId].value }
            }
        } catch (e: Exception) {
            listOf()
        }
    }

    fun addTag(
        searchingForId: Int,
        tagId: Int,
    ) {
        try {
            transaction {
                SearchingForTagsTable.insert {
                    it[SearchingForTagsTable.tagId] = tagId
                    it[SearchingForTagsTable.searchingForId] = searchingForId
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun removeTag(
        searchingForId: Int,
        tagId: Int,
    ): Boolean {
        return try {
            transaction {
                SearchingForTagsTable.deleteWhere {
                    SearchingForTagsTable.searchingForId.eq(searchingForId) and
                        SearchingForTagsTable.tagId.eq(tagId)
                } > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun removeAllTags(searchingForId: Int): Boolean {
        return try {
            transaction {
                SearchingForTagsTable.deleteWhere {
                    SearchingForTagsTable.searchingForId.eq(searchingForId)
                } > 0
            }
        } catch (e: Exception) {
            false
        }
    }
}
