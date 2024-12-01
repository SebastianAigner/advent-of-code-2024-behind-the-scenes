package day01s


fun main() {
    val lines = readInput("day01.txt")
    val (first, second) = lines.map { line ->
        line.split(Regex("\\s+")).let { it[0].toInt() to it[1].toInt() }
    }.unzip()

    val frequencies: Map<Int, Int> = second.groupingBy { it }.eachCount()
    first.fold(0) { acc, current ->
        acc + current * frequencies.getOrDefault(current, 0)
    }.also(::println)
}