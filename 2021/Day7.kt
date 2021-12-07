package advent2021

import java.io.File
import kotlin.math.abs

/** Day 7: The Treachery of Whales */
fun main() {
    parseInput()
    part2()
}

private val initPositions = arrayListOf<Long>()
private fun parseInput() {
    val positions = File("input.txt").readLines()[0].split(",").map { it.toLong() }
    initPositions.addAll(positions)
}

private fun part2() {
    var minSum = Long.MAX_VALUE
    for (target in initPositions.minOrNull()!!..initPositions.maxOrNull()!!) {
        val sum = initPositions.sumOf {
            val dist = abs(it - target)
            ((dist + 1) * dist) / 2
        }
        if (sum < minSum) minSum = sum
    }
    println(minSum)
}

private fun part1() {
    var minSum = Long.MAX_VALUE
    for (target in initPositions.minOrNull()!!..initPositions.maxOrNull()!!) {
        val sum = initPositions.sumOf { abs(it - target) }
        if (sum < minSum) minSum = sum
    }
    println(minSum)
}