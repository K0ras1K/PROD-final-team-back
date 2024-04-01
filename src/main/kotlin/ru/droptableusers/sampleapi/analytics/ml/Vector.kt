package ru.droptableusers.sampleapi.analytics.ml

import kotlin.math.pow

data class Vector(
    val name: String,
    val weight: Double,
    val x: Double,
    val y: Double,
) {
    fun distQ(vector: Vector): Double {
        return ((vector.x - this.x).pow(2) + (vector.y - this.y).pow(2)) / weight
    }
}
