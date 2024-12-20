package day12

import java.io.File
import kotlin.math.abs

val input = File("input/day12.txt")

val exA = """
    AAAA
    BBCD
    BBCC
    EEEC
""".trimIndent().lines()

val exB = """
    OOOOO
    OXOXO
    OOOOO
    OXOXO
    OOOOO
""".trimIndent().lines()

val exC = """
    RRRRIICCFF
    RRRRIICCCF
    VVRRRCCFFF
    VVRCCCJFFF
    VVVVCJJCFE
    VVIVCCJJEE
    VVIIICJJEE
    MIIIIIJJEE
    MIIISIJEEE
    MMMISSJEEE
""".trimIndent().lines()

val exD = """
    EEEEE
    EXXXX
    EEEEE
    EXXXX
    EEEEE
""".trimIndent().lines()

val exE = """
    AAAAAA
    AAABBA
    AAABBA
    ABBAAA
    ABBAAA
    AAAAAA
""".trimIndent().lines()

fun countContiguousGroups(list: List<Vec2>): Int {
    if (list.isEmpty()) return 0
    val allGroups = mutableListOf<List<Vec2>>()
    var currGroup = mutableListOf<Vec2>()
    for (elem in list) {
        if (currGroup.isEmpty() || currGroup.last().dist(elem) <= 1) {
            currGroup += elem
        } else {
            allGroups.add(currGroup)
            currGroup = mutableListOf<Vec2>()
            currGroup.add(elem)
        }
    }
    if (currGroup.isNotEmpty()) {
        allGroups.add(currGroup)
    }
    return allGroups.size
}

fun main() {
    val lines = input.readLines()
    check(countContiguousGroups(listOf(Vec2(0, 0), Vec2(1, 0), Vec2(3, 0))).also { i -> println("groups is $i") } == 2)
//    println(lines)
//    check(part1(exA).also { lng -> println("exA $lng") } == 140)
//    check(part1(exB).also { lng -> println("exB $lng") } == 772)
//    check(part1(exC).also { lng -> println("exC $lng") } == 1930)
//    println("PART 1: ${part1(lines)}")
    check(part2(exA).also { lng -> println("exA $lng") } == 80)
    check(part2(exB).also { lng -> println("exB $lng") } == 436)
    check(part2(exD).also { lng -> println("exC $lng") } == 236)
    check(part2(exE).also { lng -> println("exD $lng") } == 368)
    check(part2(exC).also { lng -> println("exE $lng") } == 1206)
    println("PART 2: ${part2(lines)}")
}

data class Vec2(val x: Int, val y: Int) {
    fun up() = Vec2(x, y - 1)
    fun down() = Vec2(x, y + 1)
    fun left() = Vec2(x - 1, y)
    fun right() = Vec2(x + 1, y)
    operator fun plus(other: Vec2) = Vec2(x + other.x, y + other.y)
    fun dist(other: Vec2): Int = abs(x - other.x) + abs(y - other.y)
}

enum class Heading(val step: Vec2) {
    NORTH(Vec2(0, -1)),
    EAST(Vec2(1, 0)),
    SOUTH(Vec2(0, 1)),
    WEST(Vec2(-1, 0));

    fun rotateRight(): Heading {
        return when (this) {
            NORTH -> EAST
            EAST -> SOUTH
            SOUTH -> WEST
            WEST -> NORTH
        }
    }

    fun rotateLeft() = this.rotateRight().rotateRight().rotateRight()
}

fun part2(lines: List<String>): Int {
    println(lines.joinToString("\n"))
    fun getPlantAt(v: Vec2) = if (v.x in lines[0].indices && v.y in lines.indices) lines[v.y][v.x] else '?'
    val allCoordinates = buildList {
        for (y in lines.indices) {
            for (x in lines[0].indices) {
                add(Vec2(x, y))
            }
        }
    }
    val untouchedLocations = ArrayList<Vec2>(allCoordinates)
    val groups = buildList {
        while (untouchedLocations.isNotEmpty()) {
            val group = findGroupForLocation(untouchedLocations.first(), ::getPlantAt)
            untouchedLocations.removeAll(group)
            add(group)
            println("found group $group")
        }
    }
    val areas = groups.map { vec2s -> vec2s.size }.also(::println)
    val sides = groups.map { vec2s -> countSidesOfPlot(vec2s, ::getPlantAt) }.also(::println)

    val prices = areas.zip(sides, transform = { area, sides ->
        println("$area x $sides")
        area * sides
    })
    return prices.sum()
}

