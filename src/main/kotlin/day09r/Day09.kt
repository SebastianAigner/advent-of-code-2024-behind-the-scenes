@file:Suppress("DuplicatedCode")

package day09r

import java.io.File

val input = File("input/day09b.txt")

data class Segment(val len: Int, val id: Int) // -1 == empty

fun main() {
    val line = input.readLines().first()
    println(line)
    val list = mutableListOf<Segment>()
    var cnt = 0
    for ((idx, char) in line.withIndex()) {
        val digit = char.digitToInt()
        val id = if (idx % 2 == 1) -1 else (cnt++)
        list += Segment(digit, id)
    }
    val a = part1(list)
    val b = part2(list)
    check(a == 6421128769094L)
    check(b == 6448168620520L)
    println(a)
    println(b)
}

private fun part1(list: List<Segment>): Long {
    val materialized = mutableListOf<Int>()
    for (segment in list) {
        repeat(segment.len) {
            materialized.add(segment.id)
        }
    }
    println(materialized)
    while (true) {
        val firstOpenSpace = materialized.indexOfFirst { i -> i == -1 }
        val lastUsedSpace = materialized.indexOfLast { i -> i != -1 }
        if (firstOpenSpace >= lastUsedSpace) {
            break
        }
        materialized[firstOpenSpace] = materialized[lastUsedSpace]
        materialized[lastUsedSpace] = -1
    }
    println(materialized)
    var checkSum = 0L
    for ((idx, block) in materialized.withIndex()) {
        if (block == -1) continue
        checkSum += idx * block
        println(checkSum)
    }
    return checkSum
}

class BlockStorage() {
    val storage = mutableListOf<Segment>()
    fun addSegment(segment: Segment) {
        storage += segment
    }

    fun firstGapOfMinimumSize(n: Int): Int {
        return storage.indexOfFirst { segment -> segment.id == -1 && segment.len >= n }
    }

    fun moveSegmentIntoGap(segmentIdx: Int, gapIdx: Int) {
        val gap = storage[gapIdx]
        val segment = storage[segmentIdx]
        val newGapLength = gap.len - segment.len
        val newGapSegment = Segment(newGapLength, -1)
        storage[segmentIdx] = Segment(segment.len, -1)
        storage[gapIdx] = segment
        storage.add(gapIdx + 1, newGapSegment)
    }

    fun materialize(): List<Int> {
        val materialized = mutableListOf<Int>()
        for (segment in storage) {
            repeat(segment.len) {
                materialized.add(segment.id)
            }
        }
        return materialized
    }

    fun checksum(): Long {
        var checkSum = 0L
        for ((idx, block) in materialize().withIndex()) {
            if (block == -1) continue
            checkSum += idx * block
            println(checkSum)
        }
        return checkSum
    }
}

private fun part2(list: List<Segment>): Long {
    val blockStorage = BlockStorage()
    for (segment in list) {
        blockStorage.addSegment(segment)
    }
    while (true) {
        var didMoveSuccessfully = false
        for ((idx, segment) in blockStorage.storage.withIndex().reversed()) {
            if (segment.id == -1) continue // data segments only
            // find a location to see if it's possible to move
            val potentialTarget = blockStorage.firstGapOfMinimumSize(segment.len)
            if (potentialTarget == -1) continue // can't move this data segment
            if (potentialTarget >= idx) continue // could only move it further to the end
            blockStorage.moveSegmentIntoGap(idx, potentialTarget) // this should always succeed
            didMoveSuccessfully = true
            println("successfully moved $segment from $idx to $potentialTarget")
            break
        }
        if (!didMoveSuccessfully) break
    }


    val materialized = mutableListOf<Int>()
    for (segment in blockStorage.storage) {
        repeat(segment.len) {
            materialized.add(segment.id)
        }
    }
    println(materialized)
    return blockStorage.checksum()
}

// idea: instead of moving the last element to the first matching gap, find the first non-zero gap, walk backwards,
// and see what file from the back you can move in there