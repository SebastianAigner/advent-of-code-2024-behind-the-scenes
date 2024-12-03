package day03o

import java.io.File
import kotlin.text.removeSurrounding

val input = File("input/day03.txt").readLines()

sealed interface Instruction
data object Do : Instruction
data object Dont : Instruction
data class Mul(val a: Int, val b: Int) : Instruction

fun Instruction(instruction: String): Instruction {
    return when {
        instruction == "do()" -> Do
        instruction == "don't()" -> Dont
        instruction.startsWith("mul(") -> {
            val (a, b) = instruction.removeSurrounding("mul(", ")").split(",")
            Mul(a.toInt(), b.toInt())
        }

        else -> error("Unknown instruction $instruction")
    }
}

fun main() {
    val mulRegex = """mul\(\d{1,3},\d{1,3}\)|do(n't)?\(\)""".toRegex()
    val all = input.flatMap { string ->
        mulRegex.findAll(string).map { Instruction(it.value) }
    }
    println(all)
    var enabled = true
    var acc = 0L
    for (instruction in all) {
        when (instruction) {
            is Do -> enabled = true
            is Dont -> enabled = false
            is Mul if enabled -> acc += instruction.a * instruction.b
            is Mul /* disabled */ -> println("Encountered disabled $instruction")
        }
    }
    println(acc)
}