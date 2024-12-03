package day03nicole

import readInput

private const val DAY = "03"

private const val mulRegex = """mul\((\d{1,3}),(\d{1,3})\)"""
private const val doRegex = """do\(\)"""
private const val dontRegex = """don't\(\)"""

// PART 1
fun addValidMultiplicationsPart1(section: String): Long {
    return mulRegex.toRegex().findAll(section).sumOf {
        val (first, second) = it.destructured
        first.toLong() * second.toLong()
    }
}

// PART 2
fun addValidMultiplicationsPart2(fullMemory: String): Long {
    var sum = 0L
    var enabled = true

    """$mulRegex|$doRegex|$dontRegex""".toRegex().findAll(fullMemory).forEach { match ->
        when (match.value) {
            "don't()" -> enabled = false
            "do()" -> enabled = true
            else -> if (enabled) sum += match.multiplyNumbers()
        }
    }
    return sum
}

private fun MatchResult.multiplyNumbers(): Long {
    val (first, second) = destructured
    return first.toLong() * second.toLong()
}

fun main() {
    fun part1(input: List<String>): Long {
        return input.sumOf { addValidMultiplicationsPart1(it) }
    }

    fun part2(input: List<String>): Long {
        return addValidMultiplicationsPart2(input.joinToString())
    }

    println("\n--- DAY $DAY ---\n")

    // TEST
    val testInput = readInput("day${DAY}/Day${DAY}_test")
    val testOutput = part1(testInput)
    println("TEST 1: $testOutput")
    check(testOutput == 161L)

    val testInput2 = readInput("day${DAY}/Day${DAY}_test_2")
    val testOutput2 = part2(testInput2)
    println("TEST 2: $testOutput2")
    check(testOutput2 == 48L)

    // REAL INPUT
    val input = readInput("day${DAY}/Day$DAY")
    println("PART 1: " + part1(input)) //153469856
    println("PART 2: " + part2(input)) //77055967
}
