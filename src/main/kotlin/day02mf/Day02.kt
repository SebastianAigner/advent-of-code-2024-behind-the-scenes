package day02mf

import day02m.println
import java.io.File

fun main() {
    fun isLineSafeFunctional(numbers: List<Int>): Boolean {
        val differences = numbers.zipWithNext { a, b -> a - b }
        return differences.all { it in -3..3 } &&
                (differences.all { it > 0 } || differences.all { it < 0 })
    }

    fun part1Functional(input: List<List<Int>>): Int =
        input.count(::isLineSafeFunctional)

    fun part2Functional(input: List<List<Int>>): Int =
        input.count { numbers ->
            numbers.indices.any {
                val skipped = numbers.toMutableList().apply { removeAt(it) }
                isLineSafeFunctional(skipped)
            }
        }

    val input = File("input/day02.txt").readLines()
    val lines = input.map { line -> line.split(" ").map { it.toInt() } }
    part1Functional(lines).println()
    part2Functional(lines).println()
}