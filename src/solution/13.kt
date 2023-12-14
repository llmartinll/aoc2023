package solution

import println
import readInput

fun main() {
    Day13(readInput("day13/input")).run().println()
}

class Day13(input: List<String>) {
    data class Pattern(val lines: List<List<Char>>) {
        fun print() {
            lines.forEach { println(it) }
        }
    }

    data class Field(val patterns: List<Pattern>) {
        fun print() {
            patterns.forEach {
                it.print()
                println("")
            }
        }
    }

    private val field: Field

    fun run(): Long {
        return field.patterns.map(::getPoints).sum().toLong()
    }

    private fun getPoints(pattern: Pattern): Int {
        val horizontalReflectionPoint = getHorizontalReflectionPoint(pattern)
        return if (horizontalReflectionPoint != -1) {
            100 * horizontalReflectionPoint
        } else getHorizontalReflectionPoint(pattern.transpose())
    }

    private fun getHorizontalReflectionPoint(pattern: Pattern): Int {
        val lines = pattern.lines
        var reversed: List<List<Char>> = listOf()
        (0..lines.size - 2).forEach {
            reversed = listOf(lines[it]) + reversed
            if (areEqual(reversed, lines.subList(it + 1, lines.lastIndex + 1))) {
                return reversed.size
            }
        }
        return -1
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
