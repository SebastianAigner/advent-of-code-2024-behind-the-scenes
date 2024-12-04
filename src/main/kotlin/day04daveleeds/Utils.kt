package daveutils

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("input/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun <T : Number> solve(puzzle: (List<String>) -> T, withInput: String, andAssert: T? = null) {
    val input = readInput(withInput).dropLastWhile { it.isEmpty() }
    val result = puzzle(input)
    println(result)
    andAssert?.let { require(result == it) }
}
