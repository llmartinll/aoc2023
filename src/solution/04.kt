package solution

import println
import readInput
import kotlin.math.pow
import kotlin.system.measureTimeMillis

fun main() {
    val day = "day04"
    val test = readInput("$day/test")
    val input = readInput("$day/input")

    val elapsed = measureTimeMillis {
        Day4().part1(test).println()
        Day4().part1(input).println()
        Day4().part2(test).println()
        Day4().part2(input).println()
    }
    "time taken: $elapsed ms".println()
}

class Day4 {
    fun part1(input: List<String>) =
        createCards(input).values.map(::calculatePoints).sum().toInt()

    fun part2(input: List<String>): Int {
        val cards = createCards(input)
        val numberOfCopies = getNumberOfCopies(cards)
        return numberOfCopies.values.sum()
    }

    private fun calculatePoints(noWinningNumbers: Int) =
        2.0.pow(noWinningNumbers - 1)

    private fun createCards(input: List<String>): Map<Int, Int> =
        input.mapIndexed { row, string ->
            val cardNo = row + 1
            val parts = string.split(':', '|')
            val winningNumbers = parts.get(1).split(" ").filterNot { it.isBlank() }
            val ownNumbers = parts.get(2).split(" ").filterNot { it.isBlank() }
            val noWinningNumbers = ownNumbers.filter { it in winningNumbers }.size
            cardNo to noWinningNumbers
        }.toMap() // map of Card # to # winning numbers

    private fun getNumberOfCopies(cards: Map<Int, Int>): Map<Int, Int> {
        val maxCardNumber = cards.size + 1
        val numberOfCopies: MutableMap<Int, Int> = mutableMapOf() // Card #, number of copies

        cards.forEach { card ->
            numberOfCopies[card.key]
        }
        cards.forEach { card ->
            val noWinningNumbers = card.value
            if (noWinningNumbers == 0) {
                return@forEach
            }
            val entryNoCopies = numberOfCopies[card.key] ?: 1
            for (i in 1..noWinningNumbers) {
                val cardToUpdate = card.key + i
                if (cardToUpdate <= maxCardNumber) {
                    numberOfCopies[cardToUpdate] = (numberOfCopies[cardToUpdate] ?: 1) + entryNoCopies
                }
            }
        }
        return numberOfCopies
    }
}
