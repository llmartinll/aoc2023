package solution

import println
import readInput

fun main() {
    Day8(readInput("Day08/input")).run().println()
}

class Day8(input: List<String>) {
    private val instructions: List<Boolean> = input.first().map { it == 'L' } // takeFirst?
    private val nodesMap: Map<String, Pair<String, String>> = input.subList(2, input.lastIndex).associate { it.substring(0, 3) to Pair(it.substring(7, 10), it.substring(12, 15)) }
    private fun nextNode(current: String, takeLeft: Boolean) = nodesMap[current].let { node -> if (takeLeft) node!!.first else node!!.second }

    fun run(): Int =
        calculateStepsToEnd("AAA")

    private fun calculateStepsToEnd(node: String): Int {
        var currentNode = node
        var finished = false
        var noSteps = 0
        while (!finished) {
            instructions.map { instruction ->
                currentNode = nextNode(currentNode, instruction)
                noSteps++
                if (currentNode == "ZZZ") {
                    finished = true
                }
            }
        }

        return noSteps
    }
}
