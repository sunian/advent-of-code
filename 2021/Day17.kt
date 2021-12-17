package advent2021

import kotlin.math.max
import kotlin.math.sign

/** Day 17: Trick Shot */
fun main() {
    part1()
    part2()
}

private val xRange = 32..65
private val yRange = -225..-177

private fun isInTarget(x: Int, y: Int) = x in xRange && y in yRange

private fun part1() {
    println((0..100000).maxOf { maxHeightFor(it) })
}

private fun part2() {
    val allValidDy = (-2000..2000).filter { maxHeightFor(it) >= 0 }
    println((0..100000).sumOf { dx ->
        allValidDy.count { dy -> willHitTarget(dx, dy) }
    })
}

private fun maxHeightFor(initialDy: Int): Int {
    var maxY = 0
    var y = 0
    var dy = initialDy
    while (y >= yRange.first) {
        y += dy
        dy--
        maxY = max(y, maxY)
        if (y in yRange) {
            return maxY
        }
    }
    return -1
}

private fun willHitTarget(initialDx: Int, initialDy: Int): Boolean {
    var x = 0
    var y = 0
    var dx = initialDx
    var dy = initialDy
    while (y >= yRange.first && x <= xRange.last) {
        x += dx
        y += dy
        dy--
        dx -= dx.sign
        if (isInTarget(x, y)) {
            return true
        }
    }
    return false
}