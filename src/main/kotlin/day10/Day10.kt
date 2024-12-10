package day10

import java.io.File

val input = File("input/day10b.txt")

data class Vec2(val x: Int, val y: Int) {
    fun up() = Vec2(x, y - 1)
    fun down() = Vec2(x, y + 1)
    fun left() = Vec2(x - 1, y)
    fun right() = Vec2(x + 1, y)
}

private fun buildMapFromLines(lines: List<String>): Map<Vec2, Int> = buildMap<Vec2, Int> {
    for (y in lines.indices) {
        for (x in lines[0].indices) {
            put(Vec2(x, y), lines[y][x].digitToInt())
        }
    }
}

fun main() {
    val lines = input.readLines()
    val map = buildMapFromLines(lines)
    val potentialTrailheads = map.filterValues { it == 0 }.toList()
    val part1Result = potentialTrailheads.sumOf { (trailhead, _) ->
        countReachableSummitsForTrailhead(trailhead, map)
    }.also(::println)

    val part2Result = potentialTrailheads.sumOf { (trailhead, _) ->
        countDistinctPathsToSummitForTrailhead(trailhead, map)
    }.also(::println)
    check(part1Result == 510)
    check(part2Result == 1058)
}

fun Map<Vec2, Int>.getCoordinate(v: Vec2) = this[v] ?: Int.MAX_VALUE // a _very_ steep cliff.

fun countReachableSummitsForTrailhead(trailhead: Vec2, map: Map<Vec2, Int>): Int {
    require(map[trailhead] == 0)
    val reachableSummits = walkPathsToSummits(listOf(trailhead), map)
    check(reachableSummits.all { summit -> map.getCoordinate(summit.location) == 9 })
    return reachableSummits.toSet().size
}

@JvmInline
value class Summit(val location: Vec2)

fun walkPathsToSummits(path: List<Vec2>, map: Map<Vec2, Int>): List<Summit> {
    val curLoc = path.last()
    val curLevel = map.getCoordinate(curLoc)
    if (curLevel == 9) return listOf(Summit(curLoc))
    val nextLevel = curLevel + 1
    val reachableSummits = with(curLoc) {
        listOf(up(), down(), left(), right())
    }.filter {
        map.getCoordinate(it) == nextLevel
    }.flatMap {
        walkPathsToSummits(path + it, map)
    }
    return reachableSummits
}


fun countDistinctPathsToSummitForTrailhead(trailhead: Vec2, map: Map<Vec2, Int>): Int {
    require(map[trailhead] == 0)
    val paths: List<Path> = tracePathsToSummits(listOf(trailhead), map)
    for (x in 0 until 10) {
        println(x)
    }
    check(paths.all { path -> map.getCoordinate(path.steps.last()) == 9 })
    return paths.toSet().size
}

@JvmInline
value class Path(val steps: List<Vec2>)

fun tracePathsToSummits(path: List<Vec2>, map: Map<Vec2, Int>): List<Path> {
    val curLoc = path.last()
    val curLevel = map.getCoordinate(curLoc)
    if (curLevel == 9) return listOf(Path(path))
    val nextLevel = curLevel + 1
    val walkablePaths = with(curLoc) {
        listOf(up(), down(), left(), right())
    }.filter {
        map.getCoordinate(it) == nextLevel
    }.flatMap {
        tracePathsToSummits(path + it, map)
    }
    return walkablePaths
}
