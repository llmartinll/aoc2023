package solution

import println
import readInput

fun main() {
    Day13_part2(readInput("day13/test")).run().println()
}

class Day13_part2(input: List<String>) {
    data class Pattern(val lines: List<List<Char>>) {
        fun print() {
            lines.forEach {
                it.forEach { print(it) }
                println("")
            }
            println("")
        }
    }

    data class Field(val patterns: List<Pattern>)

    private val field: Field

    fun run(): Long {
        return field.patterns.map(::getPoints).map {
            println("points: $it")
            it
        }.sum().toLong()
    }

    private fun getPoints(pattern: Pattern): Int {
        val horizontalReflectionPoint = getReflectionLines(pattern)
        val verticalReflectionPoint = getReflectionLines(pattern.transpose())

        pattern.lines.forEachIndexed { lineIndex, line ->
            line.forEachIndexed { charIndex, char ->
                val newLine = pattern.lines[lineIndex].toMutableList()
                newLine[charIndex] = if (newLine[charIndex] == '#') '.' else '#'
                val lines = pattern.lines.toMutableList()
                lines[lineIndex] = newLine
                val newPattern = Pattern(lines)

                val newHorizontalReflectionPoint = getReflectionLines(newPattern)

                if (newHorizontalReflectionPoint != null && newHorizontalReflectionPoint != horizontalReflectionPoint) {
//                    "old horrizontal: $horizontalReflectionPoint new: $newHorizontalReflectionPoint".println()
                    return 100 * newHorizontalReflectionPoint
                }
                val newVerticalRflectionPoint = getReflectionLines(newPattern.transpose())
//                "transposed".println()
                if (newVerticalRflectionPoint != null && newVerticalRflectionPoint != verticalReflectionPoint) {
//                    "old vertical pattern: $verticalReflectionPoint new: $newVerticalRflectionPoint".println()
                    return newVerticalRflectionPoint
                }
            }
        }
        println("unchanged!")
        pattern.print()
        if (horizontalReflectionPoint != null) {
            return horizontalReflectionPoint
        }
        if (verticalReflectionPoint != null) {
            return verticalReflectionPoint
        }
        throw IllegalStateException("bla")
    }

    private fun getReflectionLines(pattern: Pattern): List<Int> {
        val reflectionLines: MutableList<Int> = mutableListOf()
        val lines = pattern.lines
        var reversed: List<List<Char>> = listOf()
        (0..lines.size - 2).forEach {
            reversed = listOf(lines[it]) + reversed
            if (areEqual(reversed, lines.subList(it + 1, lines.lastIndex + 1))) {
                reflectionLines.add(reversed.size)
            }
        }
        return reflectionLines
    }

    private fun areEqual(first: List<List<Char>>, second: List<List<Char>>): Boolean {
        val toCheck = minOf(first.size, second.size)
        return first.subList(0, toCheck) == second.subList(0, toCheck)
    }

    private fun Pattern.transpose(): Pattern {
        val transposed: MutableMap<Int, List<Char>> = mutableMapOf()
        this.lines.forEachIndexed { _, line ->
            line.forEachIndexed { charIndex, c ->
                transposed[charIndex] = transposed[charIndex]?.let { listOf(c) + it } ?: listOf(c)
            }
        }
        return Pattern(transposed.values.toList())
    }

    init {
        val patterns: MutableList<Pattern> = mutableListOf()
        var lines: MutableList<List<Char>> = mutableListOf()

        input.forEach { line ->
            if (line.isBlank()) {
                patterns.add(Pattern(lines))
                lines = mutableListOf()
                return@forEach
            }
            lines += line.map { it }
        }
        patterns.add(Pattern(lines))
        field = Field(patterns.toList())
    }
}
