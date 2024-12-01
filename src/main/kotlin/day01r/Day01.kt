package day01r

import java.io.File
import kotlin.math.abs

val input = File("input/day01.txt")

fun main() {
    val sbsList = input.readLines().map { line ->
        val (a, b) = line.split("   ")
        a.toInt() to b.toInt()
    }
    val (firstColumn, secondColumn) = sbsList.unzip()
    println(part1(firstColumn, secondColumn))
    println(part2(firstColumn, secondColumn))
    println(part2Map(firstColumn, secondColumn))
}

private fun part1(
    firstColumn: List<Int>,
    secondColumn: List<Int>,
): Int {
    val sortedColumnPairs = firstColumn.sorted().zip(secondColumn.sorted())
    val distances = sortedColumnPairs.sumOf { (firstColumnItem, secondColumnItem) ->
        abs(firstColumnItem - secondColumnItem)
    }
    return distances
}

fun part2(
    firstColumn: List<Int>,
    secondColumn: List<Int>,
): Int {
    val sum = firstColumn.sumOf { firstColumnItem: Int ->
        firstColumnItem * secondColumn.count { secondColumnItem -> secondColumnItem == firstColumnItem }
    }
    return sum
}

fun part2Map(
    firstColumn: List<Int>,
    secondColumn: List<Int>,
): Int {
    val counts = secondColumn.groupingBy { it }.eachCount()
    val fasterSum = firstColumn.sumOf { firstColumnItem: Int ->
        firstColumnItem * (counts[firstColumnItem] ?: 0)
    }
    return fasterSum
}

