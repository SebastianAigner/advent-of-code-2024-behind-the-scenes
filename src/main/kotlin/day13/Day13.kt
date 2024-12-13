package day13

import com.microsoft.z3.BoolExpr
import java.io.File

val input = File("input/day13.txt")

data class Vec2(val x: Long, val y: Long) {
    operator fun minus(other: Vec2) = Vec2(this.x - other.x, this.y - other.y)
    operator fun plus(other: Vec2) = Vec2(this.x + other.x, this.y + other.y)
    operator fun times(scalar: Long) = Vec2(this.x * scalar, this.y * scalar)
}

data class ClawMachine(val a: Vec2, val b: Vec2, val prize: Vec2) {
    val aPressCost = 3
    val bPressCost = 1
    val offset = 10000000000000
    val prizeExpensive = Vec2(prize.x + offset, prize.y + offset)
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
    return ClawMachine(Vec2(aX.toLong(), aY.toLong()), Vec2(bX.toLong(), bY.toLong()), Vec2(pX.toLong(), pY.toLong()))
}

fun main() {
    val clawMachines = input.readText().split("\n\n").map { ClawMachine(it.trim()) }
    println(clawMachines)
    clawMachines.sumOf { minimumSpend(it) ?: 0 }.also(::println)
    clawMachines.sumOf { minimumExpensiveSpend(it) ?: 0 }.also(::println)
}

fun minimumExpensiveSpend(c: ClawMachine): Int? {
    val ctx = com.microsoft.z3.Context(emptyMap())
    val opt = ctx.mkOptimize()
    val a = ctx.mkIntConst("a")
    val b = ctx.mkIntConst("b")

    // Eq(a * 66 + b * 21, 10000000012176)
    fun foo(): BoolExpr {
        val aPrice = ctx.mkInt(c.a.x)
        val bPrice = ctx.mkInt(c.b.x)
        val total = ctx.mkInt(c.prizeExpensive.x)
        val am = ctx.mkMul(a, aPrice)
        val bm = ctx.mkMul(b, bPrice)
        val comp = ctx.mkAdd(am, bm)
        val eq = ctx.mkEq(comp, total)
        return eq
    }

    fun bar(): BoolExpr {
        val aPrice = ctx.mkInt(c.a.y)
        val bPrice = ctx.mkInt(c.b.y)
        val total = ctx.mkInt(c.prizeExpensive.y)
        val am = ctx.mkMul(a, aPrice)
        val bm = ctx.mkMul(b, bPrice)
        val comp = ctx.mkAdd(am, bm)
        val eq = ctx.mkEq(comp, total)
        return eq
    }


    opt.Add(foo())
    opt.Add(bar())
    val res = opt.MkMinimize(a)
    val mod = opt.model
    println(mod.evaluate(a, true))
    println(mod.evaluate(b, true))
    TODO()
}

fun minimumSpend(c: ClawMachine): Int? {
    val paths = sequence {
        for (a in 0..100) {
            for (b in 0..100) {
                val endPoint = (c.a * a.toLong()) + (c.b * b.toLong())
                if (endPoint == c.prize) {
                    yield(a * c.aPressCost + b * c.bPressCost)
                }
            }
        }
    }
    return paths.minOfOrNull { it }
}