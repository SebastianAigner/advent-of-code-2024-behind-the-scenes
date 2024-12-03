import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("input/$name.txt").readText().trim().lines()

fun readInputAsString(name: String) = Path("input/$name.txt").readText().trim()

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

/**
 * Remove an element at the given index from a list
 */
fun <T> List<T>.withoutIndex(index: Int): List<T> = this.toMutableList().apply { removeAt(index) }
