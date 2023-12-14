package solution

import Pos
import println
import readInput

fun main() {
    Day11(readInput("day11/input")).run().println()
}

class Day11(input: List<String>) {

    enum class Data(val char: Char) {
        EMPTY('.'), STAR('#');

        override fun toString(): String = "${this.char}"
    }

    private val grid: Map<Pos, Data> = input.flatMapIndexed { lineIndex, line -> line.mapIndexed { charIndex, char -> Pos(lineIndex, charIndex) to Data.entries.first { it.char == char } } }.associate { it.first to it.second }
    private val rowsWithoutGalaxies = input.mapIndexed { lineIndex, line -> lineIndex to line }.associate { it.first to it.second }.filter { it.value.all { it == '.' } }.keys
    private val columnsWithoutGalaxies = (0..grid.entries.last().key.column).associateWith { columnIndex -> grid.filter { it.key.column == columnIndex }.map { it.value } }.filter { it.value.all { it == Data.EMPTY } }.map { it.key }
    private val galaxies = grid.filter { it.value == Data.STAR }.map { it.key }.sortedBy { it.row }
    private var pairs: Set<Pair<Pos, Pos>> = (0..galaxies.lastIndex).flatMap { index1 ->
        (0..galaxies.lastIndex).mapNotNull { index2 ->
            if (index1 > index2) {
                Pair(galaxies[index1], galaxies[index2])
            } else null
        }
    }.toSet()

    fun run(): Long {
        return pairs.map(::calculateShortestPath).sum()
    }

    private fun calculateShortestPath(pair: Pair<Pos, Pos>) = calculateShortestPath(pair.first, pair.second)
    private fun calculateShortestPath(from: Pos, to: Pos): Long {
        val rowsTraversed = (0..maxOf(from.row, to.row)) - (0..minOf(from.row, from.row))
        val columnsTraversed = (0..maxOf(from.column, to.column)) - (0..minOf(from.column, to.column))
        return rowsTraversed.map(::getRowValue).sum() + columnsTraversed.map(::getColumnValue).sum()
    }

    private fun getColumnValue(column: Int): Int =
        if (columnsWithoutGalaxies.contains(column)) 1_000_000 else 1

    private fun getRowValue(row: Int): Long =
        if (rowsWithoutGalaxies.contains(row)) 1_000_000 else 1
}
