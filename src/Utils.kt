import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/input/$name.txt").readLines()

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

data class Point(val x: Int, val y: Int) {
    override fun toString() = "($x,$y)"
}

fun LongRange.overLapWith(other: LongRange): LongRange =
    kotlin.math.max(this.first, other.first)..kotlin.math.min(this.last, other.last)

fun LongRange.shift(shift: Long) = this.first + shift..this.last + shift


data class Pos(val row: Int, val column: Int) {
    override fun toString() = "($row,$column)"
}

enum class Dir { N, E, S, W }

fun Pos.move(dir: Dir) = when (dir) {
    Dir.N -> Pos(this.row - 1, this.column)
    Dir.E -> Pos(this.row, this.column + 1)
    Dir.S -> Pos(this.row + 1, this.column)
    Dir.W -> Pos(this.row, this.column - 1)
}

fun <T> Pos.existsIn(grid: Map<Pos, T>) = this.row >= 0 && this.row <= grid.entries.last().key.row && this.column >= 0 && this.column <= grid.entries.last().key.column
fun <T> Map<Pos, T>.lastRow() = this.entries.last().key.row
fun <T> Map<Pos, T>.lastColumn() = this.entries.last().key.column

fun <T> Map<Pos, T>.print() {
    (0..this.lastRow()).forEach { row ->
        (0..this.lastColumn()).forEach { column ->
            print(this.get(Pos(row, column)))
        }
        println("")
    }
}
