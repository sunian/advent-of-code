package advent2021

import java.io.File

/** Day 8: Seven Segment Search */
fun main() {
    parseInput()
    part2()
}

private val input = arrayListOf<Segment>()
private fun parseInput() {
    File("input.txt").forEachLine { line ->
        line.split(" | ").let {
            input.add(
                Segment(
                    inputs = it[0].trim().split(" ").map { it.sort() },
                    outputs = it[1].trim().split(" ").map { it.sort() },
                )
            )
        }
    }
}

private fun part2() {
    var count = 0
    input.forEach { segment ->
        val mappings = Array(10) { "" }
        segment.inputs.forEach { s ->
            when (s.length) {
                2 -> mappings[1] = s
                3 -> mappings[7] = s
                4 -> mappings[4] = s
                7 -> mappings[8] = s
            }
        }
        segment.inputs.forEach { s ->
            when (s.length) {
                5 -> when {
                    s.containsAll(mappings[1]) -> mappings[3] = s
                    s.contains3(mappings[4]) -> mappings[5] = s
                    else -> mappings[2] = s
                }
                6 -> when {
                    s.containsAll(mappings[4]) -> mappings[9] = s
                    s.containsAll(mappings[1]) -> mappings[0] = s
                    else -> mappings[6] = s
                }
            }
        }
        val string = segment.outputs.joinToString(separator = "") { mappings.indexOf(it).toString() }
        count += string.toInt()
    }
    println(count)
}

private fun part1() {
    var count = 0
    input.forEach { segment ->
        count += segment.outputs.count {
            when (it.length) {
                2, 3, 4, 7 -> true
                else -> false
            }
        }
    }
    println(count)
}

private class Segment(
    val inputs: List<String>,
    val outputs: List<String>,
)

private fun String.sort(): String = String(this.toCharArray().sortedArray())
private fun String.containsAll(other: String): Boolean = other.all { this.contains(it) }
private fun String.contains3(other: String): Boolean = other.count { this.contains(it) } == 3

/*
1 cf
7 acf
4 bcdf
3 acdfg all cf
5 abdfg has bdf
2 acdeg only cd
9 abcdfg all bcdf
0 abcefg all cf
6 abdefg no cf
8 abcdefg
*/