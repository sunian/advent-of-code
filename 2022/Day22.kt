package advent2022

import bucketize
import col
import emptyGrid
import getCell
import minus
import plus
import row
import times
import java.io.File
import kotlin.math.min

/**  */
fun main() {
    parseInput()
    part1()
    part2()
}

private lateinit var instructions: List<Int>
private val grid = emptyGrid<Char>()
private var cubeSize = 0
private var startRow = -1
private var startCol = -1

private const val RIGHT = 0
private const val DOWN = 1
private const val LEFT = 2
private const val UP = 3
private val directionNames = listOf("R", "D", "L", "U")
private val mapDirectionToOffset = listOf(
    0 to 1, // RIGHT
    1 to 0, // DOWN
    0 to -1, // LEFT
    -1 to 0 // UP
)

private fun parseInput() {
    File("input.txt").readLines()
        .bucketize(delimiter = "", allowEmptyBuckets = false)
        .let { (map, instr) ->
            instructions = instr.first().replace("RL", "")
                .replace("LR", "")
                .replace("R", " -3 ")
                .replace("L", " -1 ")
                .split(" ")
                .map { it.toInt() }
            val width = map.maxOf { it.length }
            map.forEach { line ->
                val row = (0 until width).map { i ->
                    when {
                        i <= line.lastIndex -> {
                            if (startRow < 0 && line[i] == '.') {
                                startRow = grid.size
                                startCol = i
                            }
                            line[i]
                        }

                        else -> ' '
                    }
                }
                grid.add(row.toMutableList())
            }
        }
    cubeSize = min(grid.size, grid.first().size) / 3
}

private fun part1() {
    performWalkAndPrintPassword(::getWrapPosition)
}

private fun part2() {
    performWalkAndPrintPassword(::getCubeWrapPosition)
}

private fun performWalkAndPrintPassword(
    getWrapPosition: (current: Pair<Int, Int>, dir: Int) -> Pair<Pair<Int, Int>, Int>
) {
    var pos = startRow to startCol
    var dir = 0
    instructions.forEach { inst ->
        when {
            inst < 0 -> dir = (dir + inst).mod(4)
            else -> repeat(inst) {
                val newPos = pos + mapDirectionToOffset[dir]
                when (grid.getCell(row = newPos.row, col = newPos.col, default = ' ')) {
                    '.' -> pos = newPos
                    '#' -> return@repeat
                    else -> {
                        val (wrapped, newDir) = getWrapPosition(pos, dir)
                        when (wrapped) {
                            pos -> return@repeat
                            else -> {
                                pos = wrapped
                                dir = newDir
                            }
                        }
                    }
                }
            }
        }
    }
    println(
        1000 * (pos.row + 1) + 4 * (pos.col + 1) + dir
    )
}

/* PART 1 */

/** we are currently at position [current] and would like to step in direction [dir]
 * but it would take us off the map. return our new position and direction after wrapping around the map.
 * if the new wrapped position contains a wall, then return the original position and direction instead.
 *  */
private fun getWrapPosition(current: Pair<Int, Int>, dir: Int): Pair<Pair<Int, Int>, Int> {
    val offset = mapDirectionToOffset[dir]
    var pos = current
    while (grid.getCell(row = pos.row, col = pos.col, default = ' ') != ' ') {
        pos -= offset
    }
    val newPos = pos + offset
    if (grid.getCell(newPos) == '#') {
        return current to dir
    }
    return newPos to dir
}

/* PART 2 */

/** we are currently at position [current] and would like to step in direction [dir]
 * but it would take us off the current face of the cube.
 * return our new position and direction after jumping to the adjacent face of the cube.
 * if the new position contains a wall, then return the original position and direction instead.
 *  */
