package ru.droptableusers.sampleapi.utils

import ru.droptableusers.sampleapi.data.enums.Group
import ru.droptableusers.sampleapi.database.persistence.GroupPersistence
import ru.droptableusers.sampleapi.database.persistence.UserPersistence

object GroupUtils {
    fun hasGroup(
        targetGroup: Group,
        currentGroup: Group,
    ): Boolean {
        return Group.entries.indexOf(targetGroup) >= Group.entries.indexOf(currentGroup)
    }

    fun hasGroup(
        username: String,
        targetGroup: Group,
    ): Boolean {
        return Group.entries.indexOf(targetGroup) >=
            Group.entries
                .indexOf(GroupPersistence().select(UserPersistence().selectByUsername(username)!!.id)!!.group)
    }

    fun hasGroup(
        userId: Int,
        targetGroup: Group,
    ): Boolean {
        return Group.entries.indexOf(targetGroup) >=
            Group.entries
                .indexOf(GroupPersistence().select(userId)!!.group)
    }
}
