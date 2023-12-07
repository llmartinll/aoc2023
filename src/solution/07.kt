package solution

import println
import readInput

fun main() {
    Day7(readInput("Day07/input")).run().println()
}

class Day7(input: List<String>) {
    private data class Hand(val cards: List<Card>, val bid: Int)
    private enum class Card(val char: Char) { TWO('2'), THREE('3'), FOUR('4'), FIVE('5'), SIX('6'), SEVEN('7'), EIGHT('8'), NINE('9'), TEN('T'), JACK('J'), QUEEN('Q'), KING('K'), ACE('A'), }
    private enum class Type { HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND, FULL_HOUSE, FOUR_OF_A_KIND, FIVE_OF_A_KIND }

    private val hands: List<Hand> = input.map { string -> Hand(string.substringBefore(' ').map { Card.entries.first { card -> card.char == it } }, string.substringAfter(' ').toInt()) }
    private fun Hand.countLabels(): List<Int> = cards.groupingBy { it }.eachCount().values.sortedDescending()
    private val handsComparator = Comparator<Hand> { a, b -> a.getType().ordinal - b.getType().ordinal }
    private val cardsComparator = Comparator<Hand> { a, b -> a.cards.mapIndexed { index, card -> cardComparator.compare(card, b.cards[index]) }.firstOrNull { it != 0 } ?: 0 }
    private val cardComparator = Comparator<Card> { a, b -> a.ordinal - b.ordinal }

    fun run(): Int =
        hands.sortedWith(handsComparator.thenComparing(cardsComparator)).mapIndexed { index, hand -> hand.bid * (index + 1) }.sum()

    private fun Hand.getType(): Type =
        when (this.countLabels()) {
            listOf(5) -> Type.FIVE_OF_A_KIND
            listOf(4, 1) -> Type.FOUR_OF_A_KIND
            listOf(3, 2) -> Type.FULL_HOUSE
            listOf(3, 1, 1) -> Type.THREE_OF_A_KIND
            listOf(2, 2, 1) -> Type.TWO_PAIR
            listOf(2, 1, 1, 1) -> Type.ONE_PAIR
            else -> Type.HIGH_CARD
        }
}
