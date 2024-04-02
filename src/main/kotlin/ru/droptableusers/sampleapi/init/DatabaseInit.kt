package ru.droptableusers.sampleapi.init

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import ru.droptableusers.sampleapi.database.schema.*

/**
 * Database init
 *
 * @constructor Create empty Database init
 * @author Roman K0ras1K Kalmykov
 */
class DatabaseInit {
    val tables: List<Table> =
        listOf(
            UserTable,
            TagTable,
            TeamsUsersTable,
            TeamTable,
            GroupTable,
            InviteTable,
            SearchingForTable,
            TagsUsersTable,
            ToursResultTable,
            TelegramsTable,
            ValidateDataTable,
            SearchingForTagsTable,
            DocumentsTable,
            DocumentConditionsTable,
            FilledDocumentsTable,
            TeamTemplateTable
        )

    /**
     * Initialize tables
     *
     * @author Roman K0ras1K Kalmykov
     */
    fun initialize() {
        transaction {
            tables.forEach {
                SchemaUtils.create(it)
            }
        }
    }
}
