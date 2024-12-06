package day06marton.smart

import java.io.File

data class Position(val x: Int, val y: Int)

enum class Direction(val xDelta: Int, val yDelta: Int) {
    Up(0, -1),
    Right(1, 0),
    Down(0, 1),
    Left(-1, 0),
    ;

    companion object {
        fun initial(): Direction = Up
    }

    fun turnRight(): Direction {
        return entries[(entries.indexOf(this) + 1) % entries.size]
    }
}

operator fun Position.plus(direction: Direction) =
    Position(x + direction.xDelta, y + direction.yDelta)

inline fun runGuard(
    width: Int,
    height: Int,
    obstacles: Set<Position>,
    initialPos: Position,
    initialDir: Direction = Direction.initial(),
    onNewPositionVisited: (Position, Direction) -> Unit,
) {
    var pos = initialPos
    var dir = initialDir

    while (pos.x in 0..width && pos.y in 0..height) {
        onNewPositionVisited(pos, dir)

        val next = pos + dir
        if (next in obstacles) {
            dir = dir.turnRight()
        } else {
            pos = next
        }
    }
}

fun part1(
    width: Int,
    height: Int,
    obstacles: Set<Position>,
    initialPos: Position,
): Int {
    val visited = mutableSetOf(initialPos)
    runGuard(
        width = width,
        height = height,
        obstacles = obstacles,
        initialPos = initialPos,
        onNewPositionVisited = { pos, dir -> visited += pos },
    )
    return visited.size
}

data class State(val position: Position, val direction: Direction)

fun part2(
    width: Int,
    height: Int,
    obstacles: Set<Position>,
    initialPos: Position,
): Int {
    var looped = 0
//    for (testX in 0..<width) {
//        for (testY in 0..<height) {
            val visited = mutableSetOf<State>()
            runGuard(
                width = width,
                height = height,
                obstacles = obstacles,
                initialPos = initialPos,
                onNewPositionVisited = { pos, dir ->
                    
                    val state = State(pos, dir)
                    if (state in visited) {
                        looped++
//                        continue
                    }
                    visited += state
                },
            )
//        }
//    }
    return looped
}

fun main() {
    val input = File("input/day06.txt").readLines()

    val obstacles = mutableSetOf<Position>()
    lateinit var initialPos: Position

    input.forEachIndexed { y, s ->
        s.forEachIndexed { x, c ->
            when (c) {
                '#' -> obstacles.add(Position(x, y))
                '^' -> initialPos = Position(x, y)
            }
        }
    }

    val height = input.size
    val width = input[0].length

    val result1 = part1(
        width,
        height,
        obstacles,
        initialPos,
    )

    check(result1 == 5534)
    println(result1) // 5534

    val result2 = part2(
        width,
        height,
        obstacles,
        initialPos,
    )
    check(result2 == 2262)
    println(result2) // 2262
}
