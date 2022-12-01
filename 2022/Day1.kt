package advent2022

import java.io.File

/** Day 1: Calorie Counting */
fun main() {
    parseInput()
    part1()
    part2()
}

val elves = arrayListOf<List<Long>>()

private fun parseInput() {
    var elf = arrayListOf<Long>()
    elves.add(elf)
    File("input.txt").forEachLine { line ->
        if (line.isEmpty()) {
            elf = arrayListOf()
            elves.add(elf)
        } else {
            elf.add(line.toLong())
        }
    }
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
