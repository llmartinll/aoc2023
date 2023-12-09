package solution

import println
import readInput

fun main() {
    Day8_part2(readInput("day08/input")).run().println()
}

class Day8_part2(input: List<String>) {
    private val instructions: List<Boolean> = input.first().map { it == 'L' } // take left?
    private val nodesMap: Map<String, Pair<String, String>> = input.subList(2, input.lastIndex + 1).associate { it.substring(0, 3) to Pair(it.substring(7, 10), it.substring(12, 15)) }
    private val startingNodes: List<String> = input.subList(2, input.lastIndex + 1).map { it.substring(0, 3) }.filter { it.endsWith('A') }

    private fun nextNode(current: String, takeLeft: Boolean) = nodesMap[current].let { node -> if (takeLeft) node!!.first else node!!.second }

    fun run(): Long {
        val noAllStepsToEnd: List<Long> = startingNodes.map(::calculateStepsToEnd)
        val primeFactors = noAllStepsToEnd.map(::primeFactors)
        val highestSharedFactor = primeFactors.first().filter { a -> primeFactors.all { b -> b.contains(a) } }.max()
        return noAllStepsToEnd.map { it / highestSharedFactor }.reduce { acc, l -> acc * l } * highestSharedFactor
    }

    private fun calculateStepsToEnd(node: String): Long {
        var currentNode = node
        var noSteps = 0L
        while (true) {
            instructions.map { instruction ->
                currentNode = nextNode(currentNode, instruction)
                noSteps++
                if (currentNode.last() == 'Z') {
                    return noSteps
                }
            }
        }
    }

    // from google
    private fun primeFactors(number: Long): ArrayList<Long> {

        // Array that contains all the prime factors of given number.
        val arr: ArrayList<Long> = arrayListOf()


        var n = number

        // At first check for divisibility by 2. add it in arr till it is divisible
        while (n % 2 == 0L) {
            arr.add(2)
            n /= 2
        }

        val squareRoot = Math.sqrt(n.toDouble()).toLong()

        // Run loop from 3 to square root of n. Check for divisibility by i. Add i in arr till it is divisible by i.
        for (i in 3..squareRoot step 2) {
            while (n % i == 0L) {
                arr.add(i)
                n /= i
            }
        }

        // If n is a prime number greater than 2.
        if (n > 2) {
            arr.add(n)
        }

        return arr
    }

}
