package solution

import println
import readInput

fun main() {
    Day12(readInput("day12/input")).run().println()
}

class Day12(input: List<String>) {
    private val records: List<Pair<String, List<Int>>> = input.map { line -> Pair(line.substringBefore(' ').trim(), line.substringAfter(' ').split(',').map { it.toInt() }) }

    fun run() =
        records.map(::calculatePossibilities).sum()

    private fun calculatePossibilities(pair: Pair<String, List<Int>>) = calculatePossibilities(pair.first, pair.second)
    private fun calculatePossibilities(str: String, records: List<Int>): Int {
        if (getUnknownPositions(str).isEmpty()) {
            return if (isValid(str, records)) 1 else 0
        }
        return calculatePossibilities(str.replaceFirst('?', '#'), records) +
            calculatePossibilities(str.replaceFirst('?', '.'), records)
    }

    private fun getUnknownPositions(str: String) =
        str.mapIndexedNotNull { index, c -> if (c == '?') index else null }

    private fun isValid(str: String, records: List<Int>) =
        toRecords(str) == records

    private fun toRecords(str: String): List<Int> =
        str.split(".").map { it.length }.filter { it != 0 }
}
