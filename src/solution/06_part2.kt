package solution

import println
import readInput
import kotlin.math.pow
import kotlin.system.measureNanoTime

fun main() {
    val day = "day06"
    val test = readInput("$day/test")
    val input = readInput("$day/input")

//    val solver = Day6_part2(test)
    val solver = Day6_part2(input)
    val elapsed = measureNanoTime {
        solver.run().println()
    }
    "time taken: $elapsed ms".println()
}

class Day6_part2(input: List<String>) {
    private val race: Race

    data class Race(val raceDuration: Long, val recordDistance: Long)

    fun run(): Long =
        race.getRangeToBeat().length()

    private fun Race.getRangeToBeat(): LongRange {
        val travelDistanceToBeat = recordDistance + 1
        val a = 1
        val b = -raceDuration
        val c = travelDistanceToBeat
        val min = ((-b - Math.sqrt(b.toDouble().pow(2) - 4 * a * c)) / 2 * a).toLong() + 1
        val max = ((-b + Math.sqrt(b.toDouble().pow(2) - 4 * a * c)) / 2 * a).toLong()
        return min..max
    }

    private fun LongRange.length() =
        this.last - this.first + 1

    init {
        var duration = 0L
        var recordTime = 0L
        input.forEach { string ->
            when {
                string.startsWith("Time:") -> {
                    val part = string.substringAfter("Time: ")
                    duration = part.replace(" ", "").toLong()
                }

                string.startsWith("Distance:") -> {
                    val part = string.substringAfter("Distance: ")
                    recordTime = part.replace(" ", "").toLong()
                }
            }
        }
        race = Race(duration, recordTime)
    }
}
