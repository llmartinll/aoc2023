package solution

import print
import println
import readInput
import kotlin.system.measureTimeMillis

fun main() {
    val day = "day06"
    val test = readInput("$day/test")
    val input = readInput("$day/input")

    val elapsed = measureTimeMillis {
        Day6(test).run().println()
        Day6(input).run().println()
    }
    "time taken: $elapsed ms".println()
}

class Day6(input: List<String>) {
    data class Race(val duration: Int, val recordDistance: Int)
    data class Boat(val startingSpeed: Int, val mmPerSecondIncrease: Int) {
        fun getTravelDistance(holdTime: Int, raceDuration: Int) =
            startingSpeed + (holdTime * mmPerSecondIncrease) * (raceDuration - holdTime)
    }

    private val boats: Set<Boat>
    private val races: Map<Int, Race>

    fun run(): Long {
        val winingTimes = findWinningTimes(races[1]!!, boats.first())
        println(" winning times: $winingTimes")
        findNumberOfWaysToBeat(races, boats.first()).println()
        return 1
    }

    private fun findNumberOfWaysToBeat(races: Map<Int, Race>, boat: Boat): Long {
        val test = races.map{race ->
           findWinningTimes(race.value, boat).size.toLong()
        }
        return test.reduce { acc, i -> acc * i }
    }

    private fun findWinningTimes(race: Race, boat: Boat): List<Int> =
        (0..race.duration).filter { holdDuration ->
            val travelDistance = boat.getTravelDistance(holdDuration, race.duration)
            travelDistance > race.recordDistance
        }

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
        boats = setOf(
            Boat(0, 1)
        )
        races.print("races")
        boats.print("boats")
    }
}
