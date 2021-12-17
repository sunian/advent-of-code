package advent2021

import kotlin.math.sign

/** Day 17: Trick Shot */
fun main() {
    part1()
    part2()
}

private val xRange = 32..65
private val yRange = -225..-177
private const val MILLION = 1_000_000

private fun Pair<Int, Int>.isInTarget() = x in xRange && y in yRange

private fun part1() {
    println((0..MILLION).maxOf { maxHeightFor(it) })
}

private fun part2() {
    val allValidDy = (-MILLION..MILLION).filter { maxHeightFor(it) >= 0 }
    println((0..MILLION).sumOf { dx ->
        allValidDy.count { dy -> willHitTarget(dx, dy) }
    })
}

private fun maxHeightFor(initialDy: Int): Int {
    var y = 0
    var dy = when {
        initialDy > 0 -> -initialDy - 1
        else -> initialDy
    }
    while (y >= yRange.first) {
        y += dy
        dy--
        if (y in yRange) {
            return when {
                initialDy > 0 -> (initialDy * initialDy + initialDy) / 2
                else -> 0
            }
        }
    }
    return -1
}

private fun willHitTarget(initialDx: Int, initialDy: Int): Boolean {
    var position = 0 to 0
    var velocity = initialDx to initialDy
    while (position.y >= yRange.first && position.x <= xRange.last) {
        position += velocity
        velocity -= velocity.x.sign to 1
        if (position.isInTarget()) {
            return true
        }
    }
    return false
}