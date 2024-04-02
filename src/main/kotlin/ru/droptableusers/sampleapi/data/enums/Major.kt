package ru.droptableusers.sampleapi.data.enums

import kotlinx.serialization.Serializable

@Serializable
enum class Major(val localizedName: String) {
    BACKEND("Бэкэнд"),
    FRONTEND("Фронтэнд"),
    MOBILE("Мобайл"),
}
