package day02m

import java.io.File
import kotlin.math.absoluteValue

fun main() {
//    operator fun Int.rangeUntil(other: Int): IntRange = this until other

    fun isLineSafe(numbers: List<Int>): Boolean {
        var safe = true
        var isUp = true
        var isDown = true
        for (i in 0..numbers.lastIndex - 1) {
            val a = numbers[i]
            val b = numbers[i + 1]
            safe = safe && ((a - b).absoluteValue <= 3)
//            safe &&= ((a - b).absoluteValue <= 3)
            when {
                a < b -> isDown = false
                b < a -> isUp = false
                else -> {
                    isUp = false
                    isDown = false
                }
            }
        }
        return safe && (isUp || isDown)
    }


    fun part1(input: List<String>): Int {
        var result = 0
        for (line in input) {
            val numbers = line.split(" ").map { it.toInt() }
            if (isLineSafe(numbers)) result++
        }

        return result
    }

    fun part2(input: List<String>): Int {
        var result = 0
        for (line in input) {
            val numbers = line.split(" ").map { it.toInt() }
            var safe = false
            for (i in 0..numbers.lastIndex) {
                safe = isLineSafe(numbers.toMutableList().apply { removeAt(i) })
                if (safe) break
            }

            if (safe) result++
        }

        return result
    }

    val input = File("input/day02.txt").readLines()
    part1(input).println()
    part2(input).println()
}

fun Any?.println() {
    println(this)
}