package advent2022

import java.io.File
import kotlin.math.max

/** Day 19: Not Enough Minerals */
fun main() {
    parseInput()
    part1()
    part2()
}

private enum class ResourceTypes {
    ORE, CLAY, OBSIDIAN, GEODE
}

private class Robot(
    val outputType: Int,
    val costs: List<Int>
)

private class Blueprint(
    val id: Int,
    val robots: List<Robot>
)

private lateinit var blueprints: List<Blueprint>
private val memoize = hashMapOf<String, Int>() // memoization table for dynamic programming
private var mostGeodesSoFar = 0 // cache of the best solution found so far for pruning purposes

private val initialAmounts = ResourceTypes.values().map { 0 }
private val initialRobots = ResourceTypes.values().map {
    when (it) {
        ResourceTypes.ORE -> 1
        else -> 0
    }
}

private fun parseInput() {
    blueprints = File("input.txt").readLines()
        .map { line ->
            val split = line.replace("Blueprint ", "")
                .replace(" Each ", "")
                .replace("robot costs ", "")
                .split(":")
            val id = split.first().toInt()
            val robots = split.last().trimEnd('.').split(".")
                .map {
                    val robot = it.split(" ", limit = 2)
                    val type = ResourceTypes.valueOf(robot.first().uppercase()).ordinal
                    val costMap = robot.last().split(" and ").associate { cost ->
                        val (num, resource) = cost.split(" ")
                        ResourceTypes.valueOf(resource.uppercase()) to num.toInt()
                    }
                    Robot(
                        outputType = type,
                        costs = ResourceTypes.values().map { resource -> costMap[resource] ?: 0 }
                    )

                }
            Blueprint(id, robots)
        }
}

private fun part1() {
    println(
        "\n" + blueprints.sumOf {
            print("${it.id} ")
            memoize.clear()
            mostGeodesSoFar = 0
            it.id * maxPossibleGeodes(
                blueprint = it,
                minutesLeft = 24,
                currentResourceAmounts = initialAmounts,
                currentRobotCounts = initialRobots
            )
        }
    )
}

private fun part2() {
    println(
        "\n" + blueprints.take(3)
            .map {
                print("${it.id} ")
                memoize.clear()
                mostGeodesSoFar = 0
                maxPossibleGeodes(
                    blueprint = it,
                    minutesLeft = 32,
                    currentResourceAmounts = initialAmounts,
                    currentRobotCounts = initialRobots
                )
            }
            .reduce { acc, i -> acc * i }
    )
}

private fun maxPossibleGeodes(
    blueprint: Blueprint,
    minutesLeft: Int,
    currentResourceAmounts: List<Int>,
    currentRobotCounts: List<Int>
): Int {
    if (minutesLeft < 1) {
        mostGeodesSoFar = max(mostGeodesSoFar, currentResourceAmounts.last())
        return currentResourceAmounts.last()
    }
    val key = "$minutesLeft|${currentResourceAmounts.joinToString()}|${currentRobotCounts.joinToString()}"
    memoize[key]?.let { return it }
    // calculate how many geodes we'd have if just collected and constructed no more robots in the minutesLeft
    val justCollectGeodes = currentResourceAmounts.last() + currentRobotCounts.last() * minutesLeft
    // calculate the most possible geodes that could be collected in the minutesLeft
    // assuming that 1 new geode robot is constructed in every remaining minute (even if currently impossible)
    val mostPossibleGeodes = justCollectGeodes + minutesLeft * (minutesLeft - 1) / 2
    if (mostPossibleGeodes < mostGeodesSoFar) {
        // if this optimistic estimate is still less than the best solution we've seen so far,
        // then prune this branch
        return justCollectGeodes
    }
    val answer = blueprint.robots.filter { robot ->
        // filter on robots whose costs can be fulfilled by simply waiting on the already existing robots
        robot.costs.mapIndexed { index, cost -> cost == 0 || currentRobotCounts[index] > 0 }.all { it }
    }.maxOf { robot ->
        // for each robot that could be constructed, calculate how many minutes we'd need to wait
        // for enough resources to be collected to build this robot, plus the 1 minute it takes to build it
        val waitMinutes = robot.costs.mapIndexed { index, cost ->
            val needed = cost - currentResourceAmounts[index]
            when {
                needed <= 0 -> 0
                needed.mod(currentRobotCounts[index]) == 0 -> needed / currentRobotCounts[index]
                else -> needed / currentRobotCounts[index] + 1
            }
        }.max() + 1
        when {
            // if there is enough time remaining to build this robot, then jump forward in time by [waitMinutes]
            waitMinutes <= minutesLeft -> maxPossibleGeodes(
                blueprint = blueprint,
                minutesLeft = minutesLeft - waitMinutes,
                currentResourceAmounts = currentResourceAmounts.mapIndexed { index, currentAmount ->
                    currentAmount - robot.costs[index] + currentRobotCounts[index] * waitMinutes
                },
                currentRobotCounts = currentRobotCounts.mapIndexed { index, currentCount ->
                    when (index) {
                        robot.outputType -> currentCount + 1
                        else -> currentCount
                    }
                }
            )
            // otherwise, just collect geodes using existing robots in the remaining time
            else -> justCollectGeodes
        }
    }
    memoize[key] = answer
    return answer
}