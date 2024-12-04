package day04

import java.io.File

val input = File("input/day04.txt")

/*

M.S
.A.
M.S

S.S
.A.
M.M

M M
 A
S S

M S
 A
M S


 */

data class Vec2(val x: Int, val y: Int)
data class Grid(val elems: List<List<Char>>) {
    val height = elems.size
    val width = elems[0].size

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

    fun countXmasForPosition(startX: Int, startY: Int): Int {
        if (elems[startY][startX] != 'X') {
            return 0 // invalid word start
        }
        println("counting for $startX,$startY")
        var sum = 0
        for (direction in allowedDirections) {
            if (checkXmasWordForDirection(startX, startY, direction)) {
                sum++
            }
        }
        return sum
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

    fun countMASCrossForPosition(centerX: Int, centerY: Int): Int {
        if (elems[centerY][centerX] != 'A') {
            return 0 // invalid cross-center
        }
        // we start at the 'A' of 'MAS', because it is the center.
        try {
            val fallingDiagonalOk =
                elems[centerY - 1][centerX - 1] == 'M' && elems[centerY + 1][centerX + 1] == 'S'
                        || elems[centerY - 1][centerX - 1] == 'S' && elems[centerY + 1][centerX + 1] == 'M'

            val risingDiagonalOk = // copied, careful
                elems[centerY + 1][centerX - 1] == 'M' && elems[centerY - 1][centerX + 1] == 'S'
                        || elems[centerY + 1][centerX - 1] == 'S' && elems[centerY - 1][centerX + 1] == 'M'
            if(risingDiagonalOk && fallingDiagonalOk) {
                return 1
            } else {
                return 0
            }
        } catch(idx: IndexOutOfBoundsException) {
            return 0
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
            masSum += grid.countMASCrossForPosition(startX, startY)
        }
    }
    println(masSum)
}

private fun part1(grid: Grid) {
    var sum = 0
    for (startY in 0..grid.elems[0].lastIndex) {
        for (startX in 0..grid.elems.lastIndex) {
            val foundAt = grid.countXmasForPosition(startX, startY)
            sum += foundAt
        }
    }

    println(grid.elems[0])
    println(sum)
}


//fun countXmas(grid: Grid): Int {
//    var cnt = 0
//    for (basePattern in basePatterns) {
//        for (startX in 0..<grid.width) {
//            for (startY in 0..<grid.height) {
//                if (grid.containsAt(basePattern, startX, startY)) {
//                    println("found at $startX $startY!")
//                    println(basePattern)
//                    cnt++
//                }
//            }
//        }
//    }
//
//    return cnt
//}

//val basePatterns = listOf(
//    """
//        XMAS
//    """.trimIndent(),
//    """
//        SAMX
//    """.trimIndent(),
//    """
//        X
//        M
//        A
//        S
//    """.trimIndent(),
//    """
//        S
//        A
//        M
//        X
//    """.trimIndent(),
//    """
//        X
//         M
//          A
//           S
//    """.trimIndent(),
//    """
//        S
//         A
//          M
//           X
//    """.trimIndent(),
//    """
//           X
//          M
//         A
//        S
//    """.trimIndent(),
//    """
//           S
//          A
//         M
//        X
//    """.trimIndent(),
//)