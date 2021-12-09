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
    this.toList().permutations { onNextPermutation(it.joinToString(separator = "")) }
}

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