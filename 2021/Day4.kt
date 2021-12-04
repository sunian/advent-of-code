package advent2021

import java.io.File
import java.util.regex.Pattern
import kotlin.system.exitProcess

private typealias Board = Array<IntArray>

private val regex = Pattern.compile("\\s+")

/** Day 4: Giant Squid */
fun main() {
    parseInput()
    part2()
}

private var numbersCalled = listOf<Int>()
private val boards = arrayListOf<Board>()

private fun part2() {
    var lastBoardIndex = -1
    numbersCalled.forEach { n ->
        boards.forEach {
            it.markOff(n)
        }
        if (lastBoardIndex < 0) {
            if (boards.count { !it.isWinning() } == 1) {
                lastBoardIndex = boards.indexOfFirst { !it.isWinning() }
            }
        } else {
            if (boards[lastBoardIndex].isWinning()) {
                println(boards[lastBoardIndex].calcScore() * n)
                exitProcess(0)
            }
        }
    }
}

private fun part1() {
    numbersCalled.forEach { n ->
        boards.forEach {
            it.markOff(n)
            if (it.isWinning()) {
                println(it.calcScore() * n)
                exitProcess(0)
            }
        }
    }
}

private fun parseInput() {
    var tempBoard = Board(5) { intArrayOf() }
    var row = 0
    File("input.txt").forEachLine { line ->
        if (numbersCalled.isEmpty()) {
            numbersCalled = line.split(",").map { it.toInt() }
        } else {
            if (line.isEmpty()) {
                if (row > 0) {
                    boards.add(tempBoard)
                }
                row = 0
                tempBoard = Board(5) { intArrayOf() }
            } else {
                tempBoard[row] = line.trim().split(regex).map { it.toInt() }.toIntArray()
                row++
            }
        }
    }
    if (row > 0) {
        boards.add(tempBoard)
    }
}

private fun Board.isWinning(): Boolean {
    val fullRow = this.any { it.all { it < 0 } }
    if (fullRow) return true
    val fullCol = (0 until this.size).any { i ->
        this.map { it[i] }.all { it < 0 }
    }
    if (fullCol) return true
    val fullDiag = this.mapIndexed { index, row -> row[index] }.all { it < 0 }
    if (fullDiag) return true
    val fullDiag2 = this.mapIndexed { index, row -> row[row.size - index - 1] }.all { it < 0 }
    return fullDiag2
}

private fun Board.calcScore(): Int = this.sumBy { it.filter { it >= 0 }.sum() }

private fun Board.markOff(numCalled: Int) {
    this.forEach {
        it.forEachIndexed { index, n ->
            if (n == numCalled) {
                it[index] = -1
            }
        }
    }
}