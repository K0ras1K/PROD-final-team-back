package ru.droptableusers.sampleapi.telegram.models

import ru.droptableusers.sampleapi.telegram.enums.TextStatus

data class StatusData(
    val status: TextStatus,
    val headMessage: Long,
    val data: MutableList<String>,
) {
    companion object {
        fun valueOf(target: String): StatusData {
            val temp = target.split(":").toMutableList()
            val tempStatus = TextStatus.valueOf(temp[0])
            temp.removeAt(0)
            val tempHeadMessage: Long = temp[0].toLong()
            temp.removeAt(0)
            return StatusData(
                tempStatus,
                tempHeadMessage,
                temp,
            )
        }
    }

    fun asString(): String {
        var stringBuilder: String = ""
        stringBuilder += this.status.toString() + ":"
        stringBuilder += this.headMessage.toString() + ":"
        this.data.forEach { stringBuilder += "$it:" }
        stringBuilder.slice(0..stringBuilder.length - 2)
        return stringBuilder
    }
}
