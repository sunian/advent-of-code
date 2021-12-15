package advent2021

import java.io.File

/** Day 12: Passage Pathing */
fun main() {
    parseInput()
    println(part1(listOf("start")))
    println(part2(listOf("start")))
}

private val graph = hashMapOf<String, List<String>>()

private fun parseInput() {
    File("input.txt").forEachLine { line ->
        val (start, end) = line.split("-")
        graph.addListElement(start, end)
        graph.addListElement(end, start)
    }
}

private fun part2(path: List<String>): Int {
    if (path.last() == "end") {
        return 1
    }
    return graph[path.last()]!!.sumBy { cave ->
        val hasDuplicateSmall = path.any { !it.isBig() && path.countCopies(it) > 1 }
        if (cave.isBig() ||
            cave !in path ||
            (cave != "start" && !hasDuplicateSmall)
        ) {
            part2(path + cave)
        } else {
            0
        }
    }
}

private fun part1(path: List<String>): Int {
    if (path.last() == "end") {
        return 1
    }
    return graph[path.last()]!!.sumBy { cave ->
        if (cave.isBig() || cave !in path) {
            part1(path + cave)
        } else {
            0
        }
    }
}

private fun String.isBig() = this[0].isUpperCase()