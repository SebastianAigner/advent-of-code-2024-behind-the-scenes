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
    val map = buildMapFromLines(lines)
    val potentialTrailheads = map.filterValues { it == 0 }
    val a = potentialTrailheads.toList().sumOf { (pth, _) ->
        countReachableSummitsForTrailhead(pth, map)
    }.also(::println)

    val b = potentialTrailheads.toList().sumOf { (pth, _) ->
        countDistinctPathsToSummitForTrailhead(pth, map)
    }.also(::println)
    check(a == 510)
    check(b == 1058)
}

private fun buildMapFromLines(lines: List<String>): Map<Vec2, Int> = buildMap<Vec2, Int> {
    for (y in lines.indices) {
        for (x in lines[0].indices) {
            val char = lines[y][x]
            put(Vec2(x, y), char.digitToInt())
        }
    }
}

fun countReachableSummitsForTrailhead(trailhead: Vec2, map: Map<Vec2, Int>): Int {
    require(map[trailhead] == 0)
    val reachableSummits = walkPathsToSummits(listOf(trailhead), map)
    check(reachableSummits.all { summit -> map.getCoord(summit.location) == 9 })
    return reachableSummits.toSet().size
}

fun countDistinctPathsToSummitForTrailhead(trailhead: Vec2, map: Map<Vec2, Int>): Int {
    require(map[trailhead] == 0)
    val paths: List<Path> = tracePathsToSummits(listOf(trailhead), map)
    check(paths.all { path -> map.getCoord(path.steps.last()) == 9 })
    return paths.toSet().size
}

@JvmInline
value class Path(val steps: List<Vec2>)

fun tracePathsToSummits(path: List<Vec2>, map: Map<Vec2, Int>): List<Path> {
    val curLoc = path.last()
    val curLevel = map.getCoord(curLoc)
    if (curLevel == 9) return listOf(Path(path))
    val nextLevel = curLevel + 1
    val walkablePaths = mutableListOf<Path>()
    for (neighbor in with(curLoc) { listOf(up(), down(), left(), right()) }) {
        if (map.getCoord(neighbor) == nextLevel) {
            val potentialTargets = tracePathsToSummits(path + neighbor, map)
            walkablePaths += potentialTargets
        }
    }
    return walkablePaths
}

fun Map<Vec2, Int>.getCoord(v: Vec2) = this[v] ?: Int.MAX_VALUE // a _very_ steep cliff.

@JvmInline
value class Summit(val location: Vec2)

fun walkPathsToSummits(path: List<Vec2>, map: Map<Vec2, Int>): List<Summit> {
    val curLoc = path.last()
    val curLevel = map.getCoord(curLoc)
    if (curLevel == 9) return listOf(Summit(curLoc))
    val nextLevel = curLevel + 1
    val reachableSummits = mutableListOf<Summit>()
    for (neighbor in with(curLoc) { listOf(up(), down(), left(), right()) }) {
        if (map.getCoord(neighbor) == nextLevel) {
            val foundSummits = walkPathsToSummits(path + neighbor, map)
            reachableSummits += foundSummits
        }
    }
    return reachableSummits
}
