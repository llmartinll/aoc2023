package solution

import Dir
import Pos
import getNext
import println
import readInput

fun main() {
    Day14_part2(readInput("day14/input")).run().println()
}

class Day14_part2(input: List<String>) {
    private enum class ROCK_TYPE(val char: Char) {
        ROUNDED('O'), CUBED('#');
        override fun toString(): String = "${this.char}"
    }

    private val maxLines = input.size
    private val lastLineIndex = input.lastIndex
    private val lastColumnIndex = input.first().lastIndex

    private val grid = input.flatMapIndexed { lineIndex, line -> line.mapIndexedNotNull { charIndex, char -> ROCK_TYPE.entries.firstOrNull { it.char == char }?.let { rockType -> Pos(lineIndex, charIndex) to rockType } } }
        .associate { it.first to it.second }
        .toMutableMap()

    fun run() {
        val set: MutableSet<Long> = mutableSetOf()
        (1..1200).forEach {
            cycle()
            val value = grid.filter { it.value == ROCK_TYPE.ROUNDED }.map { it.key.getScore() }.sumOf { it.toLong() }
            if (it > 150) {
                "cycle: ${it % 51}: $value}".println()
                set.add(value)
            }

        }
        "1000000000 mod 50: ${1000000000 % 51}".println()
    }

    private fun cycle() {
        moveAll(Dir.N)
        moveAll(Dir.W)
        moveAll(Dir.S)
        moveAll(Dir.E)
    }

    private fun moveAll(dir: Dir) {
        val toMove = grid.filter { it.value == ROCK_TYPE.ROUNDED }.keys
        val sortedSet =
            when (dir) {
                Dir.N -> toMove.sortedBy { it.line }
                Dir.E -> toMove.sortedByDescending { it.column }
                Dir.S -> toMove.sortedByDescending { it.line }
                Dir.W -> toMove.sortedBy { it.column }
            }
        sortedSet.forEach { pos ->
            grid.move(pos, dir)
        }
    }

    private fun MutableMap<Pos, ROCK_TYPE>.move(pos: Pos, dir: Dir) {
        var newPos = pos
        while (this.canMove(newPos, dir)) {
            newPos = newPos.getNext(dir)
        }
        this.remove(pos)
        this[newPos] = ROCK_TYPE.ROUNDED
    }

    private fun MutableMap<Pos, ROCK_TYPE>.canMove(pos: Pos, dir: Dir): Boolean {
        if (pos.isEdge(dir)) {
            return false
        }
        val nextPost = pos.getNext(dir)
        return this[nextPost] == null
    }

    private fun Pos.isEdge(dir: Dir) =
        when (dir) {
            Dir.N -> this.line == 0
            Dir.W -> this.column == 0
            Dir.E -> this.column == lastColumnIndex
            Dir.S -> this.line == lastLineIndex
        }

    private fun Pos.getScore(): Int {
        val score = maxLines - this.line
        return score
    }

    private fun MutableMap<Pos, ROCK_TYPE>.print() {
        (0..lastLineIndex).forEach { lineIndex ->
            var line = ""
            (0..lastColumnIndex).forEach { colIndex ->
                val value = grid.get(Pos(lineIndex, colIndex))?.toString() ?: "."
                line += value
            }
            println(line)
        }
        println("")
    }
}
