package advent2021

/** Day 23: Amphipod */
fun main() {
    part1()
    part2()
}

private var roomSize = 0

private fun part2() {
    cache.clear()
    roomSize = 4
    val start = BurrowState(
        listOf(
            Amphipod(10, 1 to 2),
            Amphipod(100, 4 to 2),
            Amphipod(100, 1 to 4),
            Amphipod(1000, 4 to 4),
            Amphipod(1, 1 to 6),
            Amphipod(1000, 4 to 6),
            Amphipod(10, 1 to 8),
            Amphipod(1, 4 to 8),
            Amphipod(1000, 2 to 2),
            Amphipod(1000, 3 to 2),
            Amphipod(100, 2 to 4),
            Amphipod(10, 3 to 4),
            Amphipod(10, 2 to 6),
            Amphipod(1, 3 to 6),
            Amphipod(1, 2 to 8),
            Amphipod(100, 3 to 8),
        ).sortedBy { it.location.toString() }
    )
    println(findMinCost(start))
}

private fun part1() {
    cache.clear()
    roomSize = 2
    val start = BurrowState(
        listOf(
            Amphipod(10, 1 to 2),
            Amphipod(100, 2 to 2),
            Amphipod(100, 1 to 4),
            Amphipod(1000, 2 to 4),
            Amphipod(1, 1 to 6),
            Amphipod(1000, 2 to 6),
            Amphipod(10, 1 to 8),
            Amphipod(1, 2 to 8),
        ).sortedBy { it.location.toString() }
    )
    println(findMinCost(start))
}

private val cache = hashMapOf<BurrowState, Int>()
private fun findMinCost(state: BurrowState): Int {
    cache[state]?.let {
        return it
    }
    if (state.isEndState()) {
        return 0
    }
    val legalNextStates = state.getLegalNextStates()
    if (legalNextStates.isEmpty()) {
        cache[state] = Int.MAX_VALUE
        return Int.MAX_VALUE
    }
    var result = legalNextStates.minOf { newState ->
        val minCost = findMinCost(newState)
        if (minCost < Int.MAX_VALUE) {
            newState.getLastMoved().cost * newState.getLastMoved().distanceMoved + minCost
        } else {
            minCost
        }
    }
    cache[state]?.let {
        result = result.coerceAtMost(it)
    }
    cache[state] = result
    return result
}

private data class Amphipod(
    val cost: Int,
    val location: Pair<Int, Int>,
    val isMoving: Boolean = false,
) {
    var distanceMoved: Int = 0
}

private data class BurrowState(
    val pods: List<Amphipod>
) {

    fun getLastMoved() = pods.first { it.isMoving }

    fun isEndState(): Boolean =
        (pods.all { it.location.row > 0 }) &&
                (pods.all { it.cost == costs[it.location.col] })

    fun getLegalNextStates(): List<BurrowState> {
        val nextStates = arrayListOf<BurrowState>()
        pods.forEach { pod ->
            val startPoint = pod.location
            val endpoints = hashMapOf<Pair<Int, Int>, Int>()
            endpoints[startPoint] = 0
            while (true) {
                val newEndpoints = hashMapOf<Pair<Int, Int>, Int>()
                endpoints.forEach { (location, distance) ->
                    location.getAdjacentLocations().forEach { endpoint ->
                        if (pods.none { it != pod && it.location == endpoint }) {
                            if (!endpoints.contains(endpoint)) {
                                newEndpoints[endpoint] = distance + 1
                            }
                        }
                    }
                }
                if (newEndpoints.isEmpty()) {
                    break
                }
                endpoints.putAll(newEndpoints)
            }
            endpoints.filter { (endpoint, distance) ->
                val startInHall = startPoint.row == 0
                val endInHall = endpoint.row == 0
                val startInRoom = startPoint.row > 0
                val endInRoom = endpoint.row > 0
                val startInCorrectCol = costs[startPoint.col] == pod.cost
                val endInCorrectCol = costs[endpoint.col] == pod.cost
                if (distance == 0) {
                    // must move at least 1 space
                    return@filter false
                }
                if (endInHall && endpoint.col in sideRoomColumns) {
                    // do not end above a room door
                    return@filter false
                }
                if (endInRoom && !endInCorrectCol) {
                    // do not enter an incorrect room
                    return@filter false
                }
                if (startInHall && endInHall) {
                    // do not wander the hallway
                    return@filter false
                }
                if (startInRoom && startInCorrectCol && // if we are already in the correct room
                    (endpoint.row < startPoint.row || endpoint.col != startPoint.col) // do not exit
                    // unless there is another incorrect pod in this room
                    && pods.none { it.location.col == startPoint.col && it.cost != pod.cost }
                ) {
                    return@filter false
                }
                if (pods.filter { it.location.row > 0 && it.location.col == endpoint.col }
                        .any { it.cost != pod.cost }) {
                    return@filter false // do not enter a room with an incorrect pod in it
                }
                true
            }.forEach { (endpoint, distance) ->
                nextStates.add(
                    BurrowState(
                        pods.map { oldPod ->
                            if (oldPod == pod) {
                                oldPod.copy(
                                    location = endpoint,
                                    isMoving = true,
                                ).apply { distanceMoved = distance }
                            } else {
                                oldPod.copy(isMoving = false)
                            }
                        }.sortedBy { it.location.toString() }
                    )
                )
            }
        }
        return nextStates
    }

}

private val sideRoomColumns = arrayOf(2, 4, 6, 8)
private val costs = Array(11) { 0 }.apply {
    this[2] = 1
    this[4] = 10
    this[6] = 100
    this[8] = 1000
}

private fun Pair<Int, Int>.getAdjacentLocations(): List<Pair<Int, Int>> =
    adjacent4Cells(row, col).filter { (r, c) ->
        when {
            r < 0 || c < 0 || c > 10 || r > roomSize -> false
            r == 0 -> true
            else -> c in sideRoomColumns
        }
    }