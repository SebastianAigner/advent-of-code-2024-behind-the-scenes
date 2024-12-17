package day17

import java.io.File
import kotlin.math.pow

val input = File("input/day17.txt")

fun main() {
    val lines = input.readLines()
    var regA = lines[0].takeLastWhile { it.isDigit() }.toInt()
    var regB = lines[1].takeLastWhile { it.isDigit() }.toInt()
    var regC = lines[2].takeLastWhile { it.isDigit() }.toInt()
    var ip = 0
    val program = lines[4].removePrefix("Program: ").split(",").map { it.toInt() }
    fun valueOfComboOp(op: Int): Int {
        return when (op) {
            in 0..3 -> {
                op
            }

            4 -> regA
            5 -> regB
            6 -> regC
            7 -> error("not a valid program")
            else -> error("not valid either!")
        }
    }

    val final = StringBuilder()
    try {
        do {
            val inst = program[ip]
            println("INST $inst at IP $ip")
            when (inst) {
                0 -> {
                    // ADV num/denom
                    val num = regA
                    val denom = 2.0.pow(valueOfComboOp(program[++ip]).toDouble()).toInt()
                    val res = num / denom
                    regA = res
                    ip++
                }

                1 -> {
                    // BXL
                    val x = regA
                    val lit = program[++ip]
                    regB = x xor lit
                    ip++
                }

                2 -> {
                    // BST
                    val co = valueOfComboOp(program[++ip])
                    val modded = co % 8
                    regB = modded
                    ip++
                }

                3 -> {
                    // JNZ
                    if (regA == 0) {
                        ip += 2
                    } else {
                        ip = program[++ip]
                        println("jumping to $ip")
                    }
                }

                4 -> {
                    // BXC
                    val new = regB xor regC
                    regB = new
                    ip += 2
                }

                5 -> {
                    // OUT
                    val output = valueOfComboOp(program[++ip]) % 8
                    println("OUT $output")
                    final.append("$output,")
                    ip++
                }

                6 -> {
                    // BDV
                    val num = regA
                    val denom = 2.0.pow(valueOfComboOp(program[++ip]).toDouble()).toInt()
                    val res = num / denom
                    regB = res
                    ip++
                }

                7 -> {
                    val num = regA
                    val denom = 2.0.pow(valueOfComboOp(program[++ip]).toDouble()).toInt()
                    val res = num / denom
                    regC = res
                    ip++
                }
            }
            println("A $regA  B $regB  C $regC  D $ip")
        } while (true)
    } catch (e: Exception) {
        println(e.stackTraceToString())
    }
    println(final.removeSuffix(","))
}
