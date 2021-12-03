package advent2021

import java.io.File

/** Day 3: Binary Diagnostic */
fun main() {
    part2()
}

private fun part2() {
    val lines = File("input.txt").readLines()
    val oxygen = calculate(lines, 0) { ones, zeroes ->
        when {
            ones >= zeroes -> '1'
            else -> '0'
        }
    }
    val co2 = calculate(lines, 0) { ones, zeroes ->
        when {
            ones >= zeroes -> '0'
            else -> '1'
        }
    }
    println(oxygen * co2)
}

private fun calculate(lines: List<String>, position: Int, block: (Int, Int) -> Char): Int {
    if (lines.size == 1) {
        return lines.first().toInt(2)
    }
    var ones = 0
    lines.forEach { if (it[position] == '1') ones++ }
    val zeroes = lines.size - ones
    val target = block(ones, zeroes)
    return calculate(lines.filter { it[position] == target }, position + 1, block)
}

private fun part1() {
    val ones = mutableListOf<Int>()
    val zeroes = mutableListOf<Int>()
    File("input.txt").forEachLine { line ->
        if (ones.isEmpty()) {
            repeat(line.length) {
                ones.add(0)
                zeroes.add(0)
            }
        }
        line.forEachIndexed { index, c ->
            when (c) {
                '0' -> zeroes[index]++
                '1' -> ones[index]++
            }
        }
    }
    var gamma = ""
    var epsilon = ""
    ones.forEachIndexed { index, _ ->
        if (ones[index] > zeroes[index]) {
            gamma += "1"
            epsilon += "0"
        } else {
            gamma += "0"
            epsilon += "1"
        }
    }
    println(gamma.toInt(2) * epsilon.toInt(2))
}