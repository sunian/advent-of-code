package advent2021

import java.io.File

/** Day 9: Smoke Basin */
fun main() {
    parseInput()
    part2()
}

private val map = arrayListOf<List<Int>>()
private fun parseInput() {
    File("input.txt").forEachLine { line ->
        map.add(line.toCharArray().map { it.toString().toInt() })
    }
}

private fun part2() {
    val basins = arrayListOf<Int>()
    map.forEachInGrid { row, col, cell ->
        if (
            cell < getValue(row - 1, col) &&
            cell < getValue(row + 1, col) &&
            cell < getValue(row, col - 1) &&
            cell < getValue(row, col + 1)
        ) {
            basins.add(getBasinSize(hashSetOf(row to col)))
        }
    }
    println(basins.sortedDescending().take(3).reduce { acc, i -> acc * i })
}

private fun getBasinSize(points: HashSet<Pair<Int, Int>>): Int {
    while (true) {
        val newPoints = hashSetOf<Pair<Int, Int>>()
        points.forEach { point ->
            listOf(
                point.first - 1 to point.second,
                point.first + 1 to point.second,
                point.first to point.second - 1,
                point.first to point.second + 1,
            ).forEach { adjacent ->
                if (!points.contains(adjacent) && getValue(adjacent.first, adjacent.second) < 9) {
                    newPoints.add(adjacent)
                }
            }
        }
        if (newPoints.isEmpty()) {
            return points.size
        }
        points.addAll(newPoints)
    }
}

private fun part1() {
    var sum = 0
    map.forEachInGrid { row, col, cell ->
        if (
            cell < getValue(row - 1, col) &&
            cell < getValue(row + 1, col) &&
            cell < getValue(row, col - 1) &&
            cell < getValue(row, col + 1)
        ) {
            sum += (1 + cell)
        }
    }
    println(sum)
}

private fun getValue(row: Int, col: Int): Int = map.getCell(row, col, default = Int.MAX_VALUE)