private fun getCubeWrapPosition(current: Pair<Int, Int>, dir: Int): Pair<Pair<Int, Int>, Int> {
    // local position of [current], relative to the face origin
    val local = current.row.mod(cubeSize) to current.col.mod(cubeSize)
    var newFaceOrigin = current.faceOrigin()
    val (_, turn) = alternativeWaysToStep[dir].first { (path, _) ->
        newFaceOrigin = current.faceOrigin()
        // check if all steps along the path are valid positions on the grid
        path.all { step ->
            newFaceOrigin += mapDirectionToOffset[step] * (cubeSize to cubeSize) // take a step of size [cubeSize]
            grid.getCell(newFaceOrigin.row, newFaceOrigin.col, ' ') != ' '
        }
    }
    val newDir = (dir + turn).mod(4)
    val newLocal = when (dir to newDir) {
        UP to RIGHT, RIGHT to UP, LEFT to DOWN, DOWN to LEFT -> local.col to local.row

        UP to UP, DOWN to DOWN, RIGHT to LEFT, LEFT to RIGHT ->
            cubeSize - 1 - local.row to local.col

        UP to DOWN, DOWN to UP, RIGHT to RIGHT, LEFT to LEFT ->
            local.row to cubeSize - 1 - local.col

        else -> cubeSize - 1 - local.col to cubeSize - 1 - local.row
    }
    val newPos = newFaceOrigin + newLocal
    return if (grid.getCell(newPos) == '#') {
        current to dir
    } else {
        newPos to newDir
    }
}

/** for a position, return the top-left corner of the face this positin belongs to */
private fun Pair<Int, Int>.faceOrigin() =
    (this.row / cubeSize) * cubeSize to (this.col / cubeSize) * cubeSize

/** returns a collection of "methods" of taking a step in direction [dir].
 * each "method" consists of a path to follow and a final turn needed to reorient
 * */
private fun alternativeWaysToStep(dir: Int): Collection<Pair<List<Int>, Int>> {
    val waysToWalkForward = waysToWalkForward()
    if (dir == UP) return waysToWalkForward
    return waysToWalkForward.map { (path, turn) ->
        path.map { (it + (dir - UP)).mod(4) } to turn
    }
}

private const val FORWARD = UP

/** in this context, UP is used to mean go Forward by one cube face
 * LEFT and RIGHT means turn in place
 * */
private val alternateWaysToMoveUp = setOf(
    listOf(LEFT, FORWARD, RIGHT, FORWARD, LEFT),
    listOf(RIGHT, FORWARD, LEFT, FORWARD, RIGHT),
    listOf(RIGHT, RIGHT, FORWARD, FORWARD, FORWARD, RIGHT, RIGHT),
)

private fun waysToWalkForward(): Set<Pair<List<Int>, Int>> {
    val waysToMoveUp = HashSet(alternateWaysToMoveUp)
    waysToMoveUp.add(listOf(FORWARD))
    while (true) {
        val prevSize = waysToMoveUp.size
        val newWays = hashSetOf<List<Int>>()
        waysToMoveUp.forEach { path ->
            path.forEachIndexed { index, i ->
                if (i == FORWARD) {
                    val prefix = path.subList(0, index)
                    val suffix = path.subList(index + 1, path.size)
                    alternateWaysToMoveUp.forEach { replacement ->
                        (prefix + replacement + suffix).takeIf { it.size < 16 }
                            ?.let(newWays::add)
                    }
                }
            }
        }
        waysToMoveUp.addAll(newWays)
        if (waysToMoveUp.size == prevSize) {
            break
        }
    }
    val waysToWalkUp = hashSetOf<Pair<List<Int>, Int>>()
    waysToMoveUp.forEach { path ->
        var dir = UP
        var lastTurn = 0
        val newPath = StringBuilder()
        path.forEach { move ->
            when (move) {
                UP -> {
                    newPath.append(directionNames[dir])
                    lastTurn = 0
                }

                LEFT -> {
                    dir = (dir - 1).mod(4)
                    lastTurn--
                }

                RIGHT -> {
                    dir = (dir + 1).mod(4)
                    lastTurn++
                }
            }
        }
        val newString = newPath.toString()
        if (lastTurn.mod(4) > 0 &&
            !newString.startsWith("U") &&
            !newString.contains("UD") &&
            !newString.contains("DU") &&
            !newString.contains("LR") &&
            !newString.contains("RL")
        ) {
            waysToWalkUp.add(
                newString.map { char -> directionNames.indexOf(char.toString()) } to (dir - UP).mod(4)
            )
        }
    }
    return waysToWalkUp
}

/** alternative methods for moving 1 step in each direction */
private val alternativeWaysToStep = listOf(RIGHT, DOWN, LEFT, UP).map { dir -> alternativeWaysToStep(dir) }