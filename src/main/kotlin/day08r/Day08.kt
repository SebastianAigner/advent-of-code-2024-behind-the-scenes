@file:Suppress("DuplicatedCode")

package day08r

import java.io.File
import kotlin.time.measureTimedValue

val input = File("input/day08b.txt")

data class Vec2(val x: Int, val y: Int) {
    operator fun minus(other: Vec2) = Vec2(this.x - other.x, this.y - other.y)
    operator fun plus(other: Vec2) = Vec2(this.x + other.x, this.y + other.y)
    operator fun times(scalar: Int) = Vec2(this.x * scalar, this.y * scalar)
}

fun main() {
    val lines = input.readLines()
    val mapYRange = lines.indices
    val mapXRange = lines[0].indices
    val typeToPosition = createLookup(lines)

    repeat(100_000) {
    val antiNodeLocations1 = measureTimedValue {
        part1(
            typeToPosition = typeToPosition
        ) {
            x in mapXRange && y in mapYRange
        }
    }
    val antiNodeLocations2 = measureTimedValue {
        part2(
            typeToPosition = typeToPosition
        ) {
            x in mapXRange && y in mapYRange
        }
    }
    println("${antiNodeLocations1.value.size} in ${antiNodeLocations1.duration}")
    println("${antiNodeLocations2.value.size} in ${antiNodeLocations2.duration}")
    }
}

fun List<String>.getCharacter(x: Int, y: Int) = this[y][x]

private fun createLookup(lines: List<String>): Map<Char, Set<Vec2>> = buildMap<Char, MutableSet<Vec2>> {
    for (y in lines.indices) {
        for (x in lines[0].indices) {
            val chr = lines.getCharacter(x, y)
            if (chr == '.') {
                continue
            }
            val ttpEntry = this.getOrElse(chr) { mutableSetOf() }
            ttpEntry.add(Vec2(x, y))
            this[chr] = ttpEntry
        }
    }
}


fun part1(
    typeToPosition: Map<Char, Set<Vec2>>,
    isInBounds: Vec2.() -> Boolean,
): Set<Vec2> = buildSet {
    for (antennaType in typeToPosition.keys) {
        for (first in typeToPosition[antennaType]!!) {
            for (second in typeToPosition[antennaType]!!) {
                if (first == second) continue
                val distanceVec = second - first
                val relDistVec = distanceVec * 2
                val absAntiNodeVec = first + relDistVec
                if (absAntiNodeVec.isInBounds()) {
                    // MAKE SURE YOU ONLY COUNT WHATS IN THE MAP
                    add(absAntiNodeVec)
                }
            }
        }
    }
}

private fun part2(
    typeToPosition: Map<Char, Set<Vec2>>,
    isInBounds: Vec2.() -> Boolean,
): Set<Vec2> = buildSet {
    for (antennaType in typeToPosition.keys) {
        for (first in typeToPosition[antennaType]!!) {
            for (second in typeToPosition[antennaType]!!) {
                if (first == second) continue
                val distanceVec = second - first
                val relDistVec = distanceVec
                var positiveNextLocation: Vec2 = first
                do {
                    positiveNextLocation = positiveNextLocation + relDistVec
                    if (positiveNextLocation.isInBounds()) {
                        this.add(positiveNextLocation)
                    }
                } while (positiveNextLocation.isInBounds())
                var negativeNextLocation: Vec2 = first
                do {
                    negativeNextLocation = negativeNextLocation - relDistVec
                    if (negativeNextLocation.isInBounds()) {
                        this.add(negativeNextLocation)
                    }
                } while (positiveNextLocation.isInBounds())
            }
        }
    }
}