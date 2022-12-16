package advent2022

import java.io.File
import java.lang.Integer.min
import java.lang.Integer.max

/** Day 16: Proboscidea Volcanium */
fun main() {
    parseInput()
    part1()
    part2()
}

/** WeightMapping[src][dst] = the distance from valve [src] to valve [dst] */
typealias WeightMapping = HashMap<String, HashMap<String, Int>>

private const val START_VALVE = "(AA)"
private val flowRates = hashMapOf<String, Int>()
private val initialWeightMapping = hashMapOf<String, HashMap<String, Int>>()

/** memoization for finding max flow via dynamic programming */
private val memoized = hashMapOf<String, Int>()

private fun parseInput() {
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
            initialWeightMapping[name] = connected.map { "($it)" to 1 }.toMap(hashMapOf())
        }

    initialWeightMapping.keys.filter { flowRates[it] == 0 }.forEach { uselessValve ->
        initialWeightMapping.removeValve(uselessValve)
    }
}

private fun part1() {
    memoized.clear()
    println(
        // use dynamic programming to find the max flow
        findMaxFlow(
            weightMapping = initialWeightMapping,
            currentValve = START_VALVE,
            closedValves = initialWeightMapping.keys.sorted().joinToString(""),
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
    val pickOne = initialWeightMapping.keys.last()
    println(
        // recurse through all ways of assigning the valves
        recurseThruAssignments(
            unassigned = initialWeightMapping.keys.toList() - pickOne,
            forMeToOpen = setOf(pickOne),
            forElephantToOpen = emptySet()
        )
    )
}

private fun findMaxFlow(
    weightMapping: WeightMapping,
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

    var answer = weightMapping.getValue(currentValve).maxOf { (nextValve, weight) ->
        when {
            timeLeft < weight -> totalFlowSoFar + currentFlowRate * timeLeft
            else -> findMaxFlow(
                weightMapping = weightMapping,
                currentValve = nextValve,
                closedValves = closedValves,
                currentFlowRate = currentFlowRate,
                totalFlowSoFar = totalFlowSoFar + currentFlowRate * weight,
                timeLeft = timeLeft - weight
            )
        }
    }
    if (closedValves.contains(currentValve)) {
        answer = max(
            answer,
            findMaxFlow(
                weightMapping = weightMapping,
                currentValve = currentValve,
                closedValves = closedValves.replace(currentValve, ""),
                currentFlowRate = currentFlowRate + flowRates.getValue(currentValve),
                totalFlowSoFar = totalFlowSoFar + currentFlowRate,
                timeLeft = timeLeft - 1
            )
        )
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
            forMeToOpen.size > 5 && forElephantToOpen.size > 5 -> { // work load is reasonably balanced
                memoized.clear()
                // add the flow from my valves and the elephant's valves
                findMaxFlow(
                    weightMapping = initialWeightMapping.withoutValves(forElephantToOpen),
                    currentValve = START_VALVE,
                    closedValves = forMeToOpen.sorted().joinToString(""),
                    currentFlowRate = 0,
                    totalFlowSoFar = 0,
                    timeLeft = 26
                ) + findMaxFlow(
                    weightMapping = initialWeightMapping.withoutValves(forMeToOpen),
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

/** remove a valve from the mapping and collapse the weights */
private fun WeightMapping.removeValve(valveToRemove: String) {
    if (valveToRemove == START_VALVE) return
    val weightsToRemove = this.getValue(valveToRemove)
    this.filter { (name, weights) -> name !== valveToRemove && weights.containsKey(valveToRemove) }
        .forEach { (valveToUpdate, weights) ->
            val priorWeight = weights.getValue(valveToRemove)
            weightsToRemove.forEach { (valve, weight) ->
                if (valve != valveToUpdate) {
                    this.getValue(valveToUpdate)[valve] = min(
                        priorWeight + weight,
                        this.getValue(valveToUpdate)[valve] ?: 999
                    )
                }
            }
            this.getValue(valveToUpdate).remove(valveToRemove)
        }
    this.remove(valveToRemove)
}

/** return a new mapping that excludes the [valvesToRemove] */
private fun WeightMapping.withoutValves(valvesToRemove: Set<String>): WeightMapping {
    val newMapping = hashMapOf<String, HashMap<String, Int>>()
    this.forEach {
        newMapping[it.key] = HashMap(it.value)
    }
    valvesToRemove.forEach(newMapping::removeValve)
    return newMapping
}