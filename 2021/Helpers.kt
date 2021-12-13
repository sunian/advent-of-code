package advent2021

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

val adjacentOffsets = arrayOf(
    -1 to -1,
    -1 to 0,
    -1 to 1,
    0 to -1,
    0 to 1,
    1 to -1,
    1 to 0,
    1 to 1,
)

fun adjacentCells(row: Int, col: Int): List<Pair<Int, Int>> {
    return adjacentOffsets.map { (r, c) -> row + r to col + c }
}

val <T> Pair<T, T>.x get() = this.first
val <T> Pair<T, T>.y get() = this.second
val <T> Triple<T, T, T>.x get() = this.first
val <T> Triple<T, T, T>.y get() = this.second
val <T> Triple<T, T, T>.z get() = this.third

fun <T> Collection<T>.countCopies(element: T) = this.count { it == element }

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