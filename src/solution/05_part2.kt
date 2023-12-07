package solution

import println
import readInput
import kotlin.math.max
import kotlin.math.min

fun main() {
    Day5_part2(readInput("Day05/input")).run().println()
}

class Day5_part2(input: List<String>) {
    private var seedRanges: MutableSet<LongRange> = mutableSetOf()

    private var seedToSoilMap: MutableMap<LongRange, Long> = mutableMapOf() // source range, shift
    private var soilToFertilizerMap: MutableMap<LongRange, Long> = mutableMapOf()
    private var fertilizerToWaterMap: MutableMap<LongRange, Long> = mutableMapOf()
    private var waterToLightMap: MutableMap<LongRange, Long> = mutableMapOf()
    private var lightToTemperatureMap: MutableMap<LongRange, Long> = mutableMapOf()
    private var temperatureToHumidityMap: MutableMap<LongRange, Long> = mutableMapOf()
    private var humidityToLocationMap: MutableMap<LongRange, Long> = mutableMapOf()

    private fun LongRange.overLapWith(other: LongRange): LongRange =
        max(this.first, other.first)..min(this.last, other.last)

    private fun LongRange.shift(shift: Long) = this.first + shift..this.last + shift

    // todo also return non-mapped
    private fun Map<LongRange, Long>.getDestinationRanges(sourceRange: LongRange): List<LongRange> =
        this.entries.map { entry ->
            entry.key.overLapWith(sourceRange).shift(entry.value)
        }.filterNot { it.isEmpty() }

    private fun getLocationRangesForSeedRange(seedRange: LongRange): List<LongRange> {
        val soilRanges = seedToSoilMap.getDestinationRanges(seedRange)
        val fert = soilRanges.flatMap { soilToFertilizerMap.getDestinationRanges(it) }
        val water = fert.flatMap { fertilizerToWaterMap.getDestinationRanges(it) }
        val light = water.flatMap { waterToLightMap.getDestinationRanges(it) }
        val temp = light.flatMap { lightToTemperatureMap.getDestinationRanges(it) }
        val hum = temp.flatMap { temperatureToHumidityMap.getDestinationRanges(it) }
        val loc = hum.flatMap { humidityToLocationMap.getDestinationRanges(it) }
        return loc
    }

    fun run(): Long =
        seedRanges.flatMap(::getLocationRangesForSeedRange).minOf { it.first }

    init {
        var currentMap: MutableMap<LongRange, Long> = seedToSoilMap
        input.forEach { string ->
            when {
                string.startsWith("seeds") -> {
                    val part = string.substringAfter("seeds: ")
                    var rangeStart: Long? = null
                    Regex("\\d+").findAll(part).forEach {
                        val number = it.value.toLong()
                        if (rangeStart == null) {
                            rangeStart = number
                            return@forEach
                        } else {
                            seedRanges.add(rangeStart!!..rangeStart!! + number)
                            rangeStart = null
                        }
                    }
                }

                string.contains("map:") -> {
                    when (string) {
                        "seed-to-soil map:" -> currentMap = seedToSoilMap
                        "soil-to-fertilizer map:" -> currentMap = soilToFertilizerMap
                        "fertilizer-to-water map:" -> currentMap = fertilizerToWaterMap
                        "water-to-light map:" -> currentMap = waterToLightMap
                        "light-to-temperature map:" -> currentMap = lightToTemperatureMap
                        "temperature-to-humidity map:" -> currentMap = temperatureToHumidityMap
                        "humidity-to-location map:" -> currentMap = humidityToLocationMap
                    }
                }

                string.isBlank() -> {
                    return@forEach
                }

                else -> {
                    val numbers = Regex("\\d+").findAll(string).take(3).map { it.value.toLong() }.toList()
                    val destinationStart = numbers[0]
                    val sourceStart = numbers[1]
                    val range = numbers[2]
                    currentMap[sourceStart..<sourceStart + range] = destinationStart - sourceStart
                }
            }
        }
    }
}
