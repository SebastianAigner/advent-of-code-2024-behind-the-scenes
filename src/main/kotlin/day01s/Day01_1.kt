package day01s

import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.abs

fun readInput(name: String) = Path("input/$name").readLines()

fun main() {
    val lines = readInput("day01.txt")
    val (first, second) = lines.map { line ->
        val left = line.substringBefore(" ").toInt()
        val right = line.substringAfterLast(" ").toInt()
        left to right
    }.unzip()

    val result = first.sorted().zip(second.sorted()).map { (first, second) ->
        abs(first - second)
    }.sum()
    println(result)
}
