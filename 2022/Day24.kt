package advent2022

import adjacent4Cells
import col
import emptyGrid
import findShortestPaths
import forEachInGrid
import getCell
import plus
import row
import java.io.File

/**Day 24: Blizzard Basin  */
fun main() {
    parseInput()
    part1()
    part2()
}

private val grid = emptyGrid<Char>()
private val blizzardCycle = arrayListOf<List<Blizzard>>()
private lateinit var startPos: Pair<Int, Int>
private lateinit var endPos: Pair<Int, Int>
private lateinit var startingState: State
private lateinit var possibleMoves: HashMap<Pair<Pair<Int, Int>, Pair<Int, Int>>, List<OpenMove>>

/** includes a lot of preprocessing */
private fun parseInput() {
    File("input.txt").readLines()
        .forEach { line -> grid.add(line.toMutableList()) }

    startPos = 0 to grid.first().indexOf('.')
    endPos = grid.lastIndex to grid.last().indexOf('.')
    val startingBlizzards = arrayListOf<Blizzard>()
    grid.forEachInGrid { row, col, cell ->
        when (cell) {
            '>' -> startingBlizzards.add(
                Blizzard(
                    row to col,
                    row to 1,
                    0 to 1,
                    symbol = cell
                )
            )

            'v' -> startingBlizzards.add(
                Blizzard(
                    row to col,
                    1 to col,
                    1 to 0,
                    symbol = cell
                )
            )

            '<' -> startingBlizzards.add(
                Blizzard(
                    row to col,
                    row to grid[row].lastIndex - 1,
                    0 to -1,
                    symbol = cell
                )
            )

            '^' -> startingBlizzards.add(
                Blizzard(
                    row to col,
                    grid.lastIndex - 1 to col,
                    -1 to 0,
                    symbol = cell
                )
            )
        }
    }
    blizzardCycle.add(startingBlizzards)
    while (true) {
        val newBlizzard = blizzardCycle.last().move()
        if (newBlizzard == blizzardCycle.first()) break
        blizzardCycle.add(newBlizzard)
    }
    val openingMapping = hashMapOf<Pair<Int, Int>, List<Int>>()
    val blizzardCycleSet = blizzardCycle.map { it.map { it.pos }.toSet() }
    grid.forEachInGrid { row, col, cell ->
        val pos = row to col
        val openings = when (cell) {
            '#' -> emptyList()
            else -> {
                blizzardCycleSet.mapIndexedNotNull { index, blizzards ->
                    index.takeIf { !blizzards.contains(pos) }
                }
            }
        }
        if (openings.isNotEmpty()) {
            openingMapping[pos] = openings
        }
    }
    val cycles = blizzardCycle.size
    val validPositions = openingMapping.keys
    possibleMoves = hashMapOf()
    validPositions.forEach { src ->
        val srcTimes = openingMapping.getValue(src)
        adjacent4Cells(src.row, src.col).forEach { dst ->
            if (validPositions.contains(dst)) {
                val dstTimes = openingMapping.getValue(dst)
                dstTimes.forEach { dstTime ->
                    var prevTime = (dstTime - 1).mod(cycles)
                    if (srcTimes.size == cycles) {
                        possibleMoves[src to dst] = (possibleMoves[src to dst] ?: emptyList()) +
                                OpenMove(src, dst, -1, dstTime) +
                                OpenMove(src, dst, -1, dstTime + cycles)
                    } else {
                        if (srcTimes.contains(prevTime)) {
                            while (srcTimes.contains(prevTime)) {
                                prevTime = (prevTime - 1).mod(cycles)
                            }
                            prevTime = (prevTime + 1).mod(cycles)
                            if (dstTime < prevTime) {
                                possibleMoves[src to dst] = (possibleMoves[src to dst] ?: emptyList()) +
                                        OpenMove(src, dst, prevTime, dstTime + cycles) +
                                        OpenMove(src, dst, prevTime - cycles, dstTime)
                            } else {
                                possibleMoves[src to dst] = (possibleMoves[src to dst] ?: emptyList()) +
                                        OpenMove(src, dst, prevTime, dstTime)
                            }
                        }
                    }
                }
            }
        }
    }
    startingState = State(startPos, 0)
}

private fun part1() {
    traverseValley(targetGoalCount = 1)
}

private fun part2() {
    traverseValley(targetGoalCount = 2)
}

private fun traverseValley(targetGoalCount: Int) {
    val cycles = blizzardCycle.size
    val ans = findShortestPaths(
        start = startingState,
        getAdjacentNodes = {
            when (it.endCount) {
                targetGoalCount -> emptyList()
                else -> it.possibleNextStates()
            }
        },
        getEdgeWeight = { src, dst ->
            when {
                src.blizzardsConfig <= dst.blizzardsConfig -> dst.blizzardsConfig - src.blizzardsConfig
                else -> dst.blizzardsConfig + cycles - src.blizzardsConfig
            }.toLong()
        }
    )
    println(ans.filter { it.key.endCount == targetGoalCount }
        .minOf { it.value.cost })
}

private data class OpenMove(
    val src: Pair<Int, Int>,
    val dst: Pair<Int, Int>,
    val srcTime: Int,
    val dstTime: Int
)

private data class Blizzard(
    val pos: Pair<Int, Int>,
    val startingPos: Pair<Int, Int>,
    val offset: Pair<Int, Int>,
    val symbol: Char
)

private data class State(
    val myPos: Pair<Int, Int>,
    val blizzardsConfig: Int,
    val startCount: Int = 1,
    val endCount: Int = 0
) {
    fun possibleNextStates(): List<State> = adjacent4Cells(myPos.row, myPos.col)
        .mapNotNull { dst ->
            val moves = possibleMoves[myPos to dst] ?: emptyList()
            moves.filter { it.srcTime <= blizzardsConfig && it.dstTime > blizzardsConfig }
                .minOfOrNull { it.dstTime }
                ?.let {
                    State(
                        dst,
                        it.mod(blizzardCycle.size),
                        when {
                            dst != startPos -> startCount
                            startCount > endCount -> startCount
                            else -> startCount + 1
                        },
                        when {
                            dst != endPos -> endCount
                            startCount == endCount + 1 -> endCount + 1
                            else -> endCount
                        },
                    )
                }
        }
}

private fun List<Blizzard>.move() = this.map { old ->
    var newPos = old.pos + old.offset
    if (grid.getCell(newPos) == '#') {
        newPos = old.startingPos
    }
    old.copy(pos = newPos)
}