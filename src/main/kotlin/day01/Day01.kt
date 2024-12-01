package day01

import java.io.File
import kotlin.math.abs

val input = File("input/day01.txt")

fun main() {
    val foo = input.readLines().map { string ->
        val (a, b) = string.split("   ")
        a to b
    }
    val theAs = foo.map { pair -> pair.first.toInt() }.sorted()
    val theBs = foo.map { pair -> pair.second.toInt() }.sorted()
    val paired = theAs.zip(theBs)
    val distances = paired.map { pair -> abs(pair.first - pair.second) }
    println(distances.sum())
    val sum= paired.sumOf { pair ->
        pair.first * theBs.count { i -> i == pair.first }
    }
    println(sum)
}