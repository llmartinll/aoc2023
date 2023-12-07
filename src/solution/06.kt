package solution

import println
import readInput
import kotlin.system.measureTimeMillis

fun main() {
    val day = "day06"
    val test = readInput("$day/test")
    val input = readInput("$day/input")

//    val solver = Day6(test)
    val solver = Day6(input)
    val elapsed = measureTimeMillis {
        solver.run().println()
    }
    "time taken: $elapsed ms".println()
}

class Day6(input: List<String>) {
    private val boat = Boat(0, 1)
    private val races: Map<Int, Race>

    data class Race(val duration: Int, val recordDistance: Int)
    data class Boat(val startingSpeed: Int, val mmPerSecondIncrease: Int) {
        fun getTravelDistance(holdTime: Int, raceDuration: Int) =
            startingSpeed + (holdTime * mmPerSecondIncrease) * (raceDuration - holdTime)
    }

    fun run(): Long =
        findNumberOfWaysToBeat(races, boat)

    private fun findNumberOfWaysToBeat(races: Map<Int, Race>, boat: Boat) =
        races
            .map { findWinningTimes(it.value, boat).size.toLong() }
            .reduce { acc, i -> acc * i }

    private fun findWinningTimes(race: Race, boat: Boat): List<Int> =
        (0..race.duration)
            .filter { boat.getTravelDistance(it, race.duration) > race.recordDistance }

    init {
        val racesBuilder: MutableMap<Int, Race> = mutableMapOf()

        input.forEach { string ->
            when {
                string.startsWith("Time:") -> {
                    var gameNo = 1
                    val part = string.substringAfter("Time: ")
                    Regex("\\d+").findAll(part).forEach {
                        racesBuilder[gameNo] = Race(it.value.toInt(), 0)
                        gameNo++
                    }
                }

                string.startsWith("Distance:") -> {
                    var gameNo = 1
                    val part = string.substringAfter("Distance: ")
                    Regex("\\d+").findAll(part).forEach {
                        val value = it.value.toInt()
                        racesBuilder[gameNo] = racesBuilder.get(gameNo)!!.copy(recordDistance = value)
                        gameNo++
                    }
                }
            }
        }
        races = racesBuilder.toMap()
    }
}
