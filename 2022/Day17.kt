package advent2022

import minus
import plus
import x
import y
import java.io.File
import kotlin.math.max

/** Day 17: Pyroclastic Flow */
fun main() {
    parseInput()
    part1()
    part2()
}

private lateinit var jetPattern: String
private val blocks = listOf(
    listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0), // -
    listOf(0 to 1, 1 to 0, 1 to 1, 1 to 2, 2 to 1), //+
    listOf(0 to 0, 1 to 0, 2 to 0, 2 to 1, 2 to 2), //_|
    listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3), // |
    listOf(0 to 0, 0 to 1, 1 to 0, 1 to 1), // #
)
private val gravity = 0 to -1

private fun parseInput() {
    jetPattern = File("input.txt").readLines().first()
}

private fun part1() {
    println(simulateBlocks(2022) { _, _, _, _ -> /* no-op */ })
}

private fun part2() {
    // find a pattern where the jetIndex lines up with the first block
    // and the towerHeight is a linear function of the number of blocks set
    var periodicJetIndex = 0
    val data = arrayListOf<Pair<Int, Int>>()
    val increment: Pair<Int, Int>
    while (true) {
        data.clear()
        simulateBlocks(jetPattern.length * blocks.size) { blocksSet, jetIndex, blockIndex, towerHeight ->
            if (jetIndex == periodicJetIndex && blockIndex == 0) {
                data.add(blocksSet to towerHeight)
            }
        }
        val increments = data.mapIndexed { index, pair ->
            when (index) {
                0 -> pair
                else -> pair - data[index - 1]
            }
        }.drop(2)
        // check that there are many data points and that the increments are all the same
        if (increments.size > 3 && increments.toSet().size == 1) {
            increment = increments.first()
            break
        }
        periodicJetIndex++
    }
    val iterations = 1000000000000L
    val initial = data.last { it.first <= increment.first } // initial data point gives the starting offsets
    val targetMod = (iterations - initial.first).mod(increment.first)
    val modData = arrayListOf<Pair<Int, Int>>()
    simulateBlocks(jetPattern.length * blocks.size) { blocksSet, _, _, towerHeight ->
        if ((blocksSet - initial.first).mod(increment.first) == targetMod) {
            modData.add(blocksSet to towerHeight)
        }
    }
    // calculate the height differences with the initial data set
    val heightDifferences = modData.drop(2).map { dataPoint ->
        val previousDataPoint = data.last { it.first <= dataPoint.first }
        dataPoint.second - previousDataPoint.second
    }.toSet()
    if (heightDifferences.size != 1) {
        throw IllegalStateException("height differences were not uniform")
    }
    val heightDifference = heightDifferences.first()
    println(
        ((iterations - initial.first - targetMod) / increment.first) * increment.second
                + initial.second
                + heightDifference
    )
}

private fun simulateBlocks(
    iterations: Int,
    callback: (blocksSet: Int, jetIndex: Int, blockIndex: Int, towerHeight: Int) -> Unit
): Int {
    var jetIndex = 0
    var blockIndex = 0
    val grid = hashSetOf<Pair<Int, Int>>()
    repeat(7) {
        grid.add(it to -1)
    }
    var towerHeight = 0
    repeat(iterations) {
        callback(it, jetIndex, blockIndex, towerHeight)
        val blockOrigin = 2 to towerHeight + 3
        var block = blocks[blockIndex].map { it + blockOrigin }
        blockIndex = (blockIndex + 1).mod(blocks.size)
        while (true) {
            val jet = when (jetPattern[jetIndex]) {
                '<' -> -1 to 0
                else -> 1 to 0
            }
            jetIndex = (jetIndex + 1).mod(jetPattern.length)
            // apply jet
            block = (block.map { it + jet })
                .takeIf { grid.canPutBlock(it) } ?: block
            // apply gravity
            val fallenBlock = block.map { it + gravity }
            if (grid.canPutBlock(fallenBlock)) {
                block = fallenBlock
            } else {
                grid.addBlock(block)
                towerHeight = max(
                    towerHeight,
                    block.maxOf { it.y } + 1
                )
                break
            }
        }
    }
    return towerHeight
}

private fun HashSet<Pair<Int, Int>>.addBlock(block: List<Pair<Int, Int>>) {
    block.forEach { this.add(it) }
}

private val hRange = 0..6
private fun HashSet<Pair<Int, Int>>.canPutBlock(block: List<Pair<Int, Int>>): Boolean =
    block.all { cell ->
        (cell.x in hRange) && !this.contains(cell)
    }