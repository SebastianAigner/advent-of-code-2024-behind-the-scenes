package day03o

import java.io.File

val input = File("input/day03.txt").readLines()

sealed interface Instruction
object Do : Instruction
object Dont : Instruction
class Mul(val a: Int, val b: Int) : Instruction

fun Instruction(instruction: String): Instruction {
    return when {
        instruction == "do()" -> Do
        instruction == "don't()" -> Dont
        instruction.startsWith("mul(") -> {
            val (a, b) = instruction.removePrefix("mul(").removeSuffix(")").split(",")
            Mul(a.toInt(), b.toInt())
        }
        else -> error("Unknown instruction $instruction")
    }
}

fun main() {
    val mulRegex = """mul\(\d{1,3},\d{1,3}\)|do(n't)*\(\)""".toRegex()
    val all = input.flatMap { string ->
        mulRegex.findAll(string).map { Instruction(it.value) }
    }
    println(all)
    var enabled = true
    var acc = 0L
    for (instruction in all) {
        when(instruction) {
            is Do -> enabled = true
            is Dont -> enabled = false
            is Mul if enabled -> acc += instruction.a * instruction.b
            is Mul -> println("Encountered disabled $instruction")
        }
    }
    println(acc)
}