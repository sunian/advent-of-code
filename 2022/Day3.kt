package advent2022

import bucketize
import java.io.File

/** Day 3: Rucksack Reorganization */
fun main() {
    parseInput()
    part1()
    part2()
}

private lateinit var rucksacks: List<String>

private fun parseInput() {
    rucksacks = File("input.txt").readLines()
}

private fun part1() {
    println(
        rucksacks.sumOf { rucksack ->
            val firstCompartment = rucksack.substring(0, rucksack.length / 2)
            val secondCompartment = rucksack.substring(rucksack.length / 2, rucksack.length)
            firstCompartment.first(secondCompartment::contains).priority()
        }
    )
}

private fun part2() {
    println(
        rucksacks.bucketize(bucketSize = 3)
            .map(::getBadge)
            .sumOf(Char::priority)
    )
}

private fun getBadge(rucksacks: List<String>): Char =
    rucksacks[0].first { item ->
        rucksacks.all { rucksack -> rucksack.contains(item) }
    }

private fun Char.priority(): Int =
    when {
        this < 'a' -> this - 'A' + 27
        else -> this - 'a' + 1
    }