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

        result.forEach {
            println(it.value)
        }


        return result.toList().sortedBy { (_, value) -> value }.toMap().keys
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

        val sorted = neighbors.toList().sortedBy { (_, value) -> value }.toMap()

        var sum = 0.0
        var n = 0
        sorted.forEach {
            sum += it.value
            if (++n == 3) {
                return sum / 3.0
            }
        }
        return sum / sorted.size
    }

    // TODO dataset
    private fun getVector(tag: String): Vector? {
        val t = tag.lowercase()
        return when (t.lowercase()) {
            "mobile" -> Vector(t, 0.8, 0.2, 0.9)
            "android" -> Vector(t, 0.7, 0.3, 0.8)
            "ios" -> Vector(t, 0.7, 0.2, 0.85)
            "swift" -> Vector(t, 0.6, 0.25, 0.82)
            "flutter" -> Vector(t, 0.6, 0.35, 0.77)
            "java" -> Vector(t, 0.5, 0.3, 0.9)
            "kotlin" -> Vector(t, 0.5, 0.25, 0.88)
            "frontend" -> Vector(t, 0.8, 0.2, 0.7)
            "javascript" -> Vector(t, 0.7, 0.3, 0.65)
            "react" -> Vector(t, 0.6, 0.35, 0.63)
            "angular" -> Vector(t, 0.5, 0.3, 0.7)
            "vue" -> Vector(t, 0.5, 0.25, 0.72)
            "backend" -> Vector(t, 0.8, 0.2, 0.5)
            "python" -> Vector(t, 0.7, 0.25, 0.48)
            "nodejs" -> Vector(t, 0.6, 0.3, 0.4)
            "php" -> Vector(t, 0.5, 0.25, 0.5)
            "c#" -> Vector(t, 0.5, 0.35, 0.45)
            "docker" -> Vector(t, 0.6, 0.8, 0.2)
            "github ci" -> Vector(t, 0.6, 0.82, 0.2)
            "heroku" -> Vector(t, 0.5, 0.88, 0.3)
            "cloud" -> Vector(t, 0.5, 0.9, 0.28)
            "kubernetes" -> Vector(t, 0.4, 0.85, 0.28)
            else -> null
        }
    }

}
