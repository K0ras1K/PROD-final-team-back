package ru.droptableusers.sampleapi.data.models.inout.input.tags

data class AddUserTagsInputModel(
    val userId: Int,
    val tagIdList: List<Int>
)
