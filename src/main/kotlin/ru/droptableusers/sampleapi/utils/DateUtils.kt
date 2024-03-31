package ru.droptableusers.sampleapi.utils

object DateUtils {
    fun getCurrentDateAsString(millis: Long): String {
        val sdf = java.text.SimpleDateFormat("dd.MM.yyyy")
        val date1 = java.util.Date(millis)
        return sdf.format(date1)
    }
}
