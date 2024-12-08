package day08

import java.io.File

val input = File("input/day08b.txt")

data class Vec2(val x: Int, val y: Int) {
    operator fun minus(other: Vec2) = Vec2(this.x - other.x, this.y - other.y)
    operator fun plus(other: Vec2) = Vec2(this.x + other.x, this.y + other.y)
    operator fun times(scalar: Int) = Vec2(this.x * scalar, this.y * scalar)
}

fun main() {
    val lines = input.readLines()
    fun getChr(x: Int, y: Int) = lines[y][x]
    val mapYs = lines.indices
    val mapXs = lines[0].indices
    val positionToType = mutableMapOf<Vec2, Char>()
    val typeToPosition = mutableMapOf<Char, MutableSet<Vec2>>()
    // do an inverse index?
    for (y in lines.indices) {
        for (x in lines[0].indices) {
            val chr = getChr(x, y)
            if (chr == '.') {
                continue
            }
            positionToType += Vec2(x, y) to chr
            val ttpEntry = typeToPosition.getOrElse(chr) { mutableSetOf() }
            ttpEntry.add(Vec2(x, y))
            typeToPosition.put(chr, ttpEntry)
        }
    }
    val antiNodeLocations = mutableSetOf<Vec2>()
    for (type in typeToPosition.keys) {
        println(type)
        val kin = typeToPosition[type]!!
        println(kin)
        for (first in kin) {
            for (second in kin) {
                if (first == second) continue
                // compute antinode locations
                // distance between
                val distanceVec = second - first
                val relDistVec = distanceVec * 2
                val absAntiNodeVec = first + relDistVec
                if (absAntiNodeVec.x in mapXs && absAntiNodeVec.y in mapYs) {
                    // MAKE SURE YOU ONLY COUNT wHATS IN THE MAP
                    antiNodeLocations += absAntiNodeVec
                }
            }
        }
    }
    println(antiNodeLocations.size)
}

// MAKE SURE YOU ONLY COUNT wHATS IN THE MAP