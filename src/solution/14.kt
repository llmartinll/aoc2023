package solution

import Pos
import println
import readInput

fun main() {
    Day14(readInput("day14/input")).run().println()
}

class Day14(input: List<String>) {
    private enum class ROCK_TYPE(val char: Char) {
        ROUNDED('O'), CUBED('#');

        override fun toString(): String = "${this.char}"
    }

    private val maxLines = input.size
    private val grid = input.flatMapIndexed { lineIndex, line -> line.mapIndexedNotNull { charIndex, char -> ROCK_TYPE.entries.firstOrNull { it.char == char }?.let { rockType -> Pos(lineIndex, charIndex) to rockType } } }
        .associate { it.first to it.second }
        .toMutableMap()

    fun run(): Int {
        var changed = true
        while (changed) {
            changed = false
            grid.filter { it.value == ROCK_TYPE.ROUNDED && it.key.canMoveNorth() }.entries.firstOrNull()?.let { first ->
                changed = true
                "moving $first to ${Pos(first.key.line - 1, first.key.column)}: ${first.value}".println()
                grid.remove(first.key)
                grid[Pos(first.key.line - 1, first.key.column)] = first.value
            }
        }
        return grid.filter { it.value == ROCK_TYPE.ROUNDED }.map { it.key.getScore() }.sum()
    }

    private fun Pos.canMoveNorth(): Boolean {
        if (this.line == 0) {
            return false
        }
        if (grid[Pos(this.line - 1, this.column)] != null) {
            return false
        }
        return true
    }

    private fun Pos.getScore() = maxLines - this.line
}
