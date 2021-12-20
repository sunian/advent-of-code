package advent2021

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

fun <T> List<T>.permutations(onNextPermutation: (List<T>) -> Boolean) {
    if (this.size < 2) {
        onNextPermutation(this)
        return
    }
    this.forEachIndexed { index, last ->
        val sublist = this.subList(0, index) + this.subList(index + 1, this.size)
        var stop = false
        sublist.permutations {
            val result = onNextPermutation(it + last)
            if (!result) {
                stop = true
            }
            result
        }
        if (stop) {
            return
        }
    }
}

inline fun <reified T> Array<T>.permutations(crossinline onNextPermutation: (Array<T>) -> Boolean) {
    this.toList().permutations { onNextPermutation(it.toTypedArray()) }
}

fun String.permutations(onNextPermutation: (String) -> Boolean) {
    this.toList().permutations {
        onNextPermutation(it.joinToString(separator = ""))
    }
}

fun <T> emptyGrid() = mutableListOf<MutableList<T>>()

fun <T> List<List<T>>.forEachInGrid(action: (row: Int, col: Int, cell: T) -> Unit) {
    this.forEachIndexed { r, row -> row.forEachIndexed { c, cell -> action(r, c, cell) } }
}

fun <T> List<List<T>>.getCell(row: Int, col: Int, default: T): T {
    if (row < 0 || col < 0) return default
    if (row >= this.size) return default
    val list = this[row]
    if (col >= list.size) return default
    return list[col]
}

private val adjacentOffsets = arrayOf(
    -1 to 0,
    0 to -1,
    0 to 1,
    1 to 0,
)
private val adjacentAndDiagonalOffsets = arrayOf(
    *adjacentOffsets,
    -1 to -1,
    -1 to 1,
    1 to -1,
    1 to 1,
)

fun adjacentCells(row: Int, col: Int): List<Pair<Int, Int>> {
    val source = row to col
    return adjacentOffsets.map { source + it }
}

fun adjacentAndDiagonalCells(row: Int, col: Int): List<Pair<Int, Int>> {
    val source = row to col
    return adjacentAndDiagonalOffsets.map { source + it }
}

fun adjacent9Cells(row: Int, col: Int): List<Pair<Int, Int>> {
    val source = row to col
    return (-1..1).flatMap { r -> (-1..1).map { c -> r to c } }
        .map { source + it }
}

typealias Vector3Int = Triple<Int, Int, Int>
typealias Matrix3Int = Array<IntArray>

val Identity: Matrix3Int = arrayOf(
    intArrayOf(1, 0, 0),
    intArrayOf(0, 1, 0),
    intArrayOf(0, 0, 1),
)

inline fun <R> Triple<R, R, R>.mapIndexed(transform: (index: Int, R) -> R): Triple<R, R, R> =
    Triple(
        transform(0, first),
        transform(1, second),
        transform(2, third),
    )

fun Vector3Int.sum(): Int = first + second + third

val <T> Pair<T, T>.x get() = this.first
val <T> Pair<T, T>.y get() = this.second
val <T> Pair<T, T>.row get() = this.first
val <T> Pair<T, T>.col get() = this.second
val <T> Triple<T, T, T>.x get() = this.first
val <T> Triple<T, T, T>.y get() = this.second
val <T> Triple<T, T, T>.z get() = this.third
operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> =
    this.copy(first + other.first, second + other.second)

operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>): Pair<Int, Int> =
    this.copy(first - other.first, second - other.second)

operator fun Vector3Int.plus(other: Vector3Int): Vector3Int =
    this.copy(first + other.first, second + other.second, third + other.third)

operator fun Vector3Int.minus(other: Vector3Int): Vector3Int =
    this.copy(first - other.first, second - other.second, third - other.third)

fun matrixRotateX(radians: Double): Matrix3Int = arrayOf(
    intArrayOf(1, 0, 0),
    intArrayOf(0, cos(radians).roundToInt(), -sin(radians).roundToInt()),
    intArrayOf(0, sin(radians).roundToInt(), cos(radians).roundToInt())
)

fun matrixRotateY(radians: Double): Matrix3Int = arrayOf(
    intArrayOf(cos(radians).roundToInt(), 0, sin(radians).roundToInt()),
    intArrayOf(0, 1, 0),
    intArrayOf(-sin(radians).roundToInt(), 0, cos(radians).roundToInt())
)

fun matrixRotateZ(radians: Double): Matrix3Int = arrayOf(
    intArrayOf(cos(radians).roundToInt(), -sin(radians).roundToInt(), 0),
    intArrayOf(sin(radians).roundToInt(), cos(radians).roundToInt(), 0),
    intArrayOf(0, 0, 1)
)

operator fun Matrix3Int.times(other: Matrix3Int): Matrix3Int =
    this.map { row ->
        row.mapIndexed { c, _ ->
            row.mapIndexed { i, n -> n * other[i][c] }.sum()
        }.toIntArray()
    }.toTypedArray()

operator fun Matrix3Int.times(other: Vector3Int): Vector3Int =
    other.mapIndexed { r, _ ->
        other.mapIndexed { c, n -> n * this[r][c] }.sum()
    }

