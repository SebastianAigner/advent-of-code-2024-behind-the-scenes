package day06marton

import java.io.File

fun main() {
    val input = File("input/day06.txt").readLines()

    val obstacles = mutableSetOf<Pair<Int, Int>>()
    lateinit var initialPos: Pair<Int, Int>

    val height = input.size
    val width = input[0].length

    val dirs = listOf(
        0 to -1,
        1 to 0,
        0 to 1,
        -1 to 0,
    )
    var dir = dirs[0]

    input.forEachIndexed { y, s ->
        s.forEachIndexed { x, c ->
            if (c == '#') {
                obstacles.add(x to y)
            }
            if (c == '^') {
                initialPos = x to y
            }
        }
    }

    var pos = initialPos

    fun turnRight() {
        val i = (dirs.indexOf(dir) + 1) % dirs.size
        dir = dirs[i]
    }

    fun nextPos() =
        (pos.first + dir.first) to (pos.second + dir.second)

    fun stepForward() {
        pos = nextPos()
    }


    val visited = mutableSetOf(pos)
    while (pos.first in 0..width && pos.second in 0..height) {
        visited += pos

        val next = nextPos()
        if (next in obstacles) {
            turnRight()
        } else {
            stepForward()
        }
    }

    println(visited.size) // 5534

    var looped = 0
    for (testX in 0..<width) {
        for (testY in 0..<height) {
            val obstacles = obstacles + (testX to testY)
            var pos = initialPos
            var dir = dirs[0]

            fun turnRight() {
                val i = (dirs.indexOf(dir) + 1) % dirs.size
                dir = dirs[i]
            }

            fun nextPos() =
                (pos.first + dir.first) to (pos.second + dir.second)

            fun stepForward() {
                pos = nextPos()
            }

            val visited = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
            while (pos.first in 0..width && pos.second in 0..height) {
                if (pos to dir in visited) {
                    looped++
                    break
                }

                visited += (pos to dir)

                val next = nextPos()
                if (next in obstacles) {
                    turnRight()
                } else {
                    stepForward()
                }
            }

        }
    }
    println(looped) // 2262
}
