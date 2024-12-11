package day11

import java.io.File
import java.math.BigInteger

val input = File("input/day11.txt")

fun main() {
    val line = input.readLines().first().split(" ").map { string -> string.toBigInteger() }
    var groups = line.groupingBy { integer -> integer }.eachCount()
    var nmList = NonMaterializedList()
    for ((k, v) in groups) {
        nmList.add(k, BigInteger.valueOf(v.toLong()))
    }
    repeat(75) {
        nmList = blinkGroups(nmList)
        println(nmList)
    }
    println(nmList.coll.values.sumOf { integer -> integer.toLong() })
}

fun blinkGroups(nmList: NonMaterializedList): NonMaterializedList {
    return NonMaterializedList().apply {
        for ((key, value) in nmList.coll) {
            when {
                key == BigInteger.ZERO -> {
                    add(BigInteger.ONE, value)
                }

                key.toString().length % 2 == 0 -> {
                    val len = key.toString().length
                    val top = key.toString().take(len / 2).toBigInteger()
                    val bot = key.toString().takeLast(len / 2).toBigInteger()
                    add(top, value)
                    add(bot, value)
                }

                else -> {
                    add(key * BigInteger.valueOf(2024), value)
                }
            }
        }
    }
}

fun blinkOne(i: BigInteger): List<BigInteger> {
    return when {
        i == BigInteger.ZERO -> {
            listOf(BigInteger.ONE)
        }

        i.toString().length % 2 == 0 -> { // even digits
            val len = i.toString().length
            listOf(
                i.toString().take(len / 2).toBigInteger(),
                i.toString().takeLast(len / 2).toBigInteger()
            )
        }

        else -> {
            listOf(i * BigInteger.valueOf(2024))
        }
    }
}

data class NonMaterializedList(val coll: MutableMap<BigInteger, BigInteger> = mutableMapOf()) {
    fun add(b: BigInteger, count: BigInteger) {
        val curr = coll[b] ?: BigInteger.ZERO
        coll[b] = curr + count
    }
}