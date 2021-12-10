package advent2021

import java.io.File

/** Day 10: Syntax Scoring */
fun main() {
    part1()
    part2()
}

private val open = listOf('(', '[', '{', '<')
private val close = listOf(')', ']', '}', '>')

private fun part2() {
    val input = File("input.txt").readLines()
    val scores = arrayListOf<Long>()
    input.forEach { line ->
        val stack = arrayListOf<Char>()
        for (c in line) {
            if (c in open) {
                stack.add(c)
            } else {
                val pop = stack.removeLastOrNull()
                if (pop == null || open.indexOf(pop) != close.indexOf(c)) {
                    return@forEach
                }
            }
        }
        var score = 0L
        stack.reversed().forEach { c ->
            score = (score * 5) + (open.indexOf(c) + 1)
        }
        scores.add(score)
    }
    println(scores.sorted()[scores.size / 2])
}

private fun part1() {
    val input = File("input.txt").readLines()
    val points = listOf(3, 57, 1197, 25137)
    var total = 0L
    input.forEach { line ->
        val stack = arrayListOf<Char>()
        for (c in line) {
            if (c in open) {
                stack.add(c)
            } else {
                val i = close.indexOf(c)
                val pop = stack.removeLastOrNull()
                if (pop == null || open.indexOf(pop) != i) {
                    total += points[i]
                    return@forEach
                }
            }
        }
    }
    println(total)
}