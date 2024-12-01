package day01r

import kotlinx.benchmark.*
import kotlin.collections.unzip

@State(Scope.Benchmark)
class MyBenchmark {
    val sbsList = input.readLines().map { line ->
        val (a, b) = line.split("   ")
        a.toInt() to b.toInt()
    }

    lateinit var firstColumn: List<Int>
    lateinit var secondColumn: List<Int>

    init {
        val (first, second) = sbsList.unzip()
        firstColumn = first
        secondColumn = second
    }

    @Benchmark
    fun benchmarkNormal() {
        part2(firstColumn, secondColumn)
    }

    @Benchmark
    fun benchmarkMap() {
        part2Map(firstColumn, secondColumn)
    }
}