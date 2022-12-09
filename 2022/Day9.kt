package advent2022

import java.io.File
import kotlin.math.abs
import kotlin.math.sign

/** Day 9: Rope Bridge */
fun main() {
    parseInput()
    countTailPositions(numKnots = 2)
    countTailPositions(numKnots = 10)
}

private lateinit var moves: List<Move>

private class Move(val xOffset: Int, val yOffset: Int, val distance: Int)

private fun parseInput() {
    moves = File("input.txt").readLines()
        .map {
            val split = it.split(" ")
            var xOffset = 0
            var yOffset = 0
            when (split[0]) {
                "U" -> yOffset = 1
                "D" -> yOffset = -1
                "L" -> xOffset = -1
                "R" -> xOffset = 1
            }
            Move(xOffset, yOffset, distance = split[1].toInt())
        }
}

private fun countTailPositions(numKnots: Int) {
    val tailPositions = hashSetOf<String>()
    val xPos = IntArray(numKnots) { 0 }
    val yPos = IntArray(numKnots) { 0 }
    moves.forEach { move ->
        repeat(move.distance) {
            xPos[0] += move.xOffset
            yPos[0] += move.yOffset
            for (i in 1 until numKnots) {
                val dX = xPos[i - 1] - xPos[i]
                val dY = yPos[i - 1] - yPos[i]
                if (abs(dX) > 1 || abs(dY) > 1) {
                    xPos[i] += dX.sign
                    yPos[i] += dY.sign
                }
            }
            tailPositions.add("${xPos.last()},${yPos.last()}")
        }
    }
    println(tailPositions.size)
}