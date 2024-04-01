package ru.droptableusers.sampleapi.database.schema

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption

object TagsUsersTable : IdTable<String>("tags_users") {
    override val id: Column<EntityID<String>> = text("recordId").entityId()
    val userId = reference("userId", UserTable.id, onDelete = ReferenceOption.CASCADE)
    val tagId = reference("tagId", TagTable.id, onDelete = ReferenceOption.CASCADE)
}
