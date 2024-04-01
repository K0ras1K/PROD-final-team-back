package ru.droptableusers.sampleapi.database.persistence

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.droptableusers.sampleapi.data.models.base.TagModel
import ru.droptableusers.sampleapi.database.schema.TagTable

class TagsPersistence {
    private fun resultRowToTag(resultRow: ResultRow) =
        TagModel(
            id = resultRow[TagTable.id].value,
            tagString = resultRow[TagTable.text],
        )

    fun insert(tagString: String) {
        try {
            transaction {
                TagTable.insert {
                    it[TagTable.text] = tagString
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun selectById(id: Int): TagModel? {
        return try {
            transaction {
                TagTable.selectAll()
                    .where { TagTable.id.eq(id) }
                    .single()
                    .let(::resultRowToTag)
            }
        } catch (e: Exception) {
            null
        }
    }

    fun selectByText(text: String): TagModel? {
        return try {
            transaction {
                TagTable.selectAll()
                    .where { TagTable.text.eq(text) }
                    .single()
                    .let(::resultRowToTag)
            }
        } catch (e: Exception) {
            null
        }
    }

    fun delete(id: Int): Boolean {
        return try {
            transaction {
                TagTable.deleteWhere { TagTable.id.eq(id) } > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    fun getTagsByIdList(idList: List<Int>): List<TagModel> {
        return try {
            transaction {
                TagTable.selectAll()
                    .where { TagTable.id.inList(idList) }
                    .map(::resultRowToTag)
            }
        } catch (e: Exception) {
            listOf()
        }
    }

    fun getAllTags(): List<TagModel> {
        return try {
            transaction {
                TagTable.selectAll()
                    .map(::resultRowToTag)
            }
        } catch (e: Exception) {
            listOf()
        }
    }
}
