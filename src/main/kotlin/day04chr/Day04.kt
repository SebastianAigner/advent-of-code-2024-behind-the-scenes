@file:Suppress("DuplicatedCode")

package day04chr

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.io.File

data class Vec2(val x: Int, val y: Int)

data class ColorEvent(val loc: Vec2, val color: Color, val priority: Int)

val colorFlow = MutableSharedFlow<ColorEvent>()

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

    val indices = sequence {
        for (y in elems[0].indices) {
            for (x in elems.indices) {
                yield(Pair<Int, Int>(x, y))
            }
        }
    }


    suspend fun getAtPos(x: Int, y: Int, noDelay: Boolean = false): Char? {
        val elem = elems.getOrNull(y)?.getOrNull(x)
        if (elem != null) {
            colorFlow.emit(ColorEvent(Vec2(x, y), Color(0xffe0e0e0), 99))
            if (!noDelay) {
                delay(100)
            }
        }
        return elem
    }

    suspend fun countXmasWordsForPosition(startX: Int, startY: Int): Int {
        return allowedDirections.count { direction ->
            if (getAtPos(startX, startY, true) != 'X') {
                delay(50)
                return@count false
            }
            checkXmasWordForDirection(startX, startY, direction)
        }
    }

    private suspend fun checkXmasWordForDirection(startX: Int, startY: Int, direction: Vec2): Boolean {
        var runningX = startX
        var runningY = startY
        if (getAtPos(runningX, runningY, true) != 'X') {
            return false
        }

        for (letter in listOf('X', 'M', 'A', 'S')) {
            if (getAtPos(runningX, runningY) != letter) {
                return false
            }
            colorFlow.emit(ColorEvent(Vec2(runningX, runningY), Color(0xFFc4ffff), 50))
            runningX += direction.x
            runningY += direction.y
        }
        println("FOUND ONE at $startX $startY")

        runningX = startX
        runningY = startY

        repeat(4) {
            colorFlow.emit(ColorEvent(Vec2(runningX, runningY), Color.Cyan, 10))
            delay(25)
            runningX += direction.x
            runningY += direction.y
        }
        return true
    }

    suspend fun isMASCrossAtPosition(centerX: Int, centerY: Int): Boolean {
        if (getAtPos(centerX, centerY) != 'A') {
            return false // invalid cross-center
        }
        // we start at the 'A' of 'MAS', because it is the center.
        val fdm = if (getAtPos(centerX - 1, centerY - 1) == 'M') {
            colorFlow.emit(ColorEvent(Vec2(centerX - 1, centerY - 1), almostStarColor, 80))
            true
        } else {
            false
        }
        val fds = if (getAtPos(centerX + 1, centerY + 1) == 'S') {
            colorFlow.emit(ColorEvent(Vec2(centerX + 1, centerY + 1), almostStarColor, 80))
            true
        } else {
            false
        }
        val isFallingDiagonalMAS =
            fdm && fds
        val isFallingDiagonalSAM =
            if (getAtPos(centerX - 1, centerY - 1) == 'S') {
                colorFlow.emit(ColorEvent(Vec2(centerX - 1, centerY - 1), almostStarColor, 80))
                true
            } else {
                false
            }
                    &&
                    if (getAtPos(centerX + 1, centerY + 1) == 'M') {
                        colorFlow.emit(ColorEvent(Vec2(centerX + 1, centerY + 1), almostStarColor, 80))
                        true
                    } else {
                        false
                    }
        val fallingDiagonalOk = isFallingDiagonalMAS || isFallingDiagonalSAM

        val isRM = if (getAtPos(centerX - 1, centerY + 1) == 'M') {
            colorFlow.emit(ColorEvent(Vec2(centerX - 1, centerY + 1), almostStarColor, 80))
            true
        } else {
            false
        }
        val isRS = if (getAtPos(centerX + 1, centerY - 1) == 'S') {
            colorFlow.emit(ColorEvent(Vec2(centerX + 1, centerY - 1), almostStarColor, 80))
            true
        } else {
            false
        }
        val isRisingDiagonalMAS = isRM && isRS
        val isRisingDiagonalSAM =
            if (getAtPos(centerX - 1, centerY + 1) == 'S') {
                colorFlow.emit(ColorEvent(Vec2(centerX - 1, centerY + 1), almostStarColor, 80))
                true
            } else {
                false
            }
                    &&
                    if (getAtPos(centerX + 1, centerY - 1) == 'M') {
                        colorFlow.emit(ColorEvent(Vec2(centerX + 1, centerY - 1), almostStarColor, 80))
                        true
                    } else {
                        false
                    }
        val risingDiagonalOk = isRisingDiagonalMAS || isRisingDiagonalSAM
        val totallyOk = fallingDiagonalOk && risingDiagonalOk
        if (totallyOk) {
            colorFlow.emit(ColorEvent(Vec2(centerX, centerY), starCenterColor, 1))
            colorFlow.emit(ColorEvent(Vec2(centerX - 1, centerY + 1), starArmColor, 1))
            colorFlow.emit(ColorEvent(Vec2(centerX - 1, centerY - 1), starArmColor, 1))
            colorFlow.emit(ColorEvent(Vec2(centerX + 1, centerY + 1), starArmColor, 1))
            colorFlow.emit(ColorEvent(Vec2(centerX + 1, centerY - 1), starArmColor, 1))
        }
        return totallyOk
    }
}

val starCenterColor = Color(0xFF00c0ff)
val starArmColor = Color(0xff0086b3)
val almostStarColor = Color(0xFF004d67)


suspend fun main() {
    val rawInput = File("input/day04.txt")
    val input = rawInput.readLines()
    val grid = Grid(input.map { it.toCharArray().toList() })
    part1(grid)
    part2(grid)
}

private suspend fun part1(grid: Grid) {
    var xmasWordCount = 0
    for ((startX, startY) in grid.indices) {
        xmasWordCount += grid.countXmasWordsForPosition(startX, startY)
    }
    println(xmasWordCount)
}

private suspend fun part2(grid: Grid) {
    var crossCount = 0
    for ((centerX, centerY) in grid.indices) {
        crossCount += if (grid.isMASCrossAtPosition(centerX, centerY)) 1 else 0
    }
    println(crossCount)
}
