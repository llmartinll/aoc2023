package solution

import println
import readInput
import kotlin.system.measureTimeMillis

fun main() {
    val day = "day05"
    val test = readInput("$day/test")
    val input = readInput("$day/input")

    val elapsed = measureTimeMillis {
        Day5(test).run().println()
        Day5(input).run().println()
    }
    "time taken: $elapsed ms".println()
}

class Day5(input: List<String>) {
    data class MapEntry(val destinationRangeStart: Long, val sourceRangeStart: Long, val rangeLength: Long) {
        fun getSourceRange(): LongRange {
            return sourceRangeStart..sourceRangeStart + (rangeLength - 1)
        }

        fun getDestinationForSource(source: Long) =
            destinationRangeStart + source - sourceRangeStart
    }

    fun Set<MapEntry>.get(source: Long) =
        this.find { source in it.getSourceRange() }?.getDestinationForSource(source) ?: source

    var seeds: MutableSet<Long> = mutableSetOf()
    var seedToSoilMap: MutableSet<MapEntry> = mutableSetOf()
    var soilToFertilizerMap: MutableSet<MapEntry> = mutableSetOf()
    var fertilizerToWaterMap: MutableSet<MapEntry> = mutableSetOf()
    var waterToLightMap: MutableSet<MapEntry> = mutableSetOf()
    var lightToTemperatureMap: MutableSet<MapEntry> = mutableSetOf()
    var temperatureToHumidityMap: MutableSet<MapEntry> = mutableSetOf()
    var humidityToLocationMap: MutableSet<MapEntry> = mutableSetOf()

    private fun getLocationForSeed(seed: Long): Long {
        val soil = seedToSoilMap.get(seed)
        val fert = soilToFertilizerMap.get(soil)
        val water = fertilizerToWaterMap.get(fert)
        val light = waterToLightMap.get(water)
        val temp = lightToTemperatureMap.get(light)
        val hum = temperatureToHumidityMap.get(temp)
        val loc = humidityToLocationMap.get(hum)
        return loc
    }

    fun run(): Long {
        val lowest = seeds.map(::getLocationForSeed).min()
        return lowest
    }

    init {
        var currentMap: MutableSet<MapEntry> = seedToSoilMap
        input.forEach { string ->
            when {
                string.startsWith("seeds") -> {
                    val part = string.substringAfter("seeds: ")
                    Regex("\\d+").findAll(part).forEach {
                        seeds.add(it.value.toLong())
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
                    currentMap.add(MapEntry(numbers[0], numbers[1], numbers[2]))
                }
            }
        }
    }
}
