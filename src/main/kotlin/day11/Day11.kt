package day11

import java.io.File
import java.math.BigInteger

val input = File("input/day11.txt")

fun main() {
    var line = input.readLines().first().split(" ").map { string -> string.toBigInteger() }
    repeat(75) {
        println(it)
        line = blink(line)
    }
    println(line.size)
}

fun blink(integers: List<BigInteger>): List<BigInteger> {
    return buildList<BigInteger> {
        for (i in integers) {
            when {
                i == BigInteger.ZERO -> {
                    add(BigInteger.ONE)
                }

                i.toString().length % 2 == 0 -> { // even digits
                    val len = i.toString().length
                    add(i.toString().take(len / 2).toBigInteger())
                    add(i.toString().takeLast(len / 2).toBigInteger())
                }

                else -> {
                    add(i * BigInteger.valueOf(2024))
                }
            }
        }
    }
}
