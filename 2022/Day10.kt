package advent2022

import java.io.File
import kotlin.math.abs

/** Day 10: Cathode-Ray Tube */
fun main() {
    parseInput()
    val signalStrengths = getSignalStrengths()
    part1(signalStrengths)
    part2(signalStrengths)
}

private lateinit var instructions: List<String>

private fun parseInput() {
    instructions = File("input.txt").readLines()
}

private fun getSignalStrengths(): List<Int> {
    val values = arrayListOf(1)
    instructions.forEach { line ->
        values.add(values.last())
        if (line.contains(' ')) {
            values.add(values.last() + line.split(" ")[1].toInt())
        }
    }
    return values
}

private fun part1(signalStrengths: List<Int>) {
    println(
        arrayOf(20, 60, 100, 140, 180, 220)
            .sumOf { it * signalStrengths[it - 1] }
    )
}

private fun part2(signalStrengths: List<Int>) {
    repeat(6) { row ->
        repeat(40) { col ->
            val i = row * 40 + col
            if (abs(col - signalStrengths[i]) < 2) {
                print("##")
            } else {
                print(".`")
            }
        }
        println()
    }
}