package day01nice

import java.io.File
import kotlin.math.abs

val input = File("input/day01.txt")

fun main() {
    val sbsList = input.readLines().map { line ->
        val (a, b) = line.split("   ")
        a.toInt() to b.toInt()
    }
    val (firstColumn, secondColumn) = sbsList.unzip()
    part1(firstColumn, secondColumn)
    part2(secondColumn, firstColumn)
}

private fun part1(
    firstColumn: List<Int>,
    secondColumn: List<Int>,
) {
    val sortedColumnPairs = firstColumn.sorted().zip(secondColumn.sorted())
    val distances = sortedColumnPairs.sumOf { (firstColumnItem, secondColumnItem) ->
        abs(firstColumnItem - secondColumnItem)
    }
    println(distances)

}

private fun part2(
    secondColumn: List<Int>,
    firstColumn: List<Int>,
) {
    val sum = firstColumn.sumOf { firstColumnItem: Int ->
        firstColumnItem * secondColumn.count { secondColumnItem -> secondColumnItem == firstColumnItem }
    }
    println(sum)

    val counts = secondColumn.groupingBy { it }.eachCount()
    val fasterSum = firstColumn.sumOf { firstColumnItem: Int ->
        firstColumnItem * (counts[firstColumnItem] ?: 0)
    }
    println(fasterSum)
}

