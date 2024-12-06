package day06

import androidx.compose.animation.core.animate
import java.io.File

val input = File("input/day06.txt")

enum class GuardHeading(val step: Vec2) {
    NORTH(Vec2(0, -1)),
    EAST(Vec2(1, 0)),
    SOUTH(Vec2(0, 1)),
    WEST(Vec2(-1, 0));

    fun turnRight(): GuardHeading {
        return when(this) {
            GuardHeading.NORTH -> EAST
            GuardHeading.EAST -> SOUTH
            GuardHeading.SOUTH -> WEST
            GuardHeading.WEST -> NORTH
        }
    }
}

data class Vec2(val x: Int, val y: Int) {
    operator fun plus(other: Vec2) = Vec2(this.x + other.x, this.y + other.y)
}
data class GuardWalkRecord(val loc: Vec2, val heading: GuardHeading)
fun main() {
    val lines = input.readLines().map { string ->
        string.toList()
    }
    var guardPos = Vec2(-1, -1)
    var guardHeading = GuardHeading.NORTH
    val visited = mutableListOf<Vec2>()
    for (y in lines.indices) {
        for (x in lines[0].indices) {
            if (lines[y][x] == '^') {
                guardPos = Vec2(x, y)
                visited += Vec2(x, y)
            }
        }
    }
    check(guardPos.y != -1 && guardPos.x != -1)
    fun getTile(x: Int, y: Int): Char {
        return if(y !in lines.indices || x !in lines[0].indices) '?' else lines[y][x]
    }
    println(guardPos)
    while (guardPos.y in lines.indices && guardPos.x in lines[0].indices) {
        // take a step
        val attemptedNewPos = guardPos + guardHeading.step
        val targetTile = getTile(attemptedNewPos.x, attemptedNewPos.y)
        if (targetTile == '.' || targetTile == '^') {
            guardPos = attemptedNewPos
            println("took a step to $guardPos")
            visited += attemptedNewPos
        } else if(targetTile == '#') {
            println("hit a thing")
            guardHeading = guardHeading.turnRight()
        } else if(targetTile == '?') {
            println("we're out of bounds at $attemptedNewPos")
            break
        } else {
            println("i've never seen a $targetTile before")
        }
        //debugPrint(lines,visited)
    }
    println(visited.distinct().size)
    println("grid size ${lines.indices} x ${lines[0].indices}")
    //debugPrint(lines, visited)
}

private fun debugPrint(
    lines: List<List<Char>>,
    visited: MutableList<Vec2>,
) {
    println("--")
    for (y in lines.indices) {
        print("$y: ")
        for (x in lines[0].indices) {
            if (lines[y][x] == '#') {
                print("█")
            } else if (Vec2(x, y) in visited) {
                print("X")
            } else {
                print(".")
            }
        }
        println()
    }
    println("--")
}