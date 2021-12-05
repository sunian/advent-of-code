package advent2021

import java.io.File
import kotlin.math.max
import kotlin.math.min

/** Day 5: Hydrothermal Venture */
fun main() {
    parseInput()
    part2()
}

private val lines = arrayListOf<IntArray>()
private val points = hashMapOf<String, Int>()

private fun parseInput() {
    File("input.txt").forEachLine { line ->
        val newLine = line.split(" -> ")
            .map { it.split(",") }
            .flatten()
            .map { it.toInt() }
            .toIntArray()
        lines.add(newLine)
    }
}

private fun part2() {
    var count = 0
    lines.forEach { line ->
        val (x1, y1, x2, y2) = line
        var x = x1
        var y = y1
        val stepX = x2.compareTo(x1)
        val stepY = y2.compareTo(y1)
        while (true) {
            val key = "$x,$y"
            points[key] = (points[key] ?: 0) + 1
            if (points[key]!! == 2) {
                count++
            }
            if (x == x2 && y == y2) break
            x += stepX
            y += stepY
        }
    }
    println(count)
}

private fun part1() {
    var count = 0
    lines.forEach { line ->
        val (x1, y1, x2, y2) = line
        if (x1 == x2) {
            for (y in min(y1, y2)..max(y1, y2)) {
                val key = "$x1,$y"
                points[key] = (points[key] ?: 0) + 1
                if (points[key]!! == 2) {
                    count++
                }
            }
        } else if (y1 == y2) {
            for (x in min(x1, x2)..max(x1, x2)) {
                val key = "$x,$y1"
                points[key] = (points[key] ?: 0) + 1
                if (points[key]!! == 2) {
                    count++
                }
            }
        }
    }
    println(count)
}