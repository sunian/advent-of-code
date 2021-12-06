package advent2021

import java.io.File

/** Day 6: Lanternfish */
fun main() {
    parseInput()
    part2()
}

var initialAges = arrayListOf<Int>()
private fun parseInput() {
    val ages = File("input.txt").readLines()[0].split(",").map { it.toInt() }
    initialAges.addAll(ages)
}

private fun part2() {
    var ages: List<Long> = arrayListOf<Long>(0, 0, 0, 0, 0, 0, 0, 0, 0)
        .apply { initialAges.forEach { this[it]++ } }
    repeat(256) {
        ages = ages.mapIndexed { index, _ ->
            when (index) {
                6 -> ages[7] + ages[0]
                8 -> ages[0]
                else -> ages[index + 1]
            }
        }
    }
    println(ages.sum())
}

private fun part1() {
    val ages = initialAges
    repeat(80) {
        val size = ages.size
        (0 until size).forEach { i ->
            val age = ages[i]
            if (age > 0) {
                ages[i] = age - 1
            } else {
                ages[i] = 6
                ages.add(8)
            }
        }
    }
    println(ages.size)
}