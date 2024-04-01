package ru.droptableusers.sampleapi.analytics.ml;

import de.nycode.bcrypt.verify
import io.ktor.server.http.content.*
import ru.droptableusers.sampleapi.data.models.base.GroupModel
import ru.droptableusers.sampleapi.data.models.base.TeamModel

@Suppress("LABEL_NAME_CLASH")
class KNN {

    fun sort(teams: Map<TeamModel, List<String>>, tags: List<String>) {
        val distances = mutableListOf<Double>()
        val result = mutableMapOf<TeamModel, Double>()

    }

    /**
     * @return average distance
     */
    fun sort(teamTags: List<String>, tags: List<String>): Double {
        val neighbors = mutableMapOf<String, Double>()
        tags.forEach { tag ->
            val vector = getVector(tag) ?: return@forEach

            teamTags.forEach {  teamTag ->
                val teamTagVector = getVector(teamTag) ?: return@forEach
                neighbors[teamTag] = vector.distQ(teamTagVector)
            }
        }

        if (neighbors.isEmpty())
            return Double.MAX_VALUE

        neighbors.toList().sortedBy { (_, value) -> value }.toMap()

        var sum = 0.0
        var n = 0
        neighbors.forEach {
            sum += it.value
            if (++n == 3)
                return sum / 3.0
        }
        return sum / neighbors.size
    }

    private fun getVector(tag: String) : Vector? {
        return when (tag) {
            "python" -> Vector(tag, 1.0, 0.0, 0.0)
            "docker" -> Vector(tag, 0.5, 0.0, 0.0)
            else -> null
        }
    }

}
