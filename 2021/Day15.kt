package advent2021

import java.io.File

/** Day 15: Chiton */
fun main() {
    parseInput()
    solve(sizeMultiplier = 1)
    solve(sizeMultiplier = 5)
}

private val grid = arrayListOf<List<Long>>()
private fun parseInput() {
    File("input.txt").forEachLine { line ->
        grid.add(line.toCharArray().map { it.toString().toLong() })
    }
}

private fun solve(sizeMultiplier: Int) {
    val rowCount = grid.size
    val colCount = grid.first().size
    val lastRow = rowCount * sizeMultiplier - 1
    val lastCol = colCount * sizeMultiplier - 1
    val allRows = 0..lastRow
    val allCols = 0..lastCol

    println(findShortestPathCost(
        start = 0 to 0,
        end = lastRow to lastCol,
        getAdjacentNodes = { node ->
            adjacent4Cells(node.row, node.col)
                .filter { (r, c) -> r in allRows && c in allCols }
        },
        getEdgeWeight = { _, (r, c) ->
            (grid[r % rowCount][c % colCount]
                    + r / rowCount + c / colCount
                    - 1) % 9 + 1
        }
    ))
}