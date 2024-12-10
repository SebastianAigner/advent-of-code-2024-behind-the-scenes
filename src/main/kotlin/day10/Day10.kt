package day10

import java.io.File

val input = File("input/day10b.txt")

data class Vec2(val x: Int, val y: Int) {
    fun up() = Vec2(x, y - 1)
    fun down() = Vec2(x, y + 1)
    fun left() = Vec2(x - 1, y)
    fun right() = Vec2(x + 1, y)
}

fun main() {
    val lines = input.readLines()
    val map = mutableMapOf<Vec2, Int>()
    for (y in lines.indices) {
        for (x in lines[0].indices) {
            val char = lines[y][x]
            map[Vec2(x, y)] = char.digitToInt()
        }
    }
    val potentialTrailheads = map.filterValues { i -> i == 0 }
    potentialTrailheads.toList().sumOf { (pth, _) ->
        calculateTrailheadScore(pth, map)
    }.also(::println)
}

fun calculateTrailheadScore(entry: Vec2, map: Map<Vec2, Int>): Int {
    require(map[entry] == 0)
    val reaches = walkUpwards(listOf(entry), map)
    println(reaches)
    check(reaches.all { vec2 -> map.getCoord(vec2) == 9 })
    return reaches.toSet().size
}

fun Map<Vec2, Int>.getCoord(v: Vec2) = this[v] ?: Int.MAX_VALUE // a _very_ steep cliff.

fun walkUpwards(path: List<Vec2>, map: Map<Vec2, Int>): List<Vec2> {
    val curLoc = path.last()
    val curLevel = map.getCoord(curLoc)
    if (curLevel > 9) return emptyList()
    if (curLevel == 9) return listOf(curLoc)
    val nextLevel = curLevel + 1
    val res = mutableListOf<Vec2>()
    for (neighbor in with(curLoc) { listOf(up(), down(), left(), right()) }) {
        if (map.getCoord(neighbor) == nextLevel) {
            val potentialTargets = walkUpwards(path + neighbor, map)
            res += potentialTargets
        }
    }
    return res
}
