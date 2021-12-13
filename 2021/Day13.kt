package advent2021

import java.io.File

/** Day 13: Transparent Origami */
fun main() {
    parseInput()
    part2()
}

private var dots = hashSetOf<Pair<Int, Int>>()
private val folds = mutableListOf<Pair<Char, Int>>()

private fun parseInput() {
    File("input.txt").forEachLine { line ->
        if (line.isEmpty()) {
            return@forEachLine
        }
        if (line.startsWith("fold along")) {
            val (instruction, number) = line.split("=")
            folds.add(instruction.last() to number.toInt())
        } else {
            val (x, y) = line.split(",").map { it.toInt() }
            dots.add(x to y)
        }
    }
}

private fun part2() {
    folds.forEach { it.executeFold() }
    println(dots.toDotGrid())
}

private fun part1() {
    folds.first().executeFold()
    println(dots.size)
}

private fun Pair<Char, Int>.executeFold() {
    val line = this.second
    if (this.first == 'x') {
        dots = dots.map { (x, y) ->
            if (x <= line) {
                x to y
            } else {
                (line - (x - line)) to y
            }
        }.toHashSet()
    } else {
        dots = dots.map { (x, y) ->
            if (y <= line) {
                x to y
            } else {
                x to (line - (y - line))
            }
        }.toHashSet()
    }
}