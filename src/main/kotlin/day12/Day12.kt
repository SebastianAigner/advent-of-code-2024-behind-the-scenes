package day12

import java.io.File

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

fun main() {
    val lines = input.readLines()
//    println(lines)
    check(part1(exA).also { lng -> println("exA $lng") } == 140)
    check(part1(exB).also { lng -> println("exB $lng") } == 772)
    check(part1(exC).also { lng -> println("exC $lng") } == 1930)
    println("PART 1: ${part1(lines)}")
}

data class Vec2(val x: Int, val y: Int) {
    fun up() = Vec2(x, y - 1)
    fun down() = Vec2(x, y + 1)
    fun left() = Vec2(x - 1, y)
    fun right() = Vec2(x + 1, y)
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