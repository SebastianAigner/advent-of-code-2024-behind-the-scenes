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

data class GuardWalkRecord(val loc: Vec2, val heading: GuardHeading) {
    fun step(): GuardWalkRecord {
        return GuardWalkRecord(loc + heading.step, heading)
    }

    fun turnRight(): GuardWalkRecord {
        return GuardWalkRecord(loc, heading.turnRight())
    }
}

fun main() {
    val lines = input.readLines().map { string ->
        string.toList()
    }
    part1(lines)
    println("grid size ${lines.indices} x ${lines[0].indices}")
    //debugPrint(lines, visited)
    part2(lines)
}

private fun part1(lines: List<List<Char>>) {
    val visited = simulateGuardPath(lines)

    //p1
    println(visited.onSuccess { value -> value.distinctBy { record -> record.loc }.size })
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
    val tile = lines[obsY][obsX]
    if (tile == '^' || tile == '#') {
        // this is either not allowed or wouldn't modify, so we don't count it as a failure.
        return Result.success(emptyList())
    }
    val workingCopy = lines.map { chars -> chars.toMutableList() }.toMutableList()
    workingCopy[obsY][obsX] = '#' // no need for 'O'.
    return simulateGuardPath(workingCopy)
}



fun getGuardStartingPosition(lines: List<List<Char>>): Vec2 {
    for (y in lines.indices) {
        for (x in lines[0].indices) {
            if (lines[y][x] == '^') {
                return Vec2(x, y)
            }
        }
    }
    error("No guard starting position found.")
}

private fun simulateGuardPath(lines: List<List<Char>>): Result<List<GuardWalkRecord>> {
    var currentGuardRecord = GuardWalkRecord(getGuardStartingPosition(lines), GuardHeading.NORTH)
    val visited = mutableListOf<GuardWalkRecord>(currentGuardRecord)
    fun getTile(x: Int, y: Int): Char {
        return if (y !in lines.indices || x !in lines[0].indices) '?' else lines[y][x]
    }
    while (currentGuardRecord.loc.y in lines.indices && currentGuardRecord.loc.x in lines[0].indices) {
        // take a step
        val attemptedNewPos = currentGuardRecord.step()
        val targetTile = getTile(attemptedNewPos.loc.x, attemptedNewPos.loc.y)
        when (targetTile) {
            '.', '^' -> {
                currentGuardRecord = attemptedNewPos
                if (currentGuardRecord in visited) {
                    println("this feels familiar.")
                    return Result.failure(IllegalPathStateException())
                }
                visited += currentGuardRecord
            }
            '#' -> {
                currentGuardRecord = currentGuardRecord.turnRight()
            }
            '?' -> {
                break
            }
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