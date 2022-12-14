package advent2022

import plus
import toPair
import x
import y
import java.io.File
import kotlin.math.max
import kotlin.math.sign

/** Day 14: Regolith Reservoir */
fun main() {
    parseInput()
    part1()
    parseInput()
    part2()
}

private lateinit var paths: List<List<Pair<Int, Int>>>
private val grid = hashSetOf<Pair<Int, Int>>()
private var maxX = 0
private var maxY = 0

private fun parseInput() {
    grid.clear()
    paths = File("input.txt").readLines()
        .map {
            it.split(" -> ")
                .map {
                    it.split(",")
                        .map { it.toInt() }
                        .toPair()
                        .also {
                            maxX = max(maxX, it.x)
                            maxY = max(maxY, it.y)
                        }
                }
        }
    paths.forEach { path ->
        path.forEachIndexed { index, point ->
            if (index > 0) {
                val previousPoint = path[index - 1]
                val step = (point.x - previousPoint.x).sign to (point.y - previousPoint.y).sign
                var current = previousPoint
                while (current != point) {
                    grid.add(current)
                    current += step
                }
                grid.add(point)
            }
        }
    }
}

private fun part1() {
    var sandCount = 0
    while (true) {
        var sandX = 500
        var sandY = 0
        while (sandY < maxY) {
            when {
                !grid.contains(sandX to sandY + 1) -> sandY++
                !grid.contains(sandX - 1 to sandY + 1) -> {
                    sandX--
                    sandY++
                }

                !grid.contains(sandX + 1 to sandY + 1) -> {
                    sandX++
                    sandY++
                }

                else -> {
                    grid.add(sandX to sandY)
                    break
                }
            }
        }
        if (sandY >= maxY) {
            break
        }
        sandCount++
    }
    println(sandCount)
}

private fun part2() {
    var sandCount = 0
    while (!grid.contains(500 to 0)) {
        var sandX = 500
        var sandY = 0
        while (true) {
            when {
                sandY == maxY + 1 -> {
                    grid.add(sandX to sandY)
                    break
                }

                !grid.contains(sandX to sandY + 1) -> sandY++
                !grid.contains(sandX - 1 to sandY + 1) -> {
                    sandX--
                    sandY++
                }

                !grid.contains(sandX + 1 to sandY + 1) -> {
                    sandX++
                    sandY++
                }

                else -> {
                    grid.add(sandX to sandY)
                    break
                }
            }
        }
        sandCount++
    }
    println(sandCount)
}