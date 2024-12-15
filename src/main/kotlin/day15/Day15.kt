package day15

import java.io.File

val input = File("input/day15.txt")

fun main() {
    val lines = input.readText()
    check(part1(part1Test) == 10092)
    println("P1: ${part1(lines)}")
}

data class Vec2(val x: Int, val y: Int) {
    operator fun plus(other: Vec2) = Vec2(this.x + other.x, this.y + other.y)

    fun up() = Vec2(x, y - 1)
    fun down() = Vec2(x, y + 1)
    fun left() = Vec2(x - 1, y)
    fun right() = Vec2(x + 1, y)
}

sealed class LayoutItem(var pos: Vec2)

class Robot(pos: Vec2) : LayoutItem(pos)
class Box(pos: Vec2) : LayoutItem(pos)
class Wall(pos: Vec2) : LayoutItem(pos)


enum class Heading(val step: Vec2) {
    NORTH(Vec2(0, -1)),
    EAST(Vec2(1, 0)),
    SOUTH(Vec2(0, 1)),
    WEST(Vec2(-1, 0));
}


fun part1(txt: String): Int {
    val (mapstr, seqlines) = txt.split("\n\n")
    val map = mapstr.lines()
    val items = buildList {
        for (y in map.indices) {
            for (x in map[0].indices) {
                val char = map[y][x]
                when (char) {
                    '#' -> add(Wall(Vec2(x, y)))
                    'O' -> add(Box(Vec2(x, y)))
                    '@' -> add(Robot(Vec2(x, y)))
                }
            }
        }
    }
    check(items.count { it is Robot } == 1)
    val seqstr = seqlines.filter { it != '\n' }.trim()
    val seq = seqstr.map {
        when (it) {
            '^' -> Heading.NORTH
            '>' -> Heading.EAST
            'v' -> Heading.SOUTH
            '<' -> Heading.WEST
            else -> error("huh")
        }
    }
    simulate(items, seq)
    val sum = items.filterIsInstance<Box>().sumOf {
        it.pos.y * 100 + it.pos.x
    }
    return sum
}

fun List<LayoutItem>.getAt(vec2: Vec2) = this.find { it.pos == vec2 }

fun simulate(items: List<LayoutItem>, steps: List<Heading>) {
    val robo = items.filterIsInstance<Robot>().single()
    for (step in steps) {
        val targetField = robo.pos + step.step
        val item = items.getAt(targetField)
        if (item == null) {
            // empty space, we can just move.
            robo.pos = targetField
            continue
        }
        if (item is Wall) {
            // can't do anything.
            continue
        }
        if (item is Box) {
            // see if we can move ALL boxes that may be affected by this step.
            val currBoxLoc = item.pos
            attemptMove(item, items, step)
            if (item.pos == currBoxLoc) {
                // the attempt failed. we're pushing into a wall.
                continue
            }
            robo.pos = targetField // we move in the freshly emptied space
        }
    }
}

fun attemptMove(box: Box, items: List<LayoutItem>, step: Heading) {
    // we're now the box.
    val targetField = box.pos + step.step
    val item = items.getAt(targetField)
    if (item == null) {
        // empty space, we can just move.
        box.pos = targetField
        return
    }
    if (item is Wall) {
        // can't do anything.
        return
    }
    if (item is Box) {
        val currBoxLoc = item.pos
        attemptMove(item, items, step)
        if (item.pos == currBoxLoc) {
            // we didn't move
            return
        } else {
            // we moved! let's move into the free space
            box.pos = targetField
        }
    }
}


val part1Test = """
    ##########
    #..O..O.O#
    #......O.#
    #.OO..O.O#
    #..O@..O.#
    #O#..O...#
    #O..O..O.#
    #.OO.O.OO#
    #....O...#
    ##########

    <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
    vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
    ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
    <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
    ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
    ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
    >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
    <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
    ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
    v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
""".trimIndent()