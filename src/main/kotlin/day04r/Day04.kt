@file:Suppress("DuplicatedCode")

package day04r

import java.io.File

val input = File("input/day04.txt")

data class Vec2(val x: Int, val y: Int)
data class Grid(val elems: List<List<Char>>) {
    val allowedDirections = listOf(
        Vec2(1, 0), // east
        Vec2(1, 1), // southeast
        Vec2(0, 1), // south
        Vec2(-1, 1), // southwest
        Vec2(-1, 0), // west
        Vec2(-1, -1), // northwest
        Vec2(0, -1), // north
        Vec2(1, -1), // northeast
    )

    fun countXmasWordsForPosition(startX: Int, startY: Int): Int {
        if (elems[startY][startX] != 'X') {
            return 0 // invalid word start
        }
        return allowedDirections.count { direction ->
            checkXmasWordForDirection(startX, startY, direction)
        }
    }

    fun getAtPos(x: Int, y: Int): Char? {
        return this.elems.getOrNull(y)?.getOrNull(x)
    }

    private fun checkXmasWordForDirection(startX: Int, startY: Int, direction: Vec2): Boolean {
        var runningX = startX
        var runningY = startY
        for (letter in listOf('X', 'M', 'A', 'S')) {
            if (runningY !in 0..elems.lastIndex || runningX !in 0..elems[0].lastIndex) {
                return false // out of bounds
            }
            if (elems[runningY][runningX] == letter) {
                runningX += direction.x
                runningY += direction.y
            } else {
                return false
            }
        }
        return true
    }

    fun isMASCrossAtPosition(centerX: Int, centerY: Int): Boolean {
        if (elems[centerY][centerX] != 'A') {
            return false // invalid cross-center
        }
        // we start at the 'A' of 'MAS', because it is the center.
        try {
            val fallingDiagonalOk =
                elems[centerY - 1][centerX - 1] == 'M' && elems[centerY + 1][centerX + 1] == 'S'
                        || elems[centerY - 1][centerX - 1] == 'S' && elems[centerY + 1][centerX + 1] == 'M'

            val risingDiagonalOk = // copied, careful
                elems[centerY + 1][centerX - 1] == 'M' && elems[centerY - 1][centerX + 1] == 'S'
                        || elems[centerY + 1][centerX - 1] == 'S' && elems[centerY - 1][centerX + 1] == 'M'
            return risingDiagonalOk && fallingDiagonalOk
        } catch (_: IndexOutOfBoundsException) {
            return false
        }
    }
}

fun main() {
    val input = input.readLines()
    val grid = Grid(input.map { it.toCharArray().toList() })
    part1(grid)
    part2(grid)
}

private fun part2(grid: Grid) {
    var masSum = 0
    for (startY in 0..grid.elems[0].lastIndex) {
        for (startX in 0..grid.elems.lastIndex) {
            masSum += if (grid.isMASCrossAtPosition(startX, startY)) 1 else 0
        }
    }
    println(masSum)
}

private fun part1(grid: Grid) {
    var sum = 0
    for (startY in 0..grid.elems[0].lastIndex) {
        for (startX in 0..grid.elems.lastIndex) {
            sum += grid.countXmasWordsForPosition(startX, startY)
        }
    }
    println(sum)
}