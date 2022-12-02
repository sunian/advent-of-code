package advent2022

import toPair
import java.io.File

/** Day 2: Rock Paper Scissors */
fun main() {
    parseInput()
    part1(strategy)
    part2(strategy)
}

private lateinit var strategy: List<Pair<Int, Int>>

private fun parseInput() {
    strategy = File("input.txt").readLines().map { line ->
        line.split(" ").map { char ->
            when (char) {
                "A", "X" -> 0
                "B", "Y" -> 1
                else -> 2
            }
        }.toPair()
    }
}

private fun part1(strategy: List<Pair<Int, Int>>) {
    var score = 0
    strategy.forEach { (op, me) ->
        score += me + 1
        score += (me - op + 1).mod(3) * 3
    }
    println(score)
}

private fun part2(strategy: List<Pair<Int, Int>>) {
    val newStrategy = strategy.map { (op, me) ->
        op to (op + me - 1).mod(3)
    }
    part1(newStrategy)
}