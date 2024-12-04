@file:Suppress("DuplicatedCode")

package day04rb

import java.io.File

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

    fun getAtPos(x: Int, y: Int): Char? = elems.getOrNull(y)?.getOrNull(x)

    fun countXmasWordsForPosition(startX: Int, startY: Int): Int {
        return allowedDirections.count { direction ->
            checkXmasWordForDirection(startX, startY, direction)
        }
    }

    private fun checkXmasWordForDirection(startX: Int, startY: Int, direction: Vec2): Boolean {
        var runningX = startX
        var runningY = startY

        for (letter in listOf('X', 'M', 'A', 'S')) {
            if (getAtPos(runningX, runningY) != letter) {
                return false
            }
            runningX += direction.x
            runningY += direction.y
        }
        return true
    }

    fun isMASCrossAtPosition(centerX: Int, centerY: Int): Boolean {
        if (getAtPos(centerX, centerY) != 'A') {
            return false // invalid cross-center
        }
        // we start at the 'A' of 'MAS', because it is the center.
        val isFallingDiagonalMAS =
            getAtPos(centerX - 1, centerY - 1) == 'M' && getAtPos(centerX + 1, centerY + 1) == 'S'
        val isFallingDiagonalSAM =
            getAtPos(centerX - 1, centerY - 1) == 'S' && getAtPos(centerX + 1, centerY + 1) == 'M'
        val fallingDiagonalOk = isFallingDiagonalMAS || isFallingDiagonalSAM

        val isRisingDiagonalMAS =
            getAtPos(centerX - 1, centerY + 1) == 'M' && getAtPos(centerX + 1, centerY - 1) == 'S'
        val isRisingDiagonalSAM =
            getAtPos(centerX - 1, centerY + 1) == 'S' && getAtPos(centerX + 1, centerY - 1) == 'M'
        val risingDiagonalOk = isRisingDiagonalMAS || isRisingDiagonalSAM

        return risingDiagonalOk && fallingDiagonalOk
    }
}

fun main() {
    val rawInput = File("input/day04.txt")
    val input = rawInput.readLines()
    val grid = Grid(input.map { it.toCharArray().toList() })
    part1(grid)
    part2(grid)
}

private fun part1(grid: Grid) {
    var xmasWordCount = 0
    for (startY in grid.elems[0].indices) {
        for (startX in grid.elems.indices) {
            xmasWordCount += grid.countXmasWordsForPosition(startX, startY)
        }
    }
    println(xmasWordCount)
}

private fun part2(grid: Grid) {
    var crossCount = 0
    for (centerY in grid.elems[0].indices) {
        for (centerX in grid.elems.indices) {
            crossCount += if (grid.isMASCrossAtPosition(centerX, centerY)) 1 else 0
        }
    }
    println(crossCount)
}
