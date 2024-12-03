package benchmark

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import readInput
import readInputAsString

@State(Scope.Benchmark)
class BenchmarkDay03 {

    val day = "03"
    val inputFileName = "day$day/Day$day"
    var input = ""

//    @Benchmark
    fun joinStrings(): String {
        return readInput(inputFileName).joinToString()
    }

//    @Benchmark
    fun readAsString(): String {
        return readInputAsString(inputFileName)
    }

    @Setup
    fun setup() {
        input = readInputAsString(inputFileName)
    }

    @Benchmark
    fun day03Part1NoLabelsDestructured(): Long {
        return day03nicole.addValidMultiplicationsPart1(input)
    }

    @Benchmark
    fun day03Part1WithLabels(): Long {
        return day03nicole.addValidMultiplicationsPart1WithLabels(input)
    }

    @Benchmark
    fun day03Part1WithGroupValues(): Long {
        return day03nicole.addValidMultiplicationsPart1WithLabels(input)
    }

    @Benchmark
    fun day03Part1WithGroupValuesAndLabels(): Long {
        return day03nicole.addValidMultiplicationsPart1WithLabels(input)
    }
}


/*

** Regex **
main summary:
Benchmark                                           Mode  Cnt     Score     Error  Units
BenchmarkDay03.day03Part1NoLabelsDestructured      thrpt    5  8179.108 ±  25.055  ops/s
BenchmarkDay03.day03Part1WithGroupValues           thrpt    5  5425.929 ±  63.386  ops/s
BenchmarkDay03.day03Part1WithGroupValuesAndLabels  thrpt    5  5237.704 ± 266.736  ops/s
BenchmarkDay03.day03Part1WithLabels                thrpt    5  5048.913 ±  38.074  ops/s

** Labels vs no Labels Regex **
main summary:
Benchmark                             Mode  Cnt     Score    Error  Units
BenchmarkDay03.day03Part1NoLabels    thrpt    5  8059.907 ± 58.932  ops/s
BenchmarkDay03.day03Part1WithLabels  thrpt    5  5694.817 ± 41.624  ops/s


** Read string input **
main summary:
Benchmark                Mode  Cnt      Score     Error  Units
ReadInput.joinStrings   thrpt    5  32878.485 ± 765.085  ops/s
ReadInput.readAsString  thrpt    5  48296.185 ± 708.021  ops/s

 */
