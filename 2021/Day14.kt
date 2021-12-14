package advent2021

import java.io.File

/** Day 14: Extended Polymerization */
fun main() {
    parseInput()
    part1()
    part2()
}

private val map = hashMapOf<String, String>()
private var initialPolymer = ""
private val initialPairCount = hashMapOf<String, Long>()
private fun parseInput() {
    File("input.txt").forEachLine { line ->
        if (line.isEmpty()) {
            return@forEachLine
        }
        if (line.contains("->")) {
            val (pair, insert) = line.split(" -> ")
            map[pair] = insert
        } else {
            // representation for part 1
            initialPolymer = line
            // representation for part 2
            initialPairCount[line.first().toString()] = 1
            initialPairCount[line.last().toString()] = 1
            line.forEachIndexed { index, c ->
                if (index < line.length - 1) {
                    initialPairCount.addNum(line.substring(index, index + 2), 1)
                }
            }
        }
    }
}

private fun part2() {
    var pairCount: MutableMap<String, Long> = initialPairCount
    repeat(40) {
        pairCount = pairCount.applyRules()
    }
    val counts = hashMapOf<Char, Long>()
    pairCount.forEach { (pair, count) ->
        pair.forEach { c -> counts.addNum(c, count) }
    }
    println((counts.values.maxOrNull()!! - counts.values.minOrNull()!!) / 2)
}

private fun part1() {
    var polymer: String = initialPolymer
    repeat(10) {
        polymer = polymer.applyRules()
    }
    polymer.toCharArray().toHashSet().map { c -> polymer.countCopies(c) }.let { counts ->
        println(counts.maxOrNull()!! - counts.minOrNull()!!)
    }
}

private fun String.applyRules(): String {
    val sb = StringBuilder()
    this.forEachIndexed { index, c ->
        sb.append(c)
        if (index < this.length - 1) {
            map[this.substring(index, index + 2)]?.let {
                sb.append(it)
            }
        }
    }
    return sb.toString()
}

private fun MutableMap<String, Long>.applyRules(): MutableMap<String, Long> {
    val newPairs = hashMapOf<String, Long>()
    map.forEach { (pair, c) ->
        val count = this[pair] ?: return@forEach
        newPairs.addNum("${pair.first()}$c", count)
        newPairs.addNum("$c${pair.last()}", count)
        this.remove(pair)
    }
    this.forEach { (pair, count) -> newPairs.addNum(pair, count) }
    return newPairs
}