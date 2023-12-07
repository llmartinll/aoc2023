package solution

import println
import readInput

fun main() {
    Day2().part2(readInput("Day02/input")).println()
}

class Day2 {
    enum class Color {
        RED, BLUE, GREEN,
    }

    private fun String.toColor() = Color.valueOf(this.uppercase())

    fun part1(input: List<String>): Int =
        createMap(input)
            .filter {
                it.value.get(Color.BLUE)!! <= 14
                    && it.value.get(Color.RED)!! <= 12
                    && it.value.get(Color.GREEN)!! <= 13
            }.map { it.key }.sum()

    fun part2(input: List<String>): Int =
        createMap(input).entries.sumOf { it.value.values.reduce { acc, i -> acc * i } }

    private fun createMapEntry(input: String): Map<Int, Map<Color, Int>> {
        val id = input.takeWhile { it != ':' }.filter { it.isDigit() }.toInt()
        val lastPart = input.takeLastWhile { it != ':' }.replace(" ", "").split(',', ';')
        val map = mutableMapOf(Color.RED to 0, Color.BLUE to 0, Color.GREEN to 0)

        lastPart.forEach {
            val newValue = it.takeWhile { it.isDigit() }.toInt()
            val color: Color = it.takeLastWhile { !it.isDigit() }.toColor()

            val oldValue = map[color]!!
            if (newValue > oldValue) {
                map[color] = newValue
            }
        }
        return mapOf(id to map.toMap())
    }

    private fun createMap(input: List<String>): Map<Int, Map<Color, Int>> {
        val map: MutableMap<Int, Map<Color, Int>> = mutableMapOf()
        input.forEach { map += createMapEntry(it) }
        return map
    }

}
