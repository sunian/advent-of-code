package advent2021

import java.io.File

/** Day 2: Dive! */
fun main() {
    part2()
}

private fun part2() {
    var x = 0
    var y = 0
    var aim = 0
    File("input.txt").forEachLine { line ->
        val input = line.split(" ")
        val amount = input[1].toInt()
        when (input[0]) {
            "forward" -> {
                x += amount
                y += amount * aim
            }
            "down" -> aim += amount
            "up" -> aim -= amount
        }
    }
    println(x * y)
}

private fun part1() {
    var x = 0
    var y = 0
    File("input.txt").forEachLine { line ->
        val input = line.split(" ")
        val amount = input[1].toInt()
        when (input[0]) {
            "forward" -> x += amount
            "down" -> y += amount
            "up" -> y -= amount
        }
    }
    println(x * y)
}