package day08vitaly

import daveutils.println
import java.io.File
import kotlin.math.max
import kotlin.math.min

data class Point(val row: Int, val col: Int)
class Line(val start: Point, val end: Point, width: Int, height: Int) {

    private val deltaRow = end.row - start.row
    private val deltaCol = end.col - start.col
    val params = calculateParams(width, height)

    fun getPoint(t: Int) = if (t in params) {
        Point(
            start.row + t * deltaRow,
            start.col + t * deltaCol
        )
    } else null

    private fun calculateParams(width: Int, height: Int): IntRange {
        val (rowStart, rowEnd) = calculateRange(deltaRow, start.row, height)
        val (colStart, colEnd) = calculateRange(deltaCol, start.col, width)
        return max(rowStart, colStart).rangeTo(min(rowEnd, colEnd))
    }

    private fun calculateRange(delta: Int, start: Int, boundary: Int): Pair<Int, Int> {
        return if (delta > 0) {
            -start / delta to (boundary - start - 1) / delta
        } else {
            (boundary - start - 1) / delta to -start / delta
        }
    }
}

fun main() {

    fun readAntennas(input: List<String>): Map<Char, List<Point>> {
        val antennas = hashMapOf<Char, MutableList<Point>>()
        input.mapIndexed { row, line ->
            line.mapIndexed { col, c ->
                if (input[row][col] != '.') {
                    antennas.getOrPut(input[row][col]) { mutableListOf() }.add(Point(row, col))
                }
            }
        }.flatten()
        return antennas
    }

    fun allAntennaLines(antennas: Map<Char, List<Point>>, width: Int, height: Int) =
        antennas.flatMap { (_, points) ->
            points.flatMapIndexed { ix, point1 ->
                points.drop(ix + 1).map { point2 ->
                    Line(point1, point2, width, height)
                }
            }
        }

    fun solve(input: List<String>, paramPredicate: (Int) -> Boolean): Int {
        val antennas = readAntennas(input)
        val lines = allAntennaLines(antennas, input[0].length, input.size)
        return lines.flatMap { line ->
            line.params.filter(paramPredicate).mapNotNull { line.getPoint(it) }
        }.distinct().count()
    }

    fun part1(input: List<String>) = solve(input) { it in listOf(-1, 2) }

    fun part2(input: List<String>) = solve(input) { true }

    val TEST = """
    ............
    ........0...
    .....0......
    .......0....
    ....0.......
    ......A.....
    ............
    ............
    ........A...
    .........A..
    ............
    ............
    """.trimIndent().lines()

    // Test if implementation meets criteria from the description, like:
    check(part1(TEST) == 14)

    val input = File("input/day08b.txt").readLines()
    part1(input).println()

    check(part2(TEST) == 34)
    part2(input).println()
}