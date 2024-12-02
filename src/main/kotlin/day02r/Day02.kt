package day02r

import java.io.File

val input = File("input/day02.txt")

fun main() {
    val lines = input.readLines()
    val numbers = lines.map { it.split(" ").map { string -> string.toInt() } }
    part1(numbers)
    part2(numbers)
}

private fun part1(numbers: List<List<Int>>) {
    val safeEntries = numbers.count {
        it.isListSafe()
    }
    println(safeEntries)
}

private fun part2(numbers: List<List<Int>>) {
    val safeDampenedEntries = numbers.count {
        it.isSafeDampened()
    }
    println(safeDampenedEntries)
}

fun List<Int>.isSafeDampened(): Boolean {
    for (idx in this.indices) {
        val dampened = this.filterIndexed { index, i -> index != idx }
        if (dampened.isListSafe()) return true
    }
    return false
}

private fun List<Int>.isListSafe(): Boolean {
    val pairs = zipWithNext()
    val allAreIncreasing = pairs.all { pair ->
        pair.second - pair.first in 1..3
    }
    val allAreDecreasing = pairs.all { pair ->
        val slope = pair.second - pair.first
        slope in (-3)..(-1)
    }
    return allAreIncreasing || allAreDecreasing
}