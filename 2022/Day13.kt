package advent2022

import bucketize
import toPair
import java.io.File

/** Day 13: Distress Signal */
fun main() {
    parseInput()
    part1()
    part2()
}

private lateinit var input: List<Pair<Packet, Packet>>

private fun parseInput() {
    input = File("input.txt").readLines()
        .bucketize(delimiter = "", allowEmptyBuckets = false)
        .map { it.map { Packet.parse(it) } }
        .map { it.toPair() }
}

private fun part1() {
    println(
        input.mapIndexed { index, pair ->
            when {
                pair.first < pair.second -> index + 1
                else -> 0
            }
        }.sum()
    )
}

private fun part2() {
    val dividers = listOf(
        Packet.parse("[[2]]"),
        Packet.parse("[[6]]")
    )
    val fullList = input.flatMap { it.toList() } + dividers
    val sorted = fullList.sorted()
    println(
        dividers.map { sorted.indexOf(it) + 1 }
            .reduce { acc, i -> acc * i }
    )
}

private sealed class Packet : Comparable<Packet> {
    class Scalar(val n: Int) : Packet() {
        override fun toString(): String = n.toString()
    }

    class Vector(val list: List<Packet>) : Packet() {
        override fun toString(): String = list.toString()
    }

    companion object {
        fun parse(s: String): Packet = parse(StringReader(s))

        private fun parse(reader: StringReader): Packet = when {
            reader.isNext("[") -> { // start reading a vector
                val list = arrayListOf<Packet>()
                reader.readNext()
                // read until hit a close bracket
                while (!reader.isNext("]")) {
                    list.add(parse(reader))
                }
                // consume the close bracket
                reader.readNext()
                Vector(list)
            }
            else -> { // start reading a scalar
                val sb = StringBuilder()
                // read until hit a comma or close bracket
                while (!reader.isNext(",]")) {
                    sb.append(reader.readNext())
                }
                Scalar(sb.toString().toInt())
            }
        }.also { // also consume next comma if available
            if (reader.isNext(",")) {
                reader.readNext()
            }
        }
    }

    fun wrap(): Vector = when (this) {
        is Scalar -> Vector(listOf(this))
        is Vector -> this
    }

    override fun compareTo(other: Packet): Int = when {
        this is Scalar && other is Scalar -> this.n.compareTo(other.n)
        this is Vector && other is Vector -> {
            this.list.mapIndexed { index, packet ->
                when {
                    index > other.list.lastIndex -> 1
                    else -> packet.compareTo(other.list[index])
                }
            }.reduceOrNull { acc, i ->
                when (acc) {
                    0 -> i
                    else -> acc
                }
            }.takeIf { it != 0 }
                ?: this.list.size.compareTo(other.list.size)
        }
        else -> this.wrap().compareTo(other.wrap())
    }

}

private class StringReader(val input: String) {
    var pos = 0
    fun readNext(): Char = input[pos++]
    fun isNext(chars: String): Boolean = when {
        pos > input.lastIndex -> false
        else -> chars.contains(input[pos])
    }
}