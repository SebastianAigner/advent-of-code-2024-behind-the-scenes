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
        if(dampened.isListSafe()) return true
    }
    return false
}

fun String.isSafe(): Boolean {
    val numbers = this.split(" ").map { string -> string.toInt() }
    return numbers.isListSafe()
}

private fun List<Int>.isListSafe(): Boolean {
    val pairs = zipWithNext()
    val allAreIncreasing = pairs.all { pair ->
        pair.second - pair.first in 1..3
    }
    val allAreDecreasing = pairs.all { pair ->
        val slope = pair.second - pair.first
        println("$pair -> $slope")
        slope in (-3)..(-1)
    }
    return allAreIncreasing || allAreDecreasing
}