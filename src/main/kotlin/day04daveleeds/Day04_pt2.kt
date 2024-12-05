package day04

import daveutils.solve
import day04.Direction.*

enum class Direction(val x: Int, val y: Int) {
    NORTH(0, -1), SOUTH(0, 1), EAST(1, 0), WEST(-1, 0),
    NORTHEAST(1, -1), NORTHWEST(-1, -1), SOUTHEAST(1, 1), SOUTHWEST(-1, 1);
}

operator fun List<String>.get(x: Int, y: Int): Char =
    if (y !in indices || x !in first().indices) '.' else this[y][x]

operator fun List<String>.get(x: Int, y: Int, direction: Direction, multiplier: Int = 1): Char =
    get(x + (direction.x * multiplier), y + (direction.y * multiplier))

fun List<String>.forEachChar(block: (x: Int, y: Int, char: Char) -> Unit) {
    indices.forEach { y -> this[y].indices.forEach { x -> block(x, y, this[y][x]) } }
}

fun main() {
    fun part1(grid: List<String>): Int {
        var count = 0
        grid.forEachChar { x, y, char ->
            if (char == 'X') {
                Direction.entries.map { dir ->
                    if (grid[x, y, dir] == 'M' && grid[x, y, dir, 2] == 'A' && grid[x, y, dir, 3] == 'S') count++
                }
            }
        }
        return count
    }

    fun part2(grid: List<String>): Int {
        val intercardinals = listOf(NORTHEAST, SOUTHWEST, SOUTHEAST, NORTHWEST)
        var count = 0

        grid.forEachChar { x, y, char ->
            if (char == 'A') {
                val corners = intercardinals.map { grid[x, y, it] }
                val oppositesDiffer = corners[0] != corners[1] && corners[2] != corners[3]
                val hasExpectedLetters = corners.count { it == 'M' } == 2 && corners.count { it == 'S' } == 2

                if (hasExpectedLetters && oppositesDiffer) count++
            }
        }

        return count
    }

    solve(::part1, withInput = "day04/test", andAssert = 18)
    solve(::part1, withInput = "day04/input", andAssert = 2560)

    solve(::part2, withInput = "day04/test", andAssert = 9)
    solve(::part2, withInput = "day04/input", andAssert = 1910)
}
