package advent2021

import advent2021.day16.InputBuffer
import advent2021.day16.Packet
import java.io.File

/** Day 16: Packet Decoder */
fun main() {
    parseInput()
    part1()
    part2()
}

private lateinit var rootPacket: Packet
private fun parseInput() {
    val input = InputBuffer.parse(File("input.txt").readLines()[0])
    rootPacket = Packet.parse(input)
}

private fun part2() {
    println(rootPacket.computeValue())
}

private fun part1() {
    println(rootPacket.reduce(initial = 0) { acc, packet -> acc + packet.version })
}