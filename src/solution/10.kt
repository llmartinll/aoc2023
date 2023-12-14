package solution

import println
import readInput

fun main() {
    Day10(readInput("Day10/test")).run().println()
//    Day10(readInput("Day10/input")).run().println()
}

class Day10(input: List<String>) {
    private enum class Tile(val char: Char) { NS_PIPE('|'), WE_PIPE('-'), NE_BEND('L'), NW_BEND('J'), SW_BEND('7'), SE_BEND('F'), GROUND('.'), START('S') }
    private enum class Dir { N, E, S, W }
    private data class Pos(val lineNr: Int, val cNr: Int) {
        override fun toString() = "($lineNr,$cNr)"
    }

    private val grid: Map<Pos, Tile> = input.flatMapIndexed { lineIndex, line -> line.mapIndexed { charIndex, char -> Pos(lineIndex, charIndex) to Tile.entries.first { it.char == char } } }.associate { it.first to it.second }
    private val lastLineIndex = grid.entries.last().key.lineNr
    private val lastColIndex = grid.entries.last().key.cNr
    private val startingPosition = grid.entries.first { it.value == Tile.START }.key
    private val loop = listOf(Dir.E, Dir.S).firstNotNullOf { dir -> buildLoop(startingPosition, startingPosition.next(dir)) }

    fun run(): Int {
        return findIslands().size
    }

    private fun findIslands(): List<Pos> {
        val islands = (0..lastLineIndex).flatMap { lineIndex ->
            (0..lastColIndex).map { colIndex -> Pos(lineIndex, colIndex) }
        }
            .filter { !loop.contains(it) }
            .filter { it.isIsland() }
        return islands
    }

    private fun Pos.isIsland(): Boolean {
        var curPos = this
        var noCrossings = 0
        var isSqueezedRight = false
        var isSqueezedLeft = false
        while (exists(curPos)) {
            if (loop.contains(curPos)) {
                val tile = grid[curPos]!!
                when (tile) {
                    Tile.WE_PIPE -> noCrossings++
                    Tile.SE_BEND -> {
                        if (isSqueezedLeft) {
                            isSqueezedLeft = false
                        }
                        if (isSqueezedRight) {
                            isSqueezedRight = false
                            noCrossings++
                        }
                    }

                    Tile.SW_BEND -> {
                        if (isSqueezedLeft) {
                            isSqueezedLeft = false
                            noCrossings++
                        }
                        if (isSqueezedRight) {
                            isSqueezedRight = false
                        }
                    }

                    Tile.NE_BEND -> isSqueezedLeft = true
                    Tile.NW_BEND -> isSqueezedRight = true
                    else -> {}
                }
            }
            curPos = curPos.next(Dir.N)
        }
        return noCrossings % 2 == 1
    }

    private fun buildLoop(previousPosition: Pos, currentPosition: Pos): List<Pos>? {
        if (!exists(currentPosition)) {
            return null
        }
        if (grid[currentPosition]!! == Tile.START) {
            return listOf(currentPosition)
        }
        val previousDirection = getPreviousDirection(previousPosition, currentPosition)
        val nextPosition = currentPosition.getNext(previousDirection)
        if (nextPosition == null) {
            return null
        } else {
            val chain = buildLoop(currentPosition, nextPosition)
            return if (chain == null) {
                null
            } else {
                listOf(currentPosition) + chain
            }
        }
    }

    private fun Pos.getNext(prevDir: Dir): Pos? {
        val tile = grid[this]!!
        return when (tile) {
            Tile.NS_PIPE -> {
                when (prevDir) {
                    Dir.N -> this.next(Dir.S)
                    Dir.S -> this.next(Dir.N)
                    else -> null
                }
            }

            Tile.WE_PIPE -> {
                when (prevDir) {
                    Dir.W -> this.next(Dir.E)
                    Dir.E -> this.next(Dir.W)
                    else -> null
                }
            }

            Tile.GROUND -> null
            Tile.START -> throw IllegalStateException()
            Tile.NE_BEND -> {
                when (prevDir) {
                    Dir.N -> this.next(Dir.E)
                    Dir.E -> this.next(Dir.N)
                    else -> null
                }
            }

            Tile.NW_BEND -> {
                when (prevDir) {
                    Dir.N -> this.next(Dir.W)
                    Dir.W -> this.next(Dir.N)
                    else -> null
                }
            }

            Tile.SE_BEND -> {
                when (prevDir) {
                    Dir.S -> this.next(Dir.E)
                    Dir.E -> this.next(Dir.S)
                    else -> null
                }
            }

            Tile.SW_BEND -> {
                when (prevDir) {
                    Dir.S -> this.next(Dir.W)
                    Dir.W -> this.next(Dir.S)
                    else -> null
                }
            }
        }
    }

    private fun Pos.next(dir: Dir): Pos {
        return when (dir) {
            Dir.N -> Pos(this.lineNr - 1, this.cNr)
            Dir.E -> Pos(this.lineNr, this.cNr + 1)
            Dir.S -> Pos(this.lineNr + 1, this.cNr)
            Dir.W -> Pos(this.lineNr, this.cNr - 1)
        }
    }

    private fun getPreviousDirection(previous: Pos, current: Pos): Dir {
        return when {
            current.cNr > previous.cNr -> Dir.W
            current.cNr < previous.cNr -> Dir.E
            current.lineNr > previous.lineNr -> Dir.N
            current.lineNr < previous.lineNr -> Dir.S
            else -> Dir.E
        }
    }

    private fun exists(c: Pos): Boolean = c.lineNr >= 0 && c.lineNr <= grid.entries.last().key.lineNr && c.cNr >= 0 && c.cNr <= grid.entries.last().key.cNr
}
