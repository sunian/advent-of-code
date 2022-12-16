package advent2022

import findShortestPaths
import java.io.File
import java.lang.Integer.max

/** Day 16: Proboscidea Volcanium */
fun main() {
    parseInput()
    part1()
    part2()
}

private const val START_VALVE = "(AA)"
private val flowRates = hashMapOf<String, Int>()
private val allShortestPaths = hashMapOf<String, Map<String, Int>>()

/** memoization for finding max flow via dynamic programming */
private val memoized = hashMapOf<String, Int>()

private fun parseInput() {
    val directPaths = hashMapOf<String, List<String>>()
    File("input.txt").readLines()
        .forEach {
            val split = it.replace("has flow rate=", "")
                .replace("; tunnels lead to valves", "")
                .replace("; tunnel leads to valve", "")
                .replace(",", "")
                .replace("Valve ", "")
                .split(" ")
            val name = "(${split[0]})"
            val flowRate = split[1].toInt()
            val connected = split.drop(2).toHashSet()
            flowRates[name] = flowRate
            directPaths[name] = connected.map { "($it)" }
        }
    // valves with 0 flow rate are irrelevant except as stepping stones to other valves
    val relevantValves = flowRates.filter { (name, flowRate) ->
        name == START_VALVE || flowRate > 0
    }.keys
    // precompute the shortest paths from each relevant valve to each other relevant valve
    relevantValves.forEach { valve ->
        val shortestPaths = findShortestPaths(
            start = valve,
            getAdjacentNodes = { name -> directPaths.getValue(name) },
            getEdgeWeight = { _, _ -> 1 }
        )
        allShortestPaths[valve] = shortestPaths
            .filter { relevantValves.contains(it.key) }
            .mapValues { it.value.cost.toInt() }
    }
}

private fun part1() {
    memoized.clear()
    println(
        // use dynamic programming to find the max flow
        findMaxFlow(
            currentValve = START_VALVE,
            closedValves = allShortestPaths.keys.sorted().joinToString(""),
            currentFlowRate = 0,
            totalFlowSoFar = 0,
            timeLeft = 30
        )
    )
}

private fun part2() {
    // Each valve only needs to be opened by me or the elephant.
    // We need to find the optimal way for use to split the work.
    // Recursively check all possible ways to split up the valves such that
    // I open some of them, and the elephant opens the remaining ones.
    // Given a set of valves that I will open, I can independently calculate the total flow from the valves I open,
    // and then calculate the total flow from the valves the elephant opens,
    // and then add them together to get the total flow.

    // Start by arbitrarily picking a valve for me to open. This cuts the search space in half.
    val pickOne = allShortestPaths.keys.last()
    println(
        // recurse through all ways of assigning the valves
        recurseThruAssignments(
            unassigned = allShortestPaths.keys.toList() - pickOne,
            forMeToOpen = setOf(pickOne),
            forElephantToOpen = emptySet()
        )
    )
}

private fun findMaxFlow(
    currentValve: String,
    closedValves: String, // the names of currently closed valves, concatenated together, like "(AA)(BB)(DD)"
    currentFlowRate: Int,
    totalFlowSoFar: Int,
    timeLeft: Int
): Int {
    if (timeLeft < 1 || closedValves.isEmpty()) {
        return totalFlowSoFar + currentFlowRate * timeLeft
    }
    val key = "$closedValves|$currentValve|$currentFlowRate|$totalFlowSoFar|$timeLeft"
    memoized[key]?.let {
        return it
    }
    val shortestPathsFromCurrent = allShortestPaths.getValue(currentValve)
    val valvesToOpen = closedValves.chunked(4).filter { it != currentValve }
    val answer = when {
        closedValves.contains(currentValve) && flowRates.getValue(currentValve) > 0 -> findMaxFlow(
            currentValve = currentValve,
            closedValves = closedValves.replace(currentValve, ""),
            currentFlowRate = currentFlowRate + flowRates.getValue(currentValve),
            totalFlowSoFar = totalFlowSoFar + currentFlowRate,
            timeLeft = timeLeft - 1
        )

        valvesToOpen.isNotEmpty() -> valvesToOpen.maxOf { nextValve ->
            val cost = shortestPathsFromCurrent.getValue(nextValve)
            when {
                timeLeft < cost -> totalFlowSoFar + currentFlowRate * timeLeft
                else -> findMaxFlow(
                    currentValve = nextValve,
                    closedValves = closedValves,
                    currentFlowRate = currentFlowRate,
                    totalFlowSoFar = totalFlowSoFar + currentFlowRate * cost,
                    timeLeft = timeLeft - cost
                )

            }
        }

        else -> totalFlowSoFar + currentFlowRate * timeLeft
    }

    memoized[key] = answer
    return answer
}


private fun recurseThruAssignments(
    unassigned: List<String>,
    forMeToOpen: Set<String>,
    forElephantToOpen: Set<String>
): Int {
    if (unassigned.isEmpty()) {
        return when {
            forMeToOpen.size > 2 && forElephantToOpen.size > 2 -> { // work load is reasonably balanced
                memoized.clear()
                // add the flow from my valves and the elephant's valves
                findMaxFlow(
                    currentValve = START_VALVE,
                    closedValves = forMeToOpen.sorted().joinToString(""),
                    currentFlowRate = 0,
                    totalFlowSoFar = 0,
                    timeLeft = 26
                ) + findMaxFlow(
                    currentValve = START_VALVE,
                    closedValves = forElephantToOpen.sorted().joinToString(""),
                    currentFlowRate = 0,
                    totalFlowSoFar = 0,
                    timeLeft = 26
                )
            }

            else -> 0 // ignore unbalanced assignment
        }
    }
    val assignment = unassigned.first()
    val newUnassigned = unassigned - assignment
    return max(
        recurseThruAssignments(newUnassigned, forMeToOpen + assignment, forElephantToOpen),
        recurseThruAssignments(newUnassigned, forMeToOpen, forElephantToOpen + assignment)
    )
}