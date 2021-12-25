package advent2021

import java.io.File
import kotlin.math.roundToLong

/** Day 24: Arithmetic Logic Unit */
fun main() {
    parseInput()
    Visualizer(function = {
        doAlgoV2(buildInput(it))
    }, buildInput = { buildInput(it) }
    )
//    println("35639583867189".toCharArray().joinToString("\t"))
//    println("35639683978189".toCharArray().joinToString("\t"))
//    println(Pvalues.joinToString("\t"))
//    println(Mvalues.joinToString("\t"))
//    println(Nvalues.joinToString("\t"))
//    val valid = listOf(
//        34969183753179,
//        34969394755179,
//        35639183423189,
//        35639294864189,
//        35639283534189,
//        35639383645189,
//        35639483756189,
//        35639583867189,
//        35639683978189,
//        35639583977189,
//    )

//    analyze()
//    binarySearch(6380548988363, 22876792454960)
    val validModelNumbers = recursiveSearch("", 0, 0)
    println(validModelNumbers.first())
    println(validModelNumbers.last())
}

private fun recursiveSearch(partial: String, z: Long, iter: Int): List<String> {
    if (iter == 14) {
        return if (z == 0L) {
            listOf(partial)
        } else {
            emptyList()
        }
    }
    return (9 downTo 1)
        .filter { w -> checkIfXEqualW(w, z, iter) == (Pvalues[iter] == 26) }
        .flatMap { w ->
            val output = doAlgoIteration(w, z, iter)
            recursiveSearch(partial + w, output, iter + 1)
        }
}

private val instructions = arrayListOf<List<String>>()
private val memory = hashMapOf<String, Int>()
private var input = ""
private var inputPosition = 0
private fun parseInput() {
    File("input.txt").forEachLine { line ->
        instructions.add(line.split(" "))
    }
}

private val Pvalues = arrayOf(1, 1, 1, 26, 1, 1, 1, 26, 1, 26, 26, 26, 26, 26)
private val Mvalues = arrayOf(11, 11, 15, -11, 15, 15, 14, -7, 12, -6, -10, -15, -9, 0)
private val Nvalues = arrayOf(6, 12, 8, 7, 7, 12, 2, 15, 4, 5, 12, 11, 13, 7)

private fun doAlgoIteration(w: Int, zInput: Long, iter: Int): Long {
    var z = zInput
    val x = (z % 26).toInt() + Mvalues[iter]
    z /= Pvalues[iter]
//    print(if (x == w) "= " else "! ")
    if (x != w) {
        z *= 26
        z += w + Nvalues[iter]
    }
    return z
}

private fun checkIfXEqualW(w: Int, zInput: Long, iter: Int): Boolean {
    val x = (zInput % 26).toInt() + Mvalues[iter]
    return x == w
}

private fun analyze() {
    val magicNumbers = Array(18) { arrayListOf<String>() }
    instructions.forEachIndexed { index, instruction ->
        val i = index % 18
        if (i > 0) {
            magicNumbers[i].add(instruction[2])
        }
    }
    listOf(4, 5, 15).forEach {
        println("$it = " + magicNumbers[it])
    }
}

private fun binarySearch(start: Long, end: Long) {
    if (end - start < 500) {
        (start..end).forEach {
            val input = buildInput(it)
            val output = doAlgoV2(input)
            if (output == 0L) {
                println("$it -> $input -> 0")
            }
        }
        return
    }
    val inc = (end - start) / 10
    ((0..9).map { start + it * inc } + end)
        .windowed(2)
        .sortedBy {
            val midpoint = it.average().roundToLong()
            val input = buildInput(midpoint)
            val output = doAlgoV2(input)
            if (output == 0L) {
                println("$midpoint -> $input -> 0")
            }
            output
        }
        .take(4)
        .forEach { binarySearch(it[0], it[1]) }
}

private fun buildInput(n: Long): String = n.toString(9)
    .padStart(14, '0')
    .map { it + 1 }
    .joinToString("")

private fun reverseInput(n: String): Long = n.map { it - 1 }
    .joinToString("")
    .toLong(9)

private fun doAlgo(s: String): Int {
    memory["w"] = 0
    memory["x"] = 0
    memory["y"] = 0
    memory["z"] = 0
    input = s
    inputPosition = 0
    instructions.forEach { it.runInstruction() }
    return memory["z"]!!
}

private fun doAlgoV2(s: String): Long {
    input = s
    var z = 0L
    repeat(14) {
        val w = input[it].toString().toInt()
        z = doAlgoIteration(w, z, it)
//        println("step $it $w\t${Pvalues[it]} ->\t${z}")
    }
    return z
}

private fun List<String>.runInstruction() {
    when (this[0]) {
        "inp" -> {
            val n = input[inputPosition].toString().toInt()
            inputPosition++
            memory[this[1]] = n
        }
        "add" -> {
            val b = memory[this[2]] ?: this[2].toInt()
            memory[this[1]] = memory[this[1]]!! + b
        }
        "mul" -> {
            val b = memory[this[2]] ?: this[2].toInt()
            memory[this[1]] = memory[this[1]]!! * b
        }
        "div" -> {
            val b = memory[this[2]] ?: this[2].toInt()
            memory[this[1]] = memory[this[1]]!! / b
        }
        "mod" -> {
            val b = memory[this[2]] ?: this[2].toInt()
            memory[this[1]] = memory[this[1]]!! % b
        }
        "eql" -> {
            val b = memory[this[2]] ?: this[2].toInt()
            memory[this[1]] = if (memory[this[1]]!! == b) 1 else 0
        }
    }
}