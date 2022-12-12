import kotlin.math.*

/*
Iterable.toPair, toTriple : Convert an Iterable to a Pair or a Triple
Iterable.bucketize : Convert an Iterable of elements into a List of buckets of said elements, using a given heuristic
permutations : Calculate all the permutations of a List, Array, or String
Iterable.countCopies, String.countCopies: count how many times a given element appears
Iterable.histogram : count how many times every element appears in an Iterable.
Iterable.times: Compute the cross product of 2 or 3 Iterables as a Sequence

IntRange.intersects
IntRange.isAdjacent
IntRange.intersection
IntRange.contains
IntRange.size
IntRange.plus

Grid operations (a grid is a List of Lists) :
emptyGrid : create an empty mutable grid
forEachInGrid : perform an action for each cell in a grid
getCell : return the item in a cell, or a default value if the input position is out of bounds
adjacentOffsets : offsets for 4 cells adjacent to a cell (up, down, left, right)
adjacentAndDiagonalOffsets : offsets for 8 cells adjacent to a cell (includes diagonals)
adjacent4Cells : returns the 4 cells adjacent to the cell (using adjacentOffsets)
adjacent8Cells : returns the 8 cells adjacent to the cell (using adjacentAndDiagonalOffsets)
adjacent9Cells : returns the 9 cells adjacent to the cell (including the input cell itself)
toDotGrid : pretty print a grid of solid/empty cells
gridToString : pretty print a grid given a mapping
Pair.row, Pair.col : treat a Pair as a grid position

Operations for 3D vectors and matrices:
typealias Vector3Int and Matrix3Int
Identity : the identity matrix
mapIndexed : transform a Triple as if it were a list
Vector3Int.sum : sum 3 components of a vector
Pair.x, Pair.y : treat a Pair as an (x, y) coordinate
Triple.x, Triple.y, Triple.z : treat a Triple as an (x, y, z) coordinate
Pair.plus, Pair.minus : arithmetic on 2 Pairs of Ints
Vector3Int.plus, Vector3Int.minus : arithmetic on 2 vectors
matrixRotateX, matrixRotateY, matrixRotateZ : returns a rotation matrix of a given angle in radians
Matrix3Int.times : multiply a matrix by another matrix or a vector
matrixString : pretty print a matrix
Rotations3D : all rotation matrices of 90 degree increments

findShortestPath
findShortestPathCost
findShortestPaths

greatestCommonFactor
leastCommonMultiple
*/

fun <T> Iterable<T>.toPair(): Pair<T, T> = iterator().let { it.next() to it.next() }
fun <T> Iterable<T>.toTriple(): Triple<T, T, T> = iterator().let { Triple(it.next(), it.next(), it.next()) }

fun <T> Iterable<T>.bucketize(
    allowEmptyBuckets: Boolean = false,
    callback: (
        currentBucket: List<T>,
        newElement: T,
        addToCurrentBucket: (T) -> Unit,
        startNewBucket: () -> Unit
    ) -> Unit
): List<List<T>> {
    var currentBucket = arrayListOf<T>()
    val buckets = arrayListOf<List<T>>(currentBucket)
    val addToCurrentBucket: (T) -> Unit = { currentBucket.add(it) }
    val startNewBucket: () -> Unit = {
        if (allowEmptyBuckets || currentBucket.isNotEmpty()) {
            currentBucket = arrayListOf()
            buckets.add(currentBucket)
        }
    }
    this.forEach { callback(currentBucket, it, addToCurrentBucket, startNewBucket) }
    return buckets
}

fun <T> Iterable<T>.bucketize(bucketSize: Int): List<List<T>> =
    this.bucketize { currentBucket, newElement, addToCurrentBucket, startNewBucket ->
        if (currentBucket.size >= bucketSize) {
            startNewBucket()
        }
        addToCurrentBucket(newElement)
    }

fun <T> Iterable<T>.bucketize(delimiter: T, allowEmptyBuckets: Boolean): List<List<T>> =
    this.bucketize(allowEmptyBuckets) { _, newElement, addToCurrentBucket, startNewBucket ->
        when (newElement) {
            delimiter -> startNewBucket()
            else -> addToCurrentBucket(newElement)
        }
    }

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

fun <T> List<List<T>>.getCell(pos: Pair<Int, Int>): T = this[pos.row][pos.col]
fun <T> List<List<List<T>>>.getCell(pos: Vector3Int): T = this[pos.x][pos.y][pos.z]

val adjacentOffsets = arrayOf(
    -1 to 0,
    0 to -1,
    0 to 1,
    1 to 0,
)
val adjacentAndDiagonalOffsets = arrayOf(
    *adjacentOffsets,
    -1 to -1,
    -1 to 1,
    1 to -1,
    1 to 1,
)

fun adjacent4Cells(row: Int, col: Int): List<Pair<Int, Int>> {
    val source = row to col
    return adjacentOffsets.map { source + it }
}

fun adjacent8Cells(row: Int, col: Int): List<Pair<Int, Int>> {
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

fun <T> Iterable<T>.countCopies(element: T) = this.count { it == element }
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

fun <T> Iterable<T>.histogram(): Map<T, Int> {
    val histogram = hashMapOf<T, Int>()
    this.forEach { histogram.addNum(it, 1) }
    return histogram
}

operator fun <T, U> Iterable<T>.times(other: Iterable<U>): Sequence<Pair<T, U>> = sequence {
    this@times.forEach { t ->
        other.forEach { u ->
            yield(t to u)
        }
    }
}

operator fun <T, U, V> Sequence<Pair<T, U>>.times(other: Iterable<V>): Sequence<Triple<T, U, V>> = sequence {
    this@times.forEach { (t, u) ->
        other.forEach { v ->
            yield(Triple(t, u, v))
        }
    }
}

fun IntRange.intersects(other: IntRange): Boolean = last >= other.first && other.last >= first
fun IntRange.isAdjacent(other: IntRange): Boolean = last == other.first - 1 || other.last == first - 1
fun IntRange.intersection(other: IntRange) = first.coerceAtLeast(other.first)..last.coerceAtMost(other.last)
fun IntRange.contains(other: IntRange) = first <= other.first && last >= other.last
fun IntRange.size(): Int = last + 1 - first
operator fun IntRange.plus(other: IntRange) = first.coerceAtMost(other.first)..last.coerceAtLeast(other.last)

fun greatestCommonFactor(a: Long, b: Long): Long {
    val n = abs(a)
    val m = abs(b)
    var x = max(n, m)
    var y = min(n, m)
    var r = x.mod(y)
    while (r != 0L) {
        x = y
        y = r
        r = x.mod(y)
    }
    return y
}

fun leastCommonMultiple(a: Long, b: Long) = abs(a) * (abs(b) / greatestCommonFactor(a, b))