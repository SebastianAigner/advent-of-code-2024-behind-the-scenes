package day03nicole

import readInputAsString

private const val DAY = "03"

// PART 1 with labels
private const val mulRegex = """mul\((\d{1,3}),(\d{1,3})\)"""
private const val mulRegexWithLabels = """mul\((?<first>\d{1,3}),(?<second>\d{1,3})\)"""

private const val chunkDontsRegex = """don't\(\).*do\(\)"""
private const val chunkDontsEndRegex = """don't\(\).*"""

fun addValidMultiplicationsPart1WithLabels(section: String): Long {
    return mulRegexWithLabels.toRegex().findAll(section).sumOf { match ->
        val first = match.groups["first"]?.value?.toLong() ?: 0
        val second = match.groups["second"]?.value?.toLong() ?: 0
        first * second
    }
}

fun addValidMultiplicationsPart1GroupValues(section: String): Long {
    return mulRegex.toRegex().findAll(section).sumOf { match ->
        val first = match.groupValues[1].toLong()
        val second = match.groupValues[2].toLong()
        first * second
    }
}

fun addValidMultiplicationsPart1GroupValuesAndLabels(section: String): Long {
    return mulRegexWithLabels.toRegex().findAll(section).sumOf { match ->
        val first = match.groupValues[1].toLong()
        val second = match.groupValues[2].toLong()
        first * second
    }
}

//fun part2Chuncked(memory: String): Long {
//    return addValidMultiplicationsPart1(
//        chunkDontsRegex.toRegex().replace(memory, "X").replace(chunkDontsEndRegex.toRegex(), "X")
//    )
//}

fun main() {
    fun part2WithStringInput(input: String): Long {
        return addValidMultiplicationsPart2(input)
    }

    println("\n--- DAY $DAY Alternatives ---\n")

    // Optional ways
    val inputString = readInputAsString("day${DAY}/Day$DAY")
    println("PART 1 with Labels: " + addValidMultiplicationsPart1WithLabels(inputString))
    println("PART 2 with String: " + part2WithStringInput(inputString))
//    println("PART 2 with chunks: " + part2Chuncked(inputString))

}
