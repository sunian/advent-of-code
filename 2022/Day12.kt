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

private lateinit var grid: List<List<Char>>
private lateinit var startPos: Pair<Int, Int>
private lateinit var endPos: Pair<Int, Int>

private fun parseInput() {
    grid = File("input.txt").readLines()
        .map { it.toCharArray().toList() }
    grid.forEachInGrid { row, col, cell ->
        when (cell) {
            'S' -> startPos = row to col
            'E' -> endPos = row to col
        }
    }
}

private fun part1() {

    // find the shortest path from S to E
    // 2 nodes are considered adjacent if the dest is at most 1 unit higher than the src
    // all edges between adjacent nodes have weight 1
    val path = findShortestPath(
        start = startPos,
        end = endPos,
        getAdjacentNodes = { (r, c) ->
            val current = grid[r][c].convert()
            adjacent4Cells(r, c)
                .filter { (r, c) ->
                    val cell = grid.getCell(r, c, null)?.convert()
                        ?: return@filter false
                    cell - current <= 1
                }
        },
        getEdgeWeight = { _, _ -> 1L }
    )
    println(path.size - 1) // ignore start node in path
}

private fun part2() {
    // find the shortest path from end node to all other nodes
    val paths = findShortestPaths(
        start = endPos,
        getAdjacentNodes = { (r, c) ->
            val current = grid[r][c].convert()
            adjacent4Cells(r, c)
                .filter { (r, c) ->
                    val cell = grid.getCell(r, c, null)?.convert()
                        ?: return@filter false
                    cell >= current - 1
                }
        },
        getEdgeWeight = { _, _ -> 1L }
    )
    // filter for paths that lead to an 'a' and find the shortest of those
    // since all edges have weight 1, the cost of the path is equal to its length
    println(
        paths.filter { grid.getCell(it.key).convert() == 'a' }
            .minOf { it.value.cost }
    )
}

private fun Char.convert() = when (this) {
    'S' -> 'a'
    'E' -> 'z'
    else -> this
}