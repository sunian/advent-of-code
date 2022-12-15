package advent2022

import plus
import times
import x
import y
import java.io.File
import kotlin.math.abs

/** Day 15: Beacon Exclusion Zone */
fun main() {
    parseInput()
    part1()
    part2()
}

/** List of Pairs of sensors and beacons */
private lateinit var input: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>

private fun parseInput() {
    input = File("input.txt").readLines()
        .map {
            val split = it.replace(Regex("[,=xy:]"), "")
                .split(" ")
            (split[2].toInt() to split[3].toInt()) to (split[8].toInt() to split[9].toInt())
        }
}

private fun part1() {
    val target = 2000000
    val emptyPositions = hashSetOf<Pair<Int, Int>>()
    input.forEach { (sensor, beacon) ->
        val distance = manhattanDistance(sensor, beacon)
        val vertical = abs(sensor.y - target)
        val horizontal = distance - vertical
        if (horizontal >= 0) {
            for (x in (sensor.x - horizontal)..(sensor.x + horizontal)) {
                val pos = x to target
                if (pos != beacon) {
                    emptyPositions.add(pos)
                }
            }
        }
    }
    println(emptyPositions.size)
}

private val multipliers = arrayOf(
    1 to 1,
    1 to -1,
    -1 to 1,
    -1 to -1,
)

/** For each sensor, let D = the distance to the nearest beacon.
 * Scan every position at distance (D + 1) from the sensor.
 * The distress beacon must be at one of these locations.
 */
private fun part2() {
    val bounds = 4000000L
    val potentialAnswers = hashSetOf<Long>()
    input.forEach { (sensor, beacon) ->
        val scanningDistance = manhattanDistance(sensor, beacon) + 1
        for (horizontal in 0..scanningDistance) {
            val vertical = scanningDistance - horizontal
            multipliers.forEach { multiplier ->
                val pos = sensor + (horizontal to vertical) * multiplier
                if (pos.x in 0..bounds && pos.y in 0..bounds && canDistressBeaconBeAt(pos)) {
                    potentialAnswers.add(pos.x * bounds + pos.y)
                }
            }
        }
    }
    println(potentialAnswers)
}

private fun canDistressBeaconBeAt(pos: Pair<Int, Int>): Boolean = input.all { (sensor, beacon) ->
    when (pos) {
        beacon -> false
        else -> manhattanDistance(pos, sensor) > manhattanDistance(sensor, beacon)
    }
}

private fun manhattanDistance(from: Pair<Int, Int>, to: Pair<Int, Int>): Int =
    abs(from.x - to.x) + abs(from.y - to.y)