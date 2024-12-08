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
    val mapYs = lines.indices
    val mapXs = lines[0].indices
    val typeToPosition = createLookup(lines)
    val antiNodeLocations1 = part1(
        typeToPosition = typeToPosition
    ) {
        x in mapXs && y in mapYs
    }
    val antiNodeLocations2 = part2(
        typeToPosition = typeToPosition
    ) {
        x in mapXs && y in mapYs
    }
    println(antiNodeLocations1.size)
    println(antiNodeLocations2.size)
}

fun List<String>.getChr(x: Int, y: Int) = this[y][x]

private fun createLookup(lines: List<String>): Map<Char, MutableSet<Vec2>> {
    val typeToPosition = mutableMapOf<Char, MutableSet<Vec2>>()
    for (y in lines.indices) {
        for (x in lines[0].indices) {
            val chr = lines.getChr(x, y)
            if (chr == '.') {
                continue
            }
            val ttpEntry = typeToPosition.getOrElse(chr) { mutableSetOf() }
            ttpEntry.add(Vec2(x, y))
            typeToPosition.put(chr, ttpEntry)
        }
    }
    return typeToPosition
}


fun part1(
    typeToPosition: Map<Char, MutableSet<Vec2>>,
    isInBounds: Vec2.() -> Boolean,
): MutableSet<Vec2> {
    val antiNodeLocations = mutableSetOf<Vec2>()
    for (type in typeToPosition.keys) {
        val kin = typeToPosition[type]!!
        for (first in kin) {
            for (second in kin) {
                if (first == second) continue
                val distanceVec = second - first
                val relDistVec = distanceVec * 2
                val absAntiNodeVec = first + relDistVec
                if (absAntiNodeVec.isInBounds()) {
                    // MAKE SURE YOU ONLY COUNT WHATS IN THE MAP
                    antiNodeLocations += absAntiNodeVec
                }
            }
        }
    }
    return antiNodeLocations
}

private fun part2(
    typeToPosition: Map<Char, MutableSet<Vec2>>,
    isInBounds: Vec2.() -> Boolean,
): MutableSet<Vec2> {
    val antiNodeLocations = mutableSetOf<Vec2>()
    for (type in typeToPosition.keys) {
        val kin = typeToPosition[type]!!
        for (first in kin) {
            for (second in kin) {
                if (first == second) continue
                val distanceVec = second - first
                val relDistVec = distanceVec
                var positiveNextLocation: Vec2 = first
                do {
                    positiveNextLocation = positiveNextLocation + relDistVec
                    if (positiveNextLocation.isInBounds()) {
                        antiNodeLocations += positiveNextLocation
                    }
                } while (positiveNextLocation.isInBounds())
                var negativeNextLocation: Vec2 = first
                do {
                    negativeNextLocation = negativeNextLocation - relDistVec
                    if (negativeNextLocation.isInBounds()) {
                        antiNodeLocations += negativeNextLocation
                    }
                } while (positiveNextLocation.isInBounds())
            }
        }
    }
    return antiNodeLocations
}