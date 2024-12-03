package day03r

import java.io.File

val input = File("input/day03.txt").readLines()

fun main() {
    val mulRegex = """mul\(\d{1,3},\d{1,3}\)|do(n't)*\(\)""".toRegex()
    val all = input.flatMap { string ->
        mulRegex.findAll(string).map { it.value }
    }
    println(all)
    var enabled = true
    var acc = 0L
    for (instruction in all) {
        when {
            instruction == "do()" -> enabled = true
            instruction == "don't()" -> enabled = false
            enabled && instruction.startsWith("mul(") -> {
                val (a, b) = instruction
                    .removePrefix("mul(")
                    .removeSuffix(")")
                    .split(",")
                acc += a.toInt() * b.toInt()
            }
        }
    }
    println(acc)
}