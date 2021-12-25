package advent2021

import java.io.File

/** Day 25: Sea Cucumber */
fun main() {
    part1()
    part2()
}

private fun part1() {
    var grid = File("input.txt").readLines()
    var step = 1
    while (true) {
        val beforeStep = grid
        grid = grid.moveEast().moveSouth()
        if (grid == beforeStep) {
            println(step)
            return
        }
        step++
    }
}

private fun List<String>.moveEast(): List<String> =
    this.mapIndexed { r, row ->
        row.mapIndexed { c, cell ->
            when (cell) {
                '.' -> if (this.getGridCell(r, c - 1) == '>') '>' else '.'
                '>' -> if (this.getGridCell(r, c + 1) == '.') '.' else '>'
                else -> cell
            }
        }.joinToString("")
    }

private fun List<String>.moveSouth(): List<String> =
    this.mapIndexed { r, row ->
        row.mapIndexed { c, cell ->
            when (cell) {
                '.' -> if (this.getGridCell(r - 1, c) == 'v') 'v' else '.'
                'v' -> if (this.getGridCell(r + 1, c) == '.') '.' else 'v'
                else -> cell
            }
        }.joinToString("")
    }

private fun List<String>.getGridCell(r: Int, c: Int): Char {
    val row = this[(r + this.size) % this.size]
    return row[(c + row.length) % row.length]
}

private fun part2() {
    println("there is no part 2! just click [Remotely Start The Sleigh Again]")
}