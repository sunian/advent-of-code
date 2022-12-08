package advent2022

import adjacentOffsets
import forEachInGrid
import getCell
import java.io.File
import kotlin.math.max

/** Day 8: Treetop Tree House */
fun main() {
    parseInput()
    part1()
    part2()
}

private lateinit var grid: List<List<TreePosition>>

private class TreePosition(
    val height: Int,
    var isVisible: Boolean = false
)

private fun parseInput() {
    grid = File("input.txt").readLines()
        .map { line ->
            line.map { TreePosition(it.digitToInt()) }
        }
}

private fun part1() {
    repeat(grid.size) { row ->
        trackTallest(rowStart = row, colStart = 0, rowStep = 0, colStep = 1)
        trackTallest(rowStart = row, colStart = grid[row].lastIndex, rowStep = 0, colStep = -1)
    }
    repeat(grid[0].size) { col ->
        trackTallest(rowStart = 0, colStart = col, rowStep = 1, colStep = 0)
        trackTallest(rowStart = grid.lastIndex, colStart = col, rowStep = -1, colStep = 0)
    }
    var count = 0
    grid.forEachInGrid { row, col, cell ->
        if (cell.isVisible) {
            count++
        }
    }
    println(count)
}

private fun part2() {
    var maxScore = 0L
    grid.forEachInGrid { row, col, cell ->
        maxScore = max(maxScore, totalScore(row, col))
    }
    println(maxScore)
}

private fun trackTallest(rowStart: Int, colStart: Int, rowStep: Int, colStep: Int) {
    var tallestSoFar = -1
    var row = rowStart
    var col = colStart
    while (true) {
        val tree = grid.getCell(row, col, default = null)
            ?: break
        if (tree.height > tallestSoFar) {
            tree.isVisible = true
            tallestSoFar = tree.height
        }
        row += rowStep
        col += colStep
    }
}

private fun totalScore(row: Int, col: Int): Long =
    adjacentOffsets.map { (rowStep, colStep) ->
        countVisibleTrees(row, col, rowStep, colStep)
    }.reduce { acc, n -> acc * n }

private fun countVisibleTrees(rowStart: Int, colStart: Int, rowStep: Int, colStep: Int): Long {
    val startHeight = grid[rowStart][colStart].height
    var row = rowStart + rowStep
    var col = colStart + colStep
    var count = 0L
    while (true) {
        val tree = grid.getCell(row, col, default = null)
            ?: break
        count++
        if (tree.height >= startHeight) {
            break
        }
        row += rowStep
        col += colStep
    }
    return count
}