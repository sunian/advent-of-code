package advent2022

import java.io.File

/** Day 6: Tuning Trouble */
fun main() {
    parseInput()
    findMarker(size = 4)
    findMarker(size = 14)
}

private lateinit var buffer: String

private fun parseInput() {
    buffer = File("input.txt").readText()
}

private fun findMarker(size: Int) {
    val marker = buffer
        .windowed(size)
        .first { it.toSet().size == size }
    println(buffer.indexOf(marker) + size)
}