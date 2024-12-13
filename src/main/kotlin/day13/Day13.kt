package day13

import java.io.File

val input = File("input/day13.txt")

data class Vec2(val x: Int, val y: Int) {
    operator fun minus(other: Vec2) = Vec2(this.x - other.x, this.y - other.y)
    operator fun plus(other: Vec2) = Vec2(this.x + other.x, this.y + other.y)
    operator fun times(scalar: Int) = Vec2(this.x * scalar, this.y * scalar)
}

data class ClawMachine(val a: Vec2, val b: Vec2, val prize: Vec2) {
    val aPressCost = 3
    val bPressCost = 1
}

fun ClawMachine(str: String): ClawMachine {
    val nums = """X\+(\d+), Y\+(\d+)""".toRegex()
    val prz = """X=(\d+), Y=(\d+)""".toRegex()
    val l = str.lines()
    val aStr = l[0]
    val bStr = l[1]
    val pStr = l[2]

    val (aX, aY) = nums.find(aStr)!!.destructured
    val (bX, bY) = nums.find(bStr)!!.destructured
    val (pX, pY) = prz.find(pStr)!!.destructured
    return ClawMachine(Vec2(aX.toInt(), aY.toInt()), Vec2(bX.toInt(), bY.toInt()), Vec2(pX.toInt(), pY.toInt()))
}

fun main() {
    val clawMachines = input.readText().split("\n\n").map { ClawMachine(it.trim()) }
    println(clawMachines)
    clawMachines.sumOf { minimumSpend(it) ?: 0 }.also(::println)
}

fun minimumSpend(c: ClawMachine): Int? {
    val paths = sequence {
        for (a in 0..100) {
            for (b in 0..100) {
                val endPoint = (c.a * a) + (c.b * b)
                if (endPoint == c.prize) {
                    yield(a * c.aPressCost + b * c.bPressCost)
                }
            }
        }
    }
    return paths.minOfOrNull { it }
}