fun Matrix3Int.matrixString(): String = joinToString("\n") { it.joinToString("\t") }

val Rotations3D: List<Matrix3Int> = arrayOf(
    intArrayOf(0, 0, 0),
    intArrayOf(0, 0, 1),
    intArrayOf(0, 0, 2),
    intArrayOf(0, 0, 3),
    intArrayOf(0, 1, 0),
    intArrayOf(0, 1, 1),
    intArrayOf(0, 1, 2),
    intArrayOf(0, 1, 3),
    intArrayOf(0, 2, 0),
    intArrayOf(0, 2, 1),
    intArrayOf(0, 2, 2),
    intArrayOf(0, 2, 3),
    intArrayOf(0, 3, 0),
    intArrayOf(0, 3, 1),
    intArrayOf(0, 3, 2),
    intArrayOf(0, 3, 3),
    intArrayOf(1, 0, 0),
    intArrayOf(1, 0, 1),
    intArrayOf(1, 0, 2),
    intArrayOf(1, 0, 3),
    intArrayOf(1, 2, 0),
    intArrayOf(1, 2, 1),
    intArrayOf(1, 2, 2),
    intArrayOf(1, 2, 3),
).map { (x, y, z) ->
    matrixRotateX(x * PI / 2) *
            matrixRotateY(y * PI / 2) *
            matrixRotateZ(z * PI / 2)
}

fun <T> Collection<T>.countCopies(element: T) = this.count { it == element }
fun String.countCopies(element: Char) = this.count { it == element }

fun Collection<Pair<Int, Int>>.toDotGrid(): String {
    val rangeX = minOf { it.x }..maxOf { it.x }
    val rangeY = minOf { it.y }..maxOf { it.y }
    return rangeY.map { y -> rangeX.map { x -> this.contains(x to y) } }
        .toDotGrid(solidValue = true)
}

fun <T> List<List<T>>.toDotGrid(solidValue: T): String =
    gridToString { if (it == solidValue) "##" else ".`" }

fun <T> List<List<T>>.gridToString(transform: (T) -> String): String =
    this.joinToString(separator = "\n") { row ->
        row.joinToString(separator = "", transform = transform)
    }

fun <K> MutableMap<K, Long>.addNum(key: K, increment: Long) {
    this[key] = (this[key] ?: 0) + increment
}

fun <K> MutableMap<K, Int>.addNum(key: K, increment: Int) {
    this[key] = (this[key] ?: 0) + increment
}

fun <K> MutableMap<K, Double>.addNum(key: K, increment: Double) {
    this[key] = (this[key] ?: 0.0) + increment
}

fun <K, V> MutableMap<K, List<V>>.addListElement(key: K, value: V) {
    this[key] = (this[key] ?: emptyList()) + value
}

fun <K, V> MutableMap<K, Set<V>>.addSetElement(key: K, value: V) {
    this[key] = (this[key] ?: emptySet()) + value
}

data class ShortestPath<Node>(val cost: Long, val prev: Node)

/** Find the total length of the shortest path from [start] to [end] in a graph. */
fun <Node> findShortestPathCost(
    start: Node,
    end: Node,
    getAdjacentNodes: (Node) -> Collection<Node>,
    getEdgeWeight: (from: Node, to: Node) -> Long
): Long {
    val shortestPaths = findShortestPaths(start, getAdjacentNodes, getEdgeWeight)
    return shortestPaths[end]!!.cost
}

/** Find the list of nodes that forms the shortest path from [start] to [end] in a graph. */
fun <Node> findShortestPath(
    start: Node,
    end: Node,
    getAdjacentNodes: (Node) -> Collection<Node>,
    getEdgeWeight: (from: Node, to: Node) -> Long
): List<Node> {
    val shortestPaths = findShortestPaths(start, getAdjacentNodes, getEdgeWeight)
    val path = arrayListOf(end)
    while (start !in path) {
        path.add(shortestPaths[path.last()]!!.prev)
    }
    path.reverse()
    return path
}

/** Find the shortest path to all nodes in a graph, starting from [start]. */
fun <Node> findShortestPaths(
    start: Node,
    getAdjacentNodes: (Node) -> Collection<Node>,
    getEdgeWeight: (from: Node, to: Node) -> Long
): Map<Node, ShortestPath<Node>> {
    var nodes = listOf(start)
    val shortestPaths = hashMapOf(start to ShortestPath(0, start))
    while (nodes.isNotEmpty()) {
        nodes = nodes.flatMap { src ->
            getAdjacentNodes(src).flatMap { dst ->
                val edge = getEdgeWeight(src, dst)
                val newCost = shortestPaths[src]!!.cost + edge
                if (newCost < (shortestPaths[dst]?.cost ?: Long.MAX_VALUE)) {
                    shortestPaths[dst] = ShortestPath(newCost, src)
                    listOf(dst)
                } else {
                    emptyList()
                }
            }
        }
    }
    return shortestPaths
}

fun <T> List<T>.histogram(): Map<T, Int> {
    val histogram = hashMapOf<T, Int>()
    this.forEach { histogram.addNum(it, 1) }
    return histogram
}