package day02r

import java.io.File

val input = File("input/day02.txt")

fun main() {
    val lines = input.readLines()
    val reports = lines.map { line ->
        line.split(" ").map { numStr -> numStr.toInt() }
    }
    part1(reports)
    part2(reports)
}

private fun part1(reports: List<List<Int>>) {
    val safeEntries = reports.count {
        it.isReportSafe()
    }
    println(safeEntries)
}

private fun part2(reports: List<List<Int>>) {
    val safeDampenedEntries = reports.count {
        it.isDampenedReportSafe()
    }
    println(safeDampenedEntries)
}

private fun List<Int>.isReportSafe(): Boolean {
    val pairs = zipWithNext()
    val allAreIncreasing = pairs.all { (a, b) ->
        b - a in 1..3
    }
    val allAreDecreasing = pairs.all { (a, b) ->
        b - a in -3..-1
    }
    return allAreIncreasing || allAreDecreasing
}

fun List<Int>.isDampenedReportSafe(): Boolean {
    for (indexToRemove in this.indices) {
        val dampened = this.filterIndexed { index, _ -> index != indexToRemove }
        if (dampened.isReportSafe()) {
            return true
        }
    }
    return false
}