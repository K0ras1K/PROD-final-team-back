@file:Suppress("LABEL_NAME_CLASH")

package ru.droptableusers.sampleapi.analytics.ml

import ru.droptableusers.sampleapi.data.models.base.TeamModel

object KNN {
    fun sort(
        teams: Map<TeamModel, Set<String>>,
        tags: Set<String>,
    ): Set<TeamModel> {
        val result = mutableMapOf<TeamModel, Double>()
        teams.forEach { (k, v) ->
            result[k] = sort(v, tags)
        }

        result.toList().sortedBy { (_, value) -> value }
        return result.keys
    }

    /**
     * @return average distance
     */
    private fun sort(
        teamTags: Set<String>,
        tags: Set<String>,
    ): Double {
        val neighbors = mutableMapOf<String, Double>()
        tags.forEach { tag ->
            val vector = getVector(tag) ?: return@forEach

            teamTags.forEach { teamTag ->
                val teamTagVector = getVector(teamTag) ?: return@forEach
                neighbors[teamTag] = vector.distQ(teamTagVector)
            }
        }

        if (neighbors.isEmpty()) {
            return Double.MAX_VALUE
        }

        neighbors.toList().sortedBy { (_, value) -> value }.toMap()

        var sum = 0.0
        var n = 0
        neighbors.forEach {
            sum += it.value
            if (++n == 3) {
                return sum / 3.0
            }
        }
        return sum / neighbors.size
    }

    // TODO dataset
    private fun getVector(tag: String): Vector? {
        return when (tag) {
            "python" -> Vector(tag, 1.0, 0.0, 0.0)
            "docker" -> Vector(tag, 0.5, 0.0, 0.0)
            else -> null
        }
    }
}
