package day07

import java.io.File
import kotlin.math.pow

val input = File("input/day07.txt")

data class Calibration(val result: Long, val operands: List<Long>) {
    fun isValid(): Boolean {
        // essentially, we generate a binary number that indicates
        // 0: plus
        // 1: times
        // and then, we iterate over those.
        val operandSlots = operands.size - 1
        val possibleCombos = 2.0.pow(operandSlots.toDouble()).toInt()
        println("possible $possibleCombos")
        for(operatorCombination in 0..<possibleCombos) {
            println("trying $operatorCombination com")
            var calcRes = operands[0]
            for(index in 0..<operands.lastIndex) {
                // excludes last index
                val a = calcRes
                val b = operands[index+1]
                val op = (operatorCombination shr index) and 0x1
                if(op == 0) {
                    calcRes = a + b
                } else {
                    calcRes = a * b
                }
            }
            if(calcRes == result) return true
        }
        return false
    }
}

fun main() {
    val lines = input.readLines()
    val calibs = lines.map { line ->
        val (res, ops) = line.split(": ")
        Calibration(res.toLong(), ops.split(" ").map { num -> num.toLong() })
    }
//    println(calibs.maxOf { calibration -> calibration.operands.size }) // 12 for my input; 2^12 and 4^12 (16777216) seems doable.
    calibs.filter { calibration ->
        calibration.isValid()
    }.sumOf { calibration ->
        calibration.result
    }.also(::println)
}