@file:Suppress("DuplicatedCode")

package day03r

import java.io.File

val input = File("input/day03.txt").readLines()

fun main() {
    part1(input)
    part2(input)
}

fun part1(lines: List<String>) {
    val mulRegex = """mul\(\d{1,3},\d{1,3}\)""".toRegex()
    val all = lines.flatMap { string ->
        mulRegex.findAll(string).map { it.value }
    }
    val total = all.sumOf { instruction ->
        val (a, b) = instruction
            .removeSurrounding("mul(", ")")
            .split(",")
        a.toInt() * b.toInt()
    }
    println(total)
}

fun part2(lines: List<String>) {
    val mulRegex = """mul\(\d{1,3},\d{1,3}\)|do(n't)*\(\)""".toRegex()
    val all = lines.flatMap { string ->
        mulRegex.findAll(string).map { it.value }
    }
    println(all)
    var enabled = true
    var acc = 0
    for (instruction in all) {
        when {
            instruction == "do()" -> enabled = true
            instruction == "don't()" -> enabled = false
            enabled && instruction.startsWith("mul(") -> {
                val (a, b) = instruction
                    .removeSurrounding("mul(", ")")
                    .split(",")
                acc += a.toInt() * b.toInt()
            }
        }
    }
    println(acc)
}