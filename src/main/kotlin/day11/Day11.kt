package day11

import java.io.File
import kotlin.time.measureTimedValue

val input = File("input/day11.txt")

fun main() {
    val line = input.readLines().first().split(" ").map { string -> string.toLong() }
    val a = measureTimedValue { part1(line) }
    println("Part 1: $a")
    val b = measureTimedValue { part2(line) }
    println("Part 2: $b")
    check(a.value == 183435)
    check(b.value == 218279375708592)
}

private fun part1(line: List<Long>): Int {
    var modifiedLine = line
    repeat(25) {
        println(it)
        modifiedLine = blink(modifiedLine)
    }
    return modifiedLine.size
}

fun blink(stones: List<Long>): List<Long> {
    return buildList<Long> {
        for (stone in stones) {
            when {

                stone == 0L -> {
                    add(1L)
                }

                stone.toString().length % 2 == 0 -> { // even digits
                    val len = stone.toString().length
                    add(stone.toString().take(len / 2).toLong())
                    add(stone.toString().takeLast(len / 2).toLong())
                }

                else -> {
                    add(stone * 2024L)
                }
            }
        }
    }
}

private fun part2(line: List<Long>): Long {
    var stoneCounts = line.groupingBy { num -> num }.eachCount()
    var stoneList = NonMaterializedList()
    for ((k, v) in stoneCounts) {
        stoneList.add(k.toLong(), v.toLong())
    }
    repeat(75) {
        stoneList = blinkGroups(stoneList)
    }
    return stoneList.coll.values.sumOf { integer -> integer.toLong() }
}

fun blinkGroups(stoneList: NonMaterializedList): NonMaterializedList {
    return buildNonMaterializedList {
        for ((stone, count) in stoneList.coll) {
            when {
                stone == 0L -> {
                    add(1L, count)
                }

                stone.toString().length % 2 == 0 -> {
                    val len = stone.toString().length
                    val top = stone.toString().take(len / 2).toLong()
                    val bot = stone.toString().takeLast(len / 2).toLong()
                    add(top, count)
                    add(bot, count)
                }

                else -> {
                    add(stone * 2024L, count)
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
