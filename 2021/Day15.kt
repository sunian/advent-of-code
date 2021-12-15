package advent2021

import java.io.File

/** Day 15: Chiton */
fun main() {
    parseInput()
    part1()
    part2()
}

private val grid = arrayListOf<List<Long>>()
private fun parseInput() {
    File("input.txt").forEachLine { line ->
        grid.add(line.toCharArray().map { it.toString().toLong() })
    }
}

private fun part2() {
    val rows = grid.size
    val cols = grid.first().size
    val lastRow = rows * 5 - 1
    val lastCol = cols * 5 - 1
    println(findShortestPathCost(
        start = 0 to 0,
        end = lastRow to lastCol,
        getAdjacentNodes = { (r, c) -> adjacentCells(r, c) },
        getEdgeWeight = { _, (r, c) ->
            if (r in 0..lastRow && c in 0..lastCol) {
                var edge = grid.getCell(r % rows, c % cols, 0)
                edge += r / rows + c / cols
                if (edge > 9) {
                    edge -= 9
                }
                edge
            } else {
                Long.MAX_VALUE
            }
        }
    ))
}

private fun part1() {
    println(findShortestPathCost(
        start = 0 to 0,
        end = grid.size - 1 to grid.first().size - 1,
        getAdjacentNodes = { (r, c) -> adjacentCells(r, c) },
        getEdgeWeight = { _, (r, c) -> grid.getCell(r, c, Long.MAX_VALUE) }
    ))
}