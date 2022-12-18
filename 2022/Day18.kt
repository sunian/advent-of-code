package advent2022

import Vector3Int
import adjacentOffsets3D
import get
import plus
import toTriple
import java.io.File
import kotlin.math.abs

/** Day 18: Boiling Boulders */
fun main() {
    parseInput()
    part1()
    part2()
}

private lateinit var cubes: Set<Vector3Int>

private fun parseInput() {
    cubes = File("input.txt").readLines()
        .map {
            it.split(",")
                .map(String::toInt)
                .toTriple()
        }.toSet()
}

private fun part1() {
    val touching = cubes.sumOf { cube ->
        cubes.count(cube::isAdjacent)
    }
    println(cubes.size * 6 - touching)
}

private fun part2() {
    // define the bounds of the search space
    val minCorner = (0..2).map { dimension ->
        cubes.minOf { it[dimension] } - 1
    }.toTriple()
    val maxCorner = (0..2).map { dimension ->
        cubes.maxOf { it[dimension] } + 1
    }.toTriple()
    val outside = hashSetOf<Vector3Int>() // will be the set of all positions of outside air
    // perform BFS
    val queue = arrayListOf(minCorner, maxCorner)
    while (queue.isNotEmpty()) {
        val outsidePosition = queue.removeAt(0)
        outside.add(outsidePosition)
        queue.addAll(
            outsidePosition.adjacentCubes()
                .filter { adjacentCube ->
                    val isLessThanMax = (0..2).all { adjacentCube[it] <= maxCorner[it] }
                    val isGreaterThanMin = (0..2).all { adjacentCube[it] >= minCorner[it] }
                    isLessThanMax && isGreaterThanMin
                            && !cubes.contains(adjacentCube)
                            && !outside.contains(adjacentCube)
                            && !queue.contains(adjacentCube)
                }
        )
    }
    // for each cube, count how many of its faces are touching the outside air
    val surface = cubes.sumOf { cube ->
        cube.adjacentCubes().count(outside::contains)
    }
    println(surface)
}

private fun Vector3Int.adjacentCubes() = adjacentOffsets3D.map { this + it }

private fun Vector3Int.isAdjacent(other: Vector3Int): Boolean {
    var countAdjacent = 0
    var countEqual = 0
    repeat(3) { index ->
        val i = this[index]
        val j = other[index]
        when (abs(i - j)) {
            1 -> countAdjacent++
            0 -> countEqual++
        }
    }
    return countAdjacent == 1 && countEqual == 2
}