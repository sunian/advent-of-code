package advent2022

import bucketize
import java.io.File

/** Day 1: Calorie Counting */
fun main() {
    parseInput()
    part1()
    part2()
}

private lateinit var elves: List<List<Long>>

private fun parseInput() {
    elves = File("input.txt").readLines()
        .bucketize(delimiter = "", allowEmptyBuckets = false)
        .map { it.map(String::toLong) }
}

private fun part1() {
    println(elves.maxOf { it.sum() })
}

private fun part2() {
    val top3 = elves.map { it.sum() }
        .sortedDescending()
        .take(3)
        .sum()
    println(top3)
}