package day07

import java.io.File
import kotlin.math.log10
import kotlin.math.pow
import kotlin.time.measureTimedValue

val input = File("input/day07.txt")

data class Calibration(val result: Long, val operands: List<Long>) {
    fun isValid(): Boolean {
        // essentially, we generate a binary number that indicates
        // 0: plus
        // 1: times
        // and then, we iterate over those.
        val operandSlots = operands.size - 1
        val possibleCombos = 2.0.pow(operandSlots.toDouble()).toInt()
        for (operatorCombination in 0..<possibleCombos) {
            var calcRes = operands[0]
            for (index in 0..<operands.lastIndex) {
                // excludes last index
                val a = calcRes
                val b = operands[index + 1]
                val op = (operatorCombination shr index) and 0x1
                when (op) {
                    0 -> calcRes = a + b
                    1 -> calcRes = a * b
                }
            }
            if (calcRes == result) return true
        }
        return false
    }


    fun isValid2(): Boolean {
        // essentially, we generate a ternary number that indicates
        // 0: plus
        // 1: times
        // 2: concat
        // and then, we iterate over those.
        val operandSlots = operands.size - 1
        val possibleCombos = 3.0.pow(operandSlots.toDouble()).toInt()
        for (operatorCombination in 0..<possibleCombos) {
            var calcRes = operands[0]
            for (index in 0..<operands.lastIndex) {
                // excludes last index
                val a = calcRes
                val b = operands[index + 1]
                val opstr = operatorCombination.toString(3)
                val op = opstr.getOrNull(opstr.lastIndex - index) ?: '0'
                when (op) {
                    '0' -> calcRes = a + b
                    '1' -> calcRes = a * b
                    '2' -> calcRes = (a.toString() + b.toString()).toLong()
                }
            }
            if (calcRes == result) return true
        }
        return false
    }

    fun isValid2Stringless(): Boolean {
        // essentially, we generate a ternary number that indicates
        // 0: plus
        // 1: times
        // 2: concat
        // and then, we iterate over those.
        val operandSlots = operands.size - 1
        val possibleCombos = 3.0.pow(operandSlots.toDouble()).toInt()
        for (operatorCombination in 0..<possibleCombos) {
            var calcRes = operands[0]
            for (index in 0..<operands.lastIndex) {
                // excludes last index
                val a = calcRes
                val b = operands[index + 1]
                // val divideBy = Math.pow(3.0, index.toDouble())
                var op = operatorCombination // / divideBy
                repeat(index) {
                    op /= 3
                }
                op %= 3
                when (op) {
                    0 -> calcRes = a + b
                    1 -> calcRes = a * b
                    2 -> calcRes = (a.toString() + b.toString()).toLong()
                }
            }
            if (calcRes == result) return true
        }
        return false
    }

    fun isValid2TotallyStringless(): Boolean {
        // essentially, we generate a ternary number that indicates
        // 0: plus
        // 1: times
        // 2: concat
        // and then, we iterate over those.
        val operandSlots = operands.size - 1
        val possibleCombos = 3.0.pow(operandSlots.toDouble()).toInt()
        for (operatorCombination in 0..<possibleCombos) {
            var calcRes = operands[0]
            for (index in 0..<operands.lastIndex) {
                // excludes last index
                val a = calcRes
                val b = operands[index + 1]
                // val divideBy = Math.pow(3.0, index.toDouble())
                var op = operatorCombination // / divideBy
                repeat(index) {
                    op /= 3
                }
                op %= 3

                when (op) {
                    0 -> calcRes = a + b
                    1 -> calcRes = a * b
                    2 -> {
                        val digits = log10(b.toDouble()).toInt() + 1
                        val concat = (10.0.pow(digits)).toInt() * a + b
                        calcRes = concat
                    }
                }
            }
            if (calcRes == result) return true
        }
        return false
    }
}

// one more version that replaces the string-based shift-right by repeatedly dividing by three instead!

fun main() {
    val lines = input.readLines()
    val calibs = lines.map { line ->
        val (res, ops) = line.split(": ")
        Calibration(res.toLong(), ops.split(" ").map { num -> num.toLong() })
    }
    print("Maximum number of observed operands: ")
    println(calibs.maxOf { calibration -> calibration.operands.size }) // 12 for my input; 2^12 and 4^12 (16,777,216) seems doable.
    print("Has zero as operand: ")
    println(calibs.flatMap { calibration -> calibration.operands }.any { lng -> lng == 0L })
    print("Has negative as operand: ")
    println(calibs.flatMap { calibration -> calibration.operands }.any { lng -> lng < 0L })
    val partA = measureTimedValue { part1(calibs) }
    println(partA)
    check(partA.value == 14711933466277)

    val partB = measureTimedValue { part2(calibs) }

    println(partB)
    check(partB.value == 286580387663654)
}

private fun part1(calibs: List<Calibration>): Long = calibs.filter { calibration ->
    calibration.isValid()
}.sumOf { calibration ->
    calibration.result
}

private fun part2(calibs: List<Calibration>): Long {
    return calibs.filter { calibration ->
        calibration.isValid2TotallyStringless()
    }.sumOf { calibration ->
        calibration.result
    }

//    return runBlocking(Dispatchers.Default) {
//        calibs
//            .map { calibration ->
//                async {
//                    if (calibration.isValid2TotallyStringless()) calibration.result else 0
//                }
//            }
//            .awaitAll()
//            .sum()
//    }
}

// baseline:
// TimedValue(value=14711933466277, duration=13.555125ms)
// TimedValue(value=286580387663654, duration=6.057535792s)

// totallystringless (p2):
// TimedValue(value=286580387663654, duration=4.195472541s)