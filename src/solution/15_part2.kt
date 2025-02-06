package solution

import println
import kotlin.io.path.Path
import kotlin.io.path.readText

fun main() {
    Day15_part2(Path("src/input/day15/input.txt").readText()).run().println()
}

class Day15_part2(input: String) {
    private val str = input
    private val steps = str.filterNot { it.isWhitespace() }.split(',')

    fun run(): Int {
        return steps.map(::runHash).sum()
    }

    private fun runHash(str: String): Int {
        var curVal = 0
        str.forEach { char ->
            curVal += char.hashCode()
            curVal *= 17
            curVal %= 256
        }
        println("$str becomes $curVal")
        return curVal
    }
}
