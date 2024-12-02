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
    fun benchmarkNormal(): Int {
        return part2(firstColumn, secondColumn)
    }

    @Benchmark
    fun benchmarkMap(): Int {
        return part2Map(firstColumn, secondColumn)
    }
}

/*
main summary:
Benchmark                     Mode  Cnt      Score      Error  Units
MyBenchmark.benchmarkMap     thrpt    5  41199,401 ± 2259,729  ops/s
MyBenchmark.benchmarkNormal  thrpt    5   1444,920 ±   17,086  ops/s

main summary:
Benchmark                     Mode  Cnt      Score      Error  Units
MyBenchmark.benchmarkMap     thrpt    5  41940,628 ± 1565,903  ops/s
MyBenchmark.benchmarkNormal  thrpt    5   1441,546 ±   10,528  ops/s
 */