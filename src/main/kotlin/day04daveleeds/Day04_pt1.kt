package day04

import daveutils.solve
import lib.string.pivot
import lib.string.tippedLeft
import lib.string.tippedRight

@Suppress("UnnecessaryVariable", "LocalVariableName")
fun main() {
    fun part1(input: List<String>): Int {
        val leftToRight = input
        val rightToLeft = input.map { it.reversed() }
        val topToBottom = input.pivot()
        val bottomToTop = topToBottom.map { it.reversed() }

        val BLtoTR = input.tippedRight()
        val TRtoBL = BLtoTR.map { it.reversed() }
        val TLtoBR = input.tippedLeft()
        val BRtoTL = TLtoBR.map { it.reversed() }

        val all = leftToRight + rightToLeft + topToBottom + bottomToTop + BLtoTR + TRtoBL + TLtoBR + BRtoTL

        return all.sumOf { Regex("XMAS").findAll(it).count() }
    }

    solve(::part1, withInput = "day04/test", andAssert = 18)
    solve(::part1, withInput = "day04/input", andAssert = 2560)
}
