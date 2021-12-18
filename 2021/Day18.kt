package advent2021

import java.io.File
import kotlin.math.ceil
import kotlin.math.floor

/** Day 18: Snailfish */
fun main() {
    part1()
    part2()
}

private fun part2() {
    val lines = File("input.txt").readLines()
    val max = lines.maxOf { line1 ->
        lines.maxOf { line2 ->
            if (line1 == line2) {
                -1
            } else {
                (line1.readSnailfish() + line2.readSnailfish())
                    .magnitude()
            }
        }
    }
    println(max)
}

private fun part1() {
    val input = File("input.txt").readLines().map { it.readSnailfish() }
    val snailSum = input.reduce { acc, snail -> acc + snail }
    println(snailSum.magnitude())
}

private sealed class Snail {
    abstract var replaceSelf: (Snail) -> Unit
    abstract fun isPair(): Boolean
    abstract fun magnitude(): Long

    fun findPairAtDepth(depth: Int): Snailfish? =
        if (this.isPair()) {
            if (depth == 0) {
                this as Snailfish
            } else {
                null
            }
        } else if (this is Literal) {
            null
        } else if (this is Snailfish) {
            this.left.findPairAtDepth(depth - 1)
                ?: this.right.findPairAtDepth(depth - 1)
        } else {
            null
        }

    fun replaceMyself(newSelf: Snail) {
        replaceSelf(newSelf)
        newSelf.replaceSelf = this.replaceSelf
    }

    fun collectLiterals(): List<Literal> =
        arrayListOf<Literal>().also { it.parse(this) }

    fun reduce(): Snail {
        while (reduceStep()) {
        }
        return this
    }

    private fun reduceStep(): Boolean {
        val literals = collectLiterals()
        findPairAtDepth(4)?.let { pair ->
            val leftIndex = literals.indexOf(pair.left as Literal)
            val rightIndex = literals.indexOf(pair.right as Literal)
            if (leftIndex > 0) {
                literals[leftIndex - 1].n += literals[leftIndex].n
            }
            if (rightIndex < literals.lastIndex) {
                literals[rightIndex + 1].n += literals[rightIndex].n
            }
            pair.replaceMyself(Literal(0))
            return true
        }
        literals.firstOrNull { it.n > 9 }?.let { literal ->
            val half = literal.n / 2.0
            literal.replaceMyself(
                Snailfish(
                    Literal(floor(half).toInt()),
                    Literal(ceil(half).toInt())
                )
            )
            return true
        }
        return false
    }

    operator fun plus(snail: Snail): Snail =
        Snailfish(this, snail)
            .apply { replaceSelf = {} }
            .reduce()
}

private class Snailfish(
    var left: Snail,
    var right: Snail,
) : Snail() {

    init {
        left.replaceSelf = { this.left = it }
        right.replaceSelf = { this.right = it }
    }

    override lateinit var replaceSelf: (Snail) -> Unit

    override fun isPair(): Boolean = left is Literal && right is Literal
    override fun magnitude(): Long = left.magnitude() * 3 + right.magnitude() * 2
}

private class Literal(
    var n: Int
) : Snail() {
    override lateinit var replaceSelf: (Snail) -> Unit

    override fun isPair(): Boolean = false
    override fun magnitude(): Long = n.toLong()
}

private fun String.readSnailfish() = this.toMutableList()
    .readSnailfish()
    .apply { replaceSelf = {} }

private fun MutableList<Char>.readSnailfish(): Snail =
    if (this[0] == '[') {
        this.removeFirst()
        Snailfish(
            this.readSnailfish().also { this.removeFirst() },
            this.readSnailfish().also { this.removeFirst() }
        )
    } else {
        Literal(this.removeFirst().toString().toInt())
    }

private fun MutableList<Literal>.parse(snail: Snail) {
    when (snail) {
        is Literal -> this.add(snail)
        is Snailfish -> {
            this.parse(snail.left)
            this.parse(snail.right)
        }
    }
}