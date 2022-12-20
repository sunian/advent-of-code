package advent2022

import java.io.File

/** Day 20: Grove Positioning System */
fun main() {
    parseInput()
    part1()
    part2()
}

// wrap in a class so that we can compare instances instead of numeric value
private class NumWrapper(val value: Long)

private lateinit var input: List<NumWrapper>

private fun parseInput() {
    input = File("input.txt").readLines().map { NumWrapper(it.toLong()) }
}

private fun part1() {
    val arraylist = ArrayList(input)
    input.forEach(arraylist::mix)
    val zeroPos = arraylist.indexOfFirst { it.value == 0L }
    println(
        arrayOf(1000, 2000, 3000).sumOf { offset ->
            arraylist[(zeroPos + offset).mod(arraylist.size)].value
        }
    )
}

private fun part2() {
    val decryptionKey = 811589153L
    val input = input.map { NumWrapper(it.value * decryptionKey) }
    val arraylist = ArrayList(input)
    repeat(10) {
        input.forEach(arraylist::mix)
    }
    val zeroPos = arraylist.indexOfFirst { it.value == 0L }
    println(
        arrayOf(1000, 2000, 3000).sumOf { offset ->
            arraylist[(zeroPos + offset).mod(arraylist.size)].value
        }
    )
}

private fun ArrayList<NumWrapper>.mix(n: NumWrapper) {
    val newIndex = (this.indexOf(n) + n.value).mod(this.size - 1)
    this.remove(n)
    this.add(newIndex, n)
}