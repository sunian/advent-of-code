package advent2022

import adjacent4Cells
import findShortestPath
import findShortestPaths
import forEachInGrid
import getCell
import java.io.File

/** Day 12: Hill Climbing Algorithm */
fun main() {
    parseInput()
    part1()
    part2()
}

private lateinit var input: List<List<Char>>
private lateinit var startPos: Pair<Int, Int>
private lateinit var endPos: Pair<Int, Int>

private fun parseInput() {
    input = File("input.txt").readLines()
        .map { it.toCharArray().toList() }
    input.forEachInGrid { row, col, cell ->
        when (cell) {
            'S' -> startPos = row to col
            'E' -> endPos = row to col
        }
    }
}

private fun part1() {
    val path = findShortestPath(
        start = startPos,
        end = endPos,
        getAdjacentNodes = { (r, c) ->
            val current = input[r][c].convert()
            adjacent4Cells(r, c)
                .filter { (r, c) ->
                    val cell = input.getCell(r, c, null)?.convert()
                        ?: return@filter false
                    cell - current <= 1
                }
        },
        getEdgeWeight = { _, _ -> 1L }
    )
    println(path.size - 1) // ignore start node in path
}

private fun part2() {
    val paths = findShortestPaths(
        start = endPos,
        getAdjacentNodes = { (r, c) ->
            val current = input[r][c].convert()
            adjacent4Cells(r, c)
                .filter { (r, c) ->
                    val cell = input.getCell(r, c, null)?.convert()
                        ?: return@filter false
                    cell >= current - 1
                }
        },
        getEdgeWeight = { _, _ -> 1L }
    )
    println(
        paths.filter { input.getCell(it.key).convert() == 'a' }
            .minOf { it.value.cost }
    )
}

private fun Char.convert() = when (this) {
    'S' -> 'a'
    'E' -> 'z'
    else -> this
}