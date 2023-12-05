package solution

import Point
import println
import readInput

fun main() {
    val day = "day03"
    val test = readInput("$day/test")
    val input = readInput("$day/input")

    Day3(test).part1().println()
    Day3(input).part1().println()
    Day3(test).part2().println()
    Day3(input).part2().println()
}

class Day3(input: List<String>) {
    private var symbols: MutableSet<Point> = mutableSetOf()
    private var gears: MutableSet<Point> = mutableSetOf()
    private val numbers: MutableMap<Pair<Point, Point>, Int> = mutableMapOf() // entry: (start (x,y), end (x,y)), value: (e.g. 123)

    init {
        createMap(input)
    }

    fun part1() =
        numbers.entries.filter(::isAdjacentToSymbol).sumOf { it.value }

    fun part2() =
        gears.map(::getGearRatio).sum()

    private fun createMap(input: List<String>) {
        input.forEachIndexed { row, string ->
            var numberStart = 0
            var curNumberString = ""
            var previousWasNumber = false

            string.forEachIndexed { index, char ->
                if (char.isDigit()) {
                    if (index == (string.length - 1)) {
                        // last index
                        if (previousWasNumber) {
                            numbers[Pair(Point(row, numberStart), Point(row, index))] = (curNumberString + char).toInt()
                        } else {
                            numbers[Pair(Point(row, index), Point(row, index))] = char.digitToInt()
                        }
                    } else {
                        if (previousWasNumber) {
                            // consecutive number in range
                            curNumberString += char
                        } else {
                            // first number in range
                            previousWasNumber = true
                            numberStart = index
                            curNumberString = "$char"
                        }
                    }
                } else {
                    // not a number
                    if (previousWasNumber) {
                        // end of number
                        numbers[Pair(Point(row, numberStart), Point(row, index - 1))] = curNumberString.toInt()
                        previousWasNumber = false
                    }
                    if (char != '.') {
                        // is symbol
                        symbols.add(Point(row, index))
                        if (char == '*') {
                            gears.add(Point(row, index))
                        }
                    }
                }
            }
        }
    }

    private fun isAdjacentToSymbol(entry: MutableMap.MutableEntry<Pair<Point, Point>, Int>): Boolean {
        val xRangeToCheck = (entry.key.first.x - 1)..(entry.key.first.x + 1)
        val yRangeToCheck = (entry.key.first.y - 1)..(entry.key.second.y + 1)
        return symbols.any { it.x in xRangeToCheck && it.y in yRangeToCheck }
    }

    private fun getGearRatio(point: Point): Int {
        val adjacentNumbers = numbers.filter {
            val xRangeToCheck = (it.key.first.x - 1)..(it.key.first.x + 1)
            val yRangeToCheck = (it.key.first.y - 1)..(it.key.second.y + 1)
            point.x in xRangeToCheck && point.y in yRangeToCheck
        }
        return if (adjacentNumbers.size >= 2) {
            adjacentNumbers.map { it.value }.reduce { acc, i -> acc * i }
        } else {
            0
        }
    }

}
