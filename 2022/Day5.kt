package advent2022

import Vector3Int
import bucketize
import java.io.File

/** Day 5: Supply Stacks */
fun main() {
    parseInput()
    part1()
    parseInput()
    part2()
}

private lateinit var stacks: List<ArrayList<Char>>
private lateinit var moves: List<Vector3Int>

private fun parseInput() {
    val inputSplit = File("input.txt").readLines()
        .bucketize(delimiter = "", allowEmptyBuckets = false)
    val stackInput = inputSplit[0].dropLast(1)
    val numStacks = (stackInput.maxOf { it.length } + 1) / 4
    stacks = (1..numStacks).map { arrayListOf() }
    stackInput.forEach { line ->
        repeat(numStacks) { i ->
            line[i * 4 + 1]
                .takeUnless { it.isWhitespace() }
                ?.let { stacks[i].add(it) }
        }
    }
    moves = inputSplit[1]
        .map {
            val split = it.split(" ")
            val num = split[1].toInt()
            val from = split[3].toInt()
            val to = split[5].toInt()
            Vector3Int(num, from - 1, to - 1)
        }
}

private fun part1() {
    moves.forEach { (num, from, to) ->
        repeat(num) {
            stacks[to].add(0, stacks[from].removeAt(0))
        }
    }
    println(
        stacks.map { it.first() }
            .joinToString(separator = "")
    )
}

private fun part2() {
    moves.forEach { (num, from, to) ->
        stacks[to].addAll(0, stacks[from].subList(0, num))
        repeat(num) {
            stacks[from].removeAt(0)
        }
    }
    println(
        stacks.map { it.first() }
            .joinToString(separator = "")
    )
}