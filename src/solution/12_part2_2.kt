package solution

import arrow.core.memoize
import println
import readInput
import kotlin.system.measureTimeMillis

fun main() {
    val day12 = Day12_part2(readInput("day12/input"))
    val elapsed = measureTimeMillis {
        day12.run().println()
    }
    "elapsed: $elapsed ms".println()
}

class Day12_part2(input: List<String>) {
    private val records: List<Pair> = input.map { line -> Pair(line.substringBefore(' ').trim(), line.substringAfter(' ').split(',').map { it.toInt() }) }
    private val unfolded = records.map(::unfold).sortedBy { it.second.size }
    private val memoizedCalculation = ::calculatePossibilities.memoize()

    data class Pair(val first: String, val second: List<Int>)

    fun run(): Long {
        var totals = 0L
        var linesDone = 0
        unfolded.forEach {
            println("starting $it")
            totals += memoizedCalculation(it)
            linesDone++
            "at $linesDone/${unfolded.size}".println()
        }
        return unfolded.map { memoizedCalculation(it) }.sum()
    }

    private fun calculatePossibilities(pair: Pair): Long {
        val string = pair.first
        val records = pair.second
        if (string.count { it == '#' || it == '?' } < records.sum()) {
            return 0
        }

        if (string.first() == '.') {
            return memoizedCalculation(Pair(pair.first.substring(1), pair.second))
        }
        if (isValid(string, records)) {
            return 1
        }

        if (string.startsWith('?')) {
            val possibilitiesIfDot = memoizedCalculation(Pair('.' + string.substring(1), records))
            val possibilitiesIfSpring = memoizedCalculation(Pair('#' + string.substring(1), records))
            return possibilitiesIfDot + possibilitiesIfSpring
        }
        if (string.startsWith('#')) {
            try {
                if (isValid(string, records)) {
                    return 1
                }
                val firstpart = string.substring(0, records.first())
                if (firstpart.any { it == '.' }) {
                    return 0
                }
                if (string.get(records.first()) == '#') {
                    return 0
                }

                val leftOverString = string.substring(records.first() + 1)
                val leftOverSublist = records.subList(1, records.lastIndex + 1)
                if (isValid(leftOverString, leftOverSublist)) {
                    return 1
                }
                return memoizedCalculation(Pair(leftOverString, leftOverSublist))
            } catch (e: Exception) {
                return 0
            }
        }
        return 1
    }

    private fun isValid(str: String, records: List<Int>) =
        records.isEmpty() && str.count { it == '#' } == 0 || toRecords(str) == records

    private fun toRecords(str: String): List<Int> =
        str.split(".").map { it.length }.filter { it != 0 }

    private fun unfold(pair: Pair): Pair {
        val newString = pair.first + '?' + pair.first + '?' + pair.first + '?' + pair.first + '?' + pair.first
        val newRecord = pair.second + pair.second + pair.second + pair.second + pair.second
        return Pair(newString, newRecord)
    }
}
