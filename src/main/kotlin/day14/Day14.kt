package day14

import java.io.File

val input = File("input/day14.txt")
val exInput = """
    p=0,4 v=3,-3
    p=6,3 v=-1,-3
    p=10,3 v=-1,2
    p=2,0 v=2,-1
    p=0,0 v=1,3
    p=3,0 v=-2,-2
    p=7,6 v=-1,-3
    p=3,0 v=-1,-2
    p=9,3 v=2,3
    p=7,3 v=-1,2
    p=2,4 v=2,-3
    p=9,5 v=-3,-3
""".trimIndent().lines()

fun main() {
    val lines = input.readLines()
    check(part1(input = exInput, width = 11, height = 7) == 12)
    println(part1(lines, width = 101, height = 103))
    part2(lines, width = 101, height = 103)
}

data class Vec2(val x: Int, val y: Int) {
    fun mod(width: Int, height: Int): Vec2 {
        return Vec2(x.mod(width), y.mod(height))
    }

    operator fun plus(other: Vec2) = Vec2(this.x + other.x, this.y + other.y)
}

data class Robot(val pos: Vec2, val vel: Vec2)

fun part2(input: List<String>, width: Int, height: Int): Int {
    println(input)
    val start = input.map {
        val re = """p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""".toRegex()
        println(it)
        val (pX, pY, vX, vY) = re.find(it)!!.destructured
        Robot(Vec2(pX.toInt(), pY.toInt()), Vec2(vX.toInt(), vY.toInt()))
    }
    println(input)
    var roboLocs = start
    var iteration = 0
    while (true) {
        roboLocs = roboLocs.map {
            val newLoc = (it.pos + it.vel).mod(width, height)
            Robot(newLoc, it.vel)
        }
        val debugString = debugPrint(roboLocs, width, height)
        val isAnagrams = debugString.lines().count { it == it.reversed() }
        val isFramed = debugString.lines().any { it.contains("##########") }
        ++iteration
        println(iteration)
        if (isFramed) {
            println(debugString)
            println("above is ${iteration}")
            readln()
        }
    }
    return 0
}

fun debugPrint(r: List<Robot>, w: Int, h: Int): String {
    val debugString = buildString {
        for (y in 0..h - 1) {
            for (x in 0..w - 1) {
                val vec = Vec2(x, y)
                val char = if (r.any { it.pos == vec }) '#' else '.'
                append(char)
            }
            appendLine()
        }
    }
    //println(debugString)
    return debugString
}

fun part1(input: List<String>, width: Int, height: Int): Int {
    println(input)
    val start = input.map {
        val re = """p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""".toRegex()
        println(it)
        val (pX, pY, vX, vY) = re.find(it)!!.destructured
        Robot(Vec2(pX.toInt(), pY.toInt()), Vec2(vX.toInt(), vY.toInt()))
    }
    println(input)
    var roboLocs = start
    repeat(100) {
        roboLocs = roboLocs.map {
            val newLoc = (it.pos + it.vel).mod(width, height)
            Robot(newLoc, it.vel)
        }
    }
    val q1x = 0..width / 2 - 1
    val q1y = 0..height / 2 - 1

    val q2x = width / 2 + 1..width - 1
    val q2y = q1y

    val q3x = q1x
    val q3y = height / 2 + 1..height - 1

    val q4x = q2x
    val q4y = q3y

    val quadrants = listOf(Pair(q1x, q1y), Pair(q2x, q2y), Pair(q3x, q3y), Pair(q4x, q4y))

    val quadrantNums = quadrants.map { (xRange, yRange) ->
        var sum = 0
        for (x in xRange) {
            for (y in yRange) {
                sum += roboLocs.count { it.pos == Vec2(x, y) }
            }
        }
        sum
    }
    return quadrantNums.reduce { a, b ->
        a * b
    }
}