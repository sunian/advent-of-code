package advent2021

import java.io.File

/** Day 1: Sonar Sweep */
fun main() {
    part2()
}

private fun part2() {
    val window = mutableListOf<Long>()
    var total = 0L
    var count = 0
    File("input.txt").forEachLine { line ->
        val depth = line.toLong()
        if (window.size < 3) {
            total += depth
        } else {
            val newTotal = total - window[0] + depth
            if (newTotal > total) {
                count++
            }
            total = newTotal
            window.removeAt(0)
        }
        window.add(depth)
    }
    println(count)
}

private fun part1() {
    var prev = Long.MAX_VALUE
    var count = 0
    File("input.txt").forEachLine { line ->
        val depth = line.toLong()
        if (depth > prev) {
            count++
        }
        prev = depth
    }
    println(count)
}