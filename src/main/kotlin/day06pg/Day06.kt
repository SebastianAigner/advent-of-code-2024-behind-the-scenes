@file:Suppress("DuplicatedCode")

package day06pg

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.awt.geom.IllegalPathStateException
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

val input = File("input/day06.txt")

enum class GuardHeading(val step: Vec2) {
    NORTH(Vec2(0, -1)),
    EAST(Vec2(1, 0)),
    SOUTH(Vec2(0, 1)),
    WEST(Vec2(-1, 0));

    fun turnRight(): GuardHeading {
        return when (this) {
            NORTH -> EAST
            EAST -> SOUTH
            SOUTH -> WEST
            WEST -> NORTH
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
    part1(lines)
    println("grid size ${lines.indices} x ${lines[0].indices}")
    //debugPrint(lines, visited)
    part2(lines)
}

private fun part2(lines: List<List<Char>>) {
    var cnt = AtomicInteger(0)
    runBlocking(Dispatchers.Default) {
        for (obsY in lines.indices) {
            for (obsX in lines[0].indices) {
                launch {
                    val res = simulatePlacedObstruction(lines, obsX, obsY)
                    if (res.isFailure) {
                        cnt.incrementAndGet()
                    }
                }
            }
        }
    }
    println(cnt)
}

fun simulatePlacedObstruction(lines: List<List<Char>>, obsX: Int, obsY: Int): Result<List<GuardWalkRecord>> {
    // place the obstruction
    val tile = lines[obsY][obsX]
    if (tile == '^' || tile == '#') {
        return Result.success(emptyList())
    }
    val workingCopy = lines.map { chars -> chars.toMutableList() }.toMutableList()
    workingCopy[obsY][obsX] = '#' // no need for 'O'.
    val res = simulateGuardPath(workingCopy)
    return res
}

private fun part1(lines: List<List<Char>>) {
    val visited = simulateGuardPath(lines)

    //p1
    println(
        visited
            .onSuccess { value ->
                value.distinctBy { record -> record.loc }.size
            })
}

private fun simulateGuardPath(lines: List<List<Char>>): Result<List<GuardWalkRecord>> {
    var guardPos = Vec2(-1, -1)
    var guardHeading = GuardHeading.NORTH
    val visited = mutableListOf<GuardWalkRecord>()
    for (y in lines.indices) {
        for (x in lines[0].indices) {
            if (lines[y][x] == '^') {
                guardPos = Vec2(x, y)
                visited += GuardWalkRecord(guardPos, guardHeading)
            }
        }
    }
    check(guardPos.y != -1 && guardPos.x != -1)
    fun getTile(x: Int, y: Int): Char {
        return if (y !in lines.indices || x !in lines[0].indices) '?' else lines[y][x]
    }
    while (guardPos.y in lines.indices && guardPos.x in lines[0].indices) {
        // take a step
        val attemptedNewPos = guardPos + guardHeading.step
        val targetTile = getTile(attemptedNewPos.x, attemptedNewPos.y)
        if (targetTile == '.' || targetTile == '^') {
            guardPos = attemptedNewPos
            //println("took a step to $guardPos")
            val record = GuardWalkRecord(attemptedNewPos, guardHeading)
            if (record in visited) {
               println("this feels familiar.")
                return Result.failure(IllegalPathStateException())
            }
            visited += record
        } else if (targetTile == '#') {
           // println("hit a thing")
            guardHeading = guardHeading.turnRight()
        } else if (targetTile == '?') {
           // println("we're out of bounds at $attemptedNewPos")
            break
        } else {
            //println("i've never seen a $targetTile before")
        }
        //debugPrint(lines,visited)
    }
    return Result.success(visited)
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