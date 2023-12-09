package solution

import println
import readInput

fun main() {
    Day9(readInput("Day09/input")).run().println()
}

class Day9(input: List<String>) {
    private val histories = input.map { line -> line.split(" ").map { it.toInt() } }

    private fun getDifferences(list: List<Int>): List<Int> =
        list.mapIndexedNotNull { index, _ -> if (index < list.lastIndex) list[index + 1] - list[index] else null }

    private fun getPrediction(list: List<Int>): Int =
        list.last() + if (list.all { it == 0 }) 0 else getPrediction(getDifferences(list))

    fun run() =
        histories.map(::getPrediction).sum()
}
