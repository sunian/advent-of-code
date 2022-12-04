package advent2022

import contains
import intersects
import toPair
import java.io.File

/** Day 4: Camp Cleanup */
fun main() {
    parseInput()
    part1()
    part2()
}

private lateinit var assignments: List<Pair<IntRange, IntRange>>

private fun parseInput() {
    assignments = File("input.txt").readLines()
        .map { pair ->
            pair.split(",")
                .map { assignment ->
                    val split = assignment.split("-").map(String::toInt)
                    split[0]..split[1]
                }
                .toPair()
        }
}

private fun part1() {
    println(assignments.count { (first, second) ->
        first.contains(second) || second.contains(first)
    })
}

private fun part2() {
    println(assignments.count { (first, second) ->
        first.intersects(second)
    })
}