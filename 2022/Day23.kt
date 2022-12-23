package advent2022

import adjacent8Cells
import col
import minus
import plus
import row
import java.io.File

/** Day 23: Unstable Diffusion */
fun main() {
    parseInput()
    part1()
    parseInput()
    part2()
}

private val elfPositions = hashSetOf<Pair<Int, Int>>()

private val NORTH = -1 to 0
private val SOUTH = 1 to 0
private val WEST = 0 to -1
private val EAST = 0 to 1
private lateinit var moveQueue: ArrayList<Pair<Int, Int>>

private fun parseInput() {
    moveQueue = arrayListOf(NORTH, SOUTH, WEST, EAST)
    elfPositions.clear()
    File("input.txt").readLines()
        .forEachIndexed { row, line ->
            line.forEachIndexed { col, cell ->
                if (cell == '#') {
                    elfPositions.add(row to col)
                }
            }
        }
}

private fun part1() {
    repeat(10) {
        moveElves()
    }
    val (min, max) = findBounds()
    val area = (max - min + (1 to 1)).let { it.row * it.col }
    println(area - elfPositions.size)
}

private fun part2() {
    repeat(Int.MAX_VALUE) { t ->
        if (!moveElves()) {
            println(t + 1)
            return
        }
    }
}

private fun moveElves(): Boolean {
    val proposed = hashMapOf<Pair<Int, Int>, Pair<Int, Int>>()
    val propCount = hashMapOf<Pair<Int, Int>, Int>()
    elfPositions.forEach { pos ->
        if (adjacent8Cells(pos.row, pos.col).none(elfPositions::contains)) {
            return@forEach
        }
        moveQueue.firstOrNull { offset ->
            offset.includeDiagonals().none { elfPositions.contains(pos + it) }
        }?.let { offset ->
            val newPos = pos + offset
            proposed[pos] = newPos
            propCount[newPos] = (propCount[newPos] ?: 0) + 1
        }
    }
    moveQueue.add(moveQueue.removeAt(0))
    var didMove = false
    proposed.forEach { (old, new) ->
        if (propCount[new] == 1) {
            elfPositions.remove(old)
            elfPositions.add(new)
            didMove = true
        }
    }
    return didMove
}

private fun Pair<Int, Int>.includeDiagonals(): List<Pair<Int, Int>> =
    when (this.row) {
        0 -> listOf(this, this + (1 to 0), this + (-1 to 0))
        else -> listOf(this, this + (0 to 1), this + (0 to -1))
    }

/** min row/col to max row/col */
private fun findBounds(): Pair<Pair<Int, Int>, Pair<Int, Int>> =
    with(elfPositions) {
        (minOf { it.row } to minOf { it.col }) to
                (maxOf { it.row } to maxOf { it.col })
    }