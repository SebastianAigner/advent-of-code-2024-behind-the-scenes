package day02

import java.io.File

val input = File("input/day02.txt")

fun main() {
    val lines = input.readLines()
    lines.count {
        it.isSafe()
    }.also(::println)

    lines.count {
        it.isSafeDampened()
    }.also(::println)
}

fun String.isSafeDampened(): Boolean {
    val numbers = this.split(" ").map { string -> string.toInt() }
    for(idx in numbers.indices) {
        val dampened = numbers.filterIndexed { index, i -> index != idx }
        if(isListSafe(dampened)) return true
    }
    return false
}

fun String.isSafe(): Boolean {
    val numbers = this.split(" ").map { string -> string.toInt() }
    return isListSafe(numbers)
}

private fun isListSafe(levels: List<Int>): Boolean {
    val levelPairs = levels.zipWithNext()
    val allAreIncreasing = levelPairs.all { (a, b) ->
        b - a in 1..3
    }
    val allAreDecreasing = levelPairs.all { (a, b) ->
        b - a in (-3)..(-1)
    }
    return allAreIncreasing || allAreDecreasing
}