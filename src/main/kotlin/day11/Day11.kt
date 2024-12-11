package day11

import java.io.File

val input = File("input/day11.txt")

fun main() {
    val line = input.readLines().first().split(" ").map { string -> string.toLong() }
    val a = part1(line)
    println("Part 1: $a")
    val b = part2(line)
    println("Part 2: $b")
    check(a == 183435)
    check(b == 218279375708592)
}

private fun part1(line: List<Long>): Int {
    var modifiedLine = line
    repeat(25) {
        println(it)
        modifiedLine = blink(modifiedLine)
    }
    return modifiedLine.size
}

fun blink(numbers: List<Long>): List<Long> {
    return buildList<Long> {
        for (number in numbers) {
            when {
                number == 0L -> {
                    add(1L)
                }

                number.toString().length % 2 == 0 -> { // even digits
                    val len = number.toString().length
                    add(number.toString().take(len / 2).toLong())
                    add(number.toString().takeLast(len / 2).toLong())
                }

                else -> {
                    add(number * 2024L)
                }
            }
        }
    }
}

private fun part2(line: List<Long>): Long {
    var groups = line.groupingBy { num -> num }.eachCount()
    var nmList = NonMaterializedList()
    for ((k, v) in groups) {
        nmList.add(k.toLong(), v.toLong())
    }
    repeat(75) {
        nmList = blinkGroups(nmList)
    }
    return nmList.coll.values.sumOf { integer -> integer.toLong() }
}

fun blinkGroups(nmList: NonMaterializedList): NonMaterializedList {
    return buildNonMaterializedList {
        for ((number, count) in nmList.coll) {
            when {
                number == 0L -> {
                    add(1L, count)
                }

                number.toString().length % 2 == 0 -> {
                    val len = number.toString().length
                    val top = number.toString().take(len / 2).toLong()
                    val bot = number.toString().takeLast(len / 2).toLong()
                    add(top, count)
                    add(bot, count)
                }

                else -> {
                    add(number * 2024L, count)
                }
            }
        }
    }
}

data class NonMaterializedList(val coll: MutableMap<Long, Long> = mutableMapOf()) {
    fun add(b: Long, count: Long) {
        val curr = coll[b] ?: 0L
        coll[b] = curr + count
    }
}

fun buildNonMaterializedList(builder: NonMaterializedList.() -> Unit): NonMaterializedList {
    val list = NonMaterializedList()
    list.builder()
    return list
}
