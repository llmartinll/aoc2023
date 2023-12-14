package solution

import println
import readInput

fun main() {
    Day13v2(readInput("day13/input")).run().println()
}

class Day13v2(input: List<String>) {
    data class Pattern(val lines: List<List<Char>>)
    data class Field(val patterns: List<Pattern>)
    data class FieldAbstraction(val patterns: List<PatternAbstraction>)

    data class PatternAbstraction(val linesAbstraction: List<Int>, val columnsAbstraction: List<Int>)

    private val field: Field
    private val fieldAbstraction: FieldAbstraction

    fun run(): Long {
        return fieldAbstraction.patterns.map(::getPoints).map {
            println(it)
            it
        }.sum().toLong()
    }

    private fun getPoints(pattern: PatternAbstraction): Int {
        val linesReflectionPoint = getLineReflectionPoint(pattern.linesAbstraction)
//        "patterns $pattern".println()
        return if (linesReflectionPoint != null) {
            100 * linesReflectionPoint
        } else getLineReflectionPoint(pattern.columnsAbstraction) ?: throw IllegalStateException("bla")
    }

    private fun getLineReflectionPoint(list: List<Int>): Int? {
        "getting reflection for $list".println()
        (0..list.lastIndex - 1).forEach { i ->
            val toCheck = list.subList(0, i + 1)
            val against = list.subList(i + 1, list.lastIndex + 1)
//            println("On $i checking $toCheck against $against")

            if (areEqual(toCheck, against)) {
//                "found for i=$i returning ${i+1}".println()
                return i + 1
            }
        }
        return null
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

    private fun areEqual(first: List<Int>, second: List<Int>): Boolean {
        val noToCheck = minOf(first.size, second.size)
        val firstCheck = first.reversed().subList(0, noToCheck)
        val secondCheck = second.subList(0, noToCheck)
//        "actually checking $firstCheck against $secondCheck ".println()
        return firstCheck == secondCheck
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
        fieldAbstraction = FieldAbstraction(field.patterns.map {
            val transposed = it.transpose()
            PatternAbstraction(it.lines.map { it.count { it == '#' } }, transposed.lines.map { it.count { it == '#' } })
        })
    }
}
