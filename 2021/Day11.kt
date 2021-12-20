package advent2021

import java.io.File

/** Day 11: Dumbo Octopus */
fun main() {
    parseInput()
    part2()
}

private val grid = emptyGrid<Int>()
private fun parseInput() {
    File("input.txt").forEachLine { line ->
        grid.add(line.map { it.toString().toInt() }.toMutableList())
    }
}

private fun part2() {
    repeat(Int.MAX_VALUE) { i ->
        var flashCount = 0
        grid.forEachInGrid { row, col, cell ->
            flashCount += increaseEnergy(row, col)
        }
        grid.forEachInGrid { row, col, cell ->
            if (cell > 9) {
                grid[row][col] = 0
            }
        }
        if (flashCount == 100) {
            println(i + 1)
            return
        }
    }
}

private fun part1() {
    var flashCount = 0
    repeat(100) {
        grid.forEachInGrid { row, col, cell ->
            flashCount += increaseEnergy(row, col)
        }
        grid.forEachInGrid { row, col, cell ->
            if (cell > 9) {
                grid[row][col] = 0
            }
        }
    }
    println(flashCount)
}

// returns the recursive number of flashes resulting from this increment
private fun increaseEnergy(row: Int, col: Int): Int {
    grid[row][col]++
    if (grid[row][col] != 10) return 0

    return 1 + adjacent8Cells(row, col)
        .filter { (r, c) -> grid.getCell(r, c, -1) >= 0 }
        .sumOf { (r, c) -> increaseEnergy(r, c) }
}