fun countSidesOfPlot(points: List<Vec2>, at: (Vec2) -> Char): Int {
    val plantType = at(points.first())
    println("======")
    println("Working on type $plantType ($points)")
    val xRange = (points.minOf { vec2 -> vec2.x }) - 1..(points.maxOf { it.x }) + 1
    val yRange = (points.minOf { vec2 -> vec2.y }) - 1..(points.maxOf { it.y }) + 1
    val tophorEdgeVecs = buildList {
        for (y in yRange) {
            add(buildList {
                for (x in xRange) {
                    val loc = Vec2(x, y)
                    if (at(loc) != plantType && loc.down() in points) {
                        // top edge
                        add(loc)
                    }
                }
            })
        }
    }.filter { edgevec -> edgevec.isNotEmpty() }
    println("Horizontal Edge Squares Top: $tophorEdgeVecs")
    val botHorEdgeVecs = buildList {
        for (y in yRange) {
            add(buildList {
                for (x in xRange) {
                    val loc = Vec2(x, y)
                    if (at(loc) != plantType && loc.up() in points) {
                        //bottom edge
                        add(loc)
                    }
                }
            })
        }
    }.filter { edgevec -> edgevec.isNotEmpty() }
    println("Horizontal Edge Squares Bottom: $botHorEdgeVecs")
    val leftverEdgeVecs = buildList {
        for (x in xRange) {
            add(buildList {
                for (y in yRange) {
                    val loc = Vec2(x, y)
                    if (at(loc) != plantType && loc.right() in points) {
                        add(loc)
                    }
                }
            })
        }
    }.filter { edgevec -> edgevec.isNotEmpty() }
    println("Vertical Edge Squares Left: $leftverEdgeVecs")

    val rightverEdgeVecs = buildList {
        for (x in xRange) {
            add(buildList {
                for (y in yRange) {
                    val loc = Vec2(x, y)
                    if (at(loc) != plantType && loc.left() in points) {
                        add(loc)
                    }
                }
            })
        }
    }.filter { edgevec -> edgevec.isNotEmpty() }
    println("Vertical Edge Squares Right: $rightverEdgeVecs")

    val horizontalTopSegments = tophorEdgeVecs.sumOf { vecLine ->
        countContiguousGroups(vecLine)
    }
    val horizontalBotSegments = botHorEdgeVecs.sumOf { vecLine ->
        countContiguousGroups(vecLine)
    }

    val verticalLeftSegements = leftverEdgeVecs.sumOf { vecColumn ->
        countContiguousGroups(vecColumn)
    }

    val verticalRightSegements = rightverEdgeVecs.sumOf { vecColumn ->
        countContiguousGroups(vecColumn)
    }

    return horizontalTopSegments + horizontalBotSegments + verticalRightSegements + verticalLeftSegements
}

// NOTE: there can be more than one plot per type of plant!
fun part1(lines: List<String>): Int {
    println(lines.joinToString("\n"))
    fun getPlantAt(v: Vec2) = if (v.x in lines[0].indices && v.y in lines.indices) lines[v.y][v.x] else '?'
    val allCoordinates = buildList {
        for (y in lines.indices) {
            for (x in lines[0].indices) {
                add(Vec2(x, y))
            }
        }
    }
    val untouchedLocations = ArrayList<Vec2>(allCoordinates)
    val groups = buildList {
        while (untouchedLocations.isNotEmpty()) {
            val group = findGroupForLocation(untouchedLocations.first(), ::getPlantAt)
            untouchedLocations.removeAll(group)
            add(group)
            println("found group $group")
        }
    }
    val areas = groups.map { vec2s -> vec2s.size }.also(::println)
    val perimeters = groups.map { vec2s -> calculatePerimeter(vec2s, ::getPlantAt) }.also(::println)
    groups.map { vec2s ->
        println(getPlantAt(vec2s.first()) + " " + vec2s.size + " " + calculatePerimeter(vec2s, ::getPlantAt))
    }
    val prices = areas.zip(perimeters, transform = { area, perimeter ->
        println("$area x $perimeter")
        area * perimeter
    })
    return prices.sum()
}

fun findGroupForLocation(vec2: Vec2, at: (Vec2) -> Char): List<Vec2> {
    // flood fill?
    val plantType = at(vec2)
    return buildList {
        val locationsToCheck = ArrayList<Vec2>(listOf(vec2))
        add(vec2)
        while (locationsToCheck.isNotEmpty()) {
            val currentLocation = locationsToCheck.removeFirst()
            val neighbors = with(currentLocation) { listOf(up(), down(), left(), right()) }
            val neighborTypes = neighbors.map { vec2 -> at(vec2) }
            val validNeighbors = neighbors.filter { vec2 -> at(vec2) == plantType }
            val newNeighbors = validNeighbors.filter { vec2 -> vec2 !in this }
            locationsToCheck.addAll(newNeighbors)
            this.addAll(newNeighbors)
        }
    }
}


fun calculatePerimeter(vec2s: List<Vec2>, at: (Vec2) -> Char): Int {
    val plantType = at(vec2s.first())
    println("perimeter for type $plantType")
    val plotAdjacentLocations = buildList<Vec2> { // this is NOT a set!
        for (loc in vec2s) {
            val neighbors = with(loc) { listOf(up(), down(), left(), right()) }
            val adjacentNeighbors =
                neighbors.filter { vec2 -> at(vec2) != plantType /*&& at(vec2) != '?'*/ } // important NOT to exlcude '?"
            addAll(adjacentNeighbors)
        }
    }
    println(plotAdjacentLocations)
    return plotAdjacentLocations.size
}