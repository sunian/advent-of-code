package advent2021

import java.io.File
import kotlin.math.abs

/** Day 19: Beacon Scanner */
fun main() {
    parseInput()
    part1()
    part2()
}

private val scanners = arrayListOf<Scanner>()

private fun parseInput() {
    File("input.txt").forEachLine { line ->
        if (line.isEmpty()) {
            return@forEachLine
        }
        if (line.contains("scanner")) {
            scanners.add(Scanner())
            return@forEachLine
        }
        val nums = line.split(",").map { it.toInt() }
        scanners.last().localBeacons.add(Vector3Int(nums[0], nums[1], nums[2]))
    }
}

private fun part1() {
    val correctScanners = arrayListOf<Scanner>()
    correctScanners.add(scanners.removeAt(0))
    while (scanners.isNotEmpty()) {
        val correctedScanner = scanners.first { scanner ->
            correctScanners.any { correctScanner ->
                Rotations3D.any { rotation ->
                    scanner.rotation = rotation
                    val potentialTranslations = scanner.getPotentialTranslations(correctScanner)
                    val duplicates = potentialTranslations.histogram().filterValues { it > 1 }.keys
                    duplicates.any { translation ->
                        scanner.translation = translation
                        val myBeacons = scanner.getAbsoluteBeacons()
                        val correctBeacons = correctScanner.getAbsoluteBeacons()
                        myBeacons.count { it in correctBeacons } >= 12
                    }
                }
            }
        }
        scanners.remove(correctedScanner)
        correctScanners.add(correctedScanner)
    }
    val beacons = hashSetOf<Vector3Int>()
    correctScanners.forEach { scanner ->
        scanner.getAbsoluteBeacons().forEach { beacon ->
            beacons.add(beacon)
        }
    }
    println(beacons.size)
    scanners.addAll(correctScanners)
}

private fun part2() {
    // assume all scanners have been correctly rotated and translated from part 1
    val maxDistance = scanners.maxOf { scanner1 ->
        scanners.maxOf { scanner2 ->
            val p1 = scanner1.translation
            val p2 = scanner2.translation
            abs(p1.x - p2.x) +
                    abs(p1.y - p2.y) +
                    abs(p1.z - p2.z)
        }
    }
    println(maxDistance)
}

private class Scanner {
    var localBeacons = arrayListOf<Vector3Int>()
    var rotation: Matrix3Int = Identity
    var translation: Vector3Int = Vector3Int(0, 0, 0)

    private fun getRotatedBeacons(): List<Vector3Int> = localBeacons.map { local ->
        rotation * local
    }

    fun getAbsoluteBeacons(): List<Vector3Int> = localBeacons.map { local ->
        rotation * local + translation
    }

    fun getPotentialTranslations(other: Scanner): List<Vector3Int> {
        val myBeacons = getRotatedBeacons()
        return other.getAbsoluteBeacons().flatMap { otherBeacon ->
            myBeacons.map { myBeacon -> otherBeacon - myBeacon }
        }
    }
}