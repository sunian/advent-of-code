package advent2021

import java.io.File

/** Day 20: Trench Map */
fun main() {
    parseInput()
    applyAlgoTwice(1)
    applyAlgoTwice(25)
}

private var algorithm = listOf<Int>()
private val grid = arrayListOf<List<Int>>()
private fun parseInput() {
    File("input.txt").forEachLine { line ->
        if (algorithm.isEmpty()) {
            algorithm = line.map {
                when (it) {
                    '#' -> 1
                    else -> 0
                }
            }
        } else {
            grid.add(line.map {
                when (it) {
                    '#' -> 1
                    else -> 0
                }
            })
        }
    }
}

private fun applyAlgoTwice(times: Int) {
    var grid: List<List<Int>> = grid
    repeat(times) {
        grid = grid.applyAlgo(0)
        grid = grid.applyAlgo(1)
    }
    println(grid.sumOf { it.sum() })
}

private fun List<List<Int>>.applyAlgo(default: Int): List<List<Int>> =
    (-1..this.lastIndex + 1).map { r ->
        (-1..this.first().lastIndex + 1).map { c ->
            val bitString = adjacent9Cells(r, c)
                .joinToString("") { (r, c) ->
                    this.getCell(r, c, default).toString()
                }
            val index = bitString.toInt(2)
            algorithm[index]
        }
    }