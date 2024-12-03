package day03

import java.io.File

fun main() {
    val x = File("input/day03.txt").readLines()
    val mulRegex = """mul\(\d{1,3},\d{1,3}\)|do(n't)?\(\)""".toRegex()
    val all = x.flatMap { string ->
        mulRegex.findAll(string).map { it.value }
    }
    println(all)
    var enabled = true
    var acc = 0L
    for (instruction in all) {
        if (instruction == "do()") {
            enabled = true
        }
        if (instruction == "don't()") {
            enabled = false
        }
        if (enabled && instruction.startsWith("mul(")) {
            val (a, b) = instruction.removePrefix("mul(").removeSuffix(")").split(",")
            acc += a.toInt() * b.toInt()
        }
    }
    println(acc)
}