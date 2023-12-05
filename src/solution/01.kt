package solution

import println
import readInput

val day = "day01"
val test1 = readInput("$day/test1")
val input1 = readInput("$day/input1")
val test2 = readInput("$day/test2")
val input2 = readInput("$day/input2")

fun main() {
    Day1().part1(test1).println()
    Day1().part1(input1).println()
    Day1().part2(test2).println()
    Day1().part2(input2).println()
}


class Day1 {
    fun part1(input: List<String>): Int {
        return input.sumOf(::getNumber)
    }

    fun part2(input: List<String>): Int {
        return input.sumOf(::getNumber2)
    }

    private fun getNumber(str: String): Int {
        val first = str.first { it.isDigit() }
        val last = str.last { it.isDigit() }
        val result = "$first$last".toInt()
//    "calibration value: $result".println()
        return result
    }

    private fun getNumber2(str: String): Int {
        val first = findFirst(str)
        val last = findLast(str)
        val result = "$first$last"
//    "first: $first, last: $last, calibration value: $result".println()
        return result.toInt()
    }

    private val replacements: Map<String, Int> =
        mapOf(
            "one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9,
            "1" to 1, "2" to 2, "3" to 3, "4" to 4, "5" to 5, "6" to 6, "7" to 7, "8" to 8, "9" to 9,
        )

    private fun findFirst(str: String): Int {
        var word = ""
        str.forEach { char ->
            word += char
            replacements.forEach {
                if (word.contains(it.key)) {
                    return it.value
                }
            }
        }
        throw Exception()
    }

    private fun findLast(str: String): Int {
        var word = ""
        str.reversed().forEach { char ->
            word = "$char$word"
            replacements.forEach {
                if (word.contains(it.key)) {
                    return it.value
                }
            }
        }
        throw Exception()
    }
}
