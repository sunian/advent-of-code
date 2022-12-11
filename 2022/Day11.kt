package advent2022

import bucketize
import java.io.File

/** Day 11: Monkey in the Middle */
fun main() {
    parseInput()
    part1()
    parseInput()
    part2()
}

private lateinit var monkeys: List<Monkey>
private var productOfDivisors: Long = 0

private class Monkey(
    val items: MutableList<Long> = arrayListOf(),
    var operation: (Long) -> Long,
    val testDivisor: Long,
    val trueDest: Int,
    val falseDest: Int,
    var inspectionCount: Long = 0
) {
    fun takeTurn() {
        inspectionCount += items.size
        items.map(operation)
            .map { it.mod(productOfDivisors) } // optimization for part 2
            .forEach { item ->
                val destIndex = if (item.mod(testDivisor) == 0L) trueDest else falseDest
                monkeys[destIndex].items.add(item)
            }
        items.clear()
    }
}

private fun parseInput() {
    monkeys = File("input.txt").readLines()
        .bucketize(delimiter = "", allowEmptyBuckets = false)
        .map { lines ->
            val operationDesc = lines[2].split(" ")
            val operator = operationDesc[operationDesc.lastIndex - 1]
            val opNum = operationDesc.last()
            Monkey(
                operation = { n ->
                    val num = when (opNum) {
                        "old" -> n
                        else -> opNum.toLong()
                    }
                    when (operator) {
                        "*" -> n * num
                        "+" -> n + num
                        else -> error("unrecognized: $operator")
                    }
                },
                testDivisor = lines[3].split(" ").last().toLong(),
                trueDest = lines[4].split(" ").last().toInt(),
                falseDest = lines[5].split(" ").last().toInt(),
            ).apply {
                items.addAll(
                    lines[1].replace("  Starting items: ", "")
                        .split(", ")
                        .map(String::toLong)
                )
            }
        }
    productOfDivisors = monkeys.map { it.testDivisor }
        .reduce { acc, n -> acc * n }
}

private fun part1() {
    monkeys.forEach {
        val originalOperation = it.operation
        // wrap each monkey's operation with division by 3
        it.operation = { n -> originalOperation(n) / 3 }
    }
    println(getLevelOfMonkeyBusiness(rounds = 20))

}

private fun part2() {
    println(getLevelOfMonkeyBusiness(rounds = 10000))
}

private fun getLevelOfMonkeyBusiness(rounds: Int): Long {
    repeat(rounds) {
        monkeys.forEach(Monkey::takeTurn)
    }
    val inspectionCounts = monkeys.map { it.inspectionCount }
        .sortedDescending()
    return inspectionCounts[0] * inspectionCounts[1]
}
