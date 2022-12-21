package advent2022

import java.io.File

/** Day 21: Monkey Math */
fun main() {
    parseInput()
    part1()
    part2()
}

private lateinit var monkeys: MutableMap<String, MathMonkey>

private fun parseInput() {
    monkeys = File("input.txt").readLines()
        .associate { line ->
            val split = line.split(": ")
            val name = split.first()
            val job = split.last()
            val monkey = when {
                job.contains(" ") -> {
                    val jobSplit = job.split(" ")
                    MathMonkey.Operation(jobSplit.first(), jobSplit.last(), jobSplit[1], name)
                }

                else -> MathMonkey.Constant(job.toLong(), name)
            }
            name to monkey
        }.toMutableMap()
}

private fun part1() {
    println(monkeys.getValue("root").resolveValue())
}

private fun part2() {
    monkeys["humn"] = MathMonkey.Human
    (monkeys["root"] as MathMonkey.Operation).let { old ->
        monkeys["root"] = MathMonkey.Equation(old.a, old.b)
    }
    monkeys.getValue("root").simplify()
    println(monkeys["root"])
}

private sealed class MathMonkey {
    /** name of this monkey */
    abstract var name: String

    /** return the value that this monkey will yell */
    abstract fun resolveValue(): Long

    /** returns true if this monkey does not depend on the value of the human */
    abstract fun simplify(): Boolean

    object Human : MathMonkey() {
        override var name: String = "humn"
        override fun resolveValue(): Long = throw UnsupportedOperationException()
        override fun toString(): String = "humn"
        override fun simplify(): Boolean = false
    }

    class Constant(val n: Long, override var name: String) : MathMonkey() {
        override fun resolveValue(): Long = n
        override fun toString(): String = n.toString()
        override fun simplify(): Boolean = true
    }

    class Operation(val a: String, val b: String, val operation: String, override var name: String) :
        MathMonkey() {
        override fun resolveValue(): Long {
            val aVal = monkeys.getValue(a).resolveValue()
            val bVal = monkeys.getValue(b).resolveValue()
            return when (operation) {
                "+" -> aVal + bVal
                "-" -> aVal - bVal
                "*" -> aVal * bVal
                else -> aVal / bVal
            }
        }

        override fun simplify(): Boolean = monkeys.getValue(a).simplify() && monkeys.getValue(b).simplify()

        override fun toString(): String = "(${monkeys.getValue(a)}) $operation (${monkeys.getValue(b)})"
    }

    class Equation(val a: String, val b: String) : MathMonkey() {
        override var name: String = "root"

        override fun resolveValue(): Long {
            val aVal = monkeys.getValue(a).resolveValue()
            val bVal = monkeys.getValue(b).resolveValue()
            return when {
                aVal == bVal -> 1
                else -> 0
            }
        }

        /** recursively move values across the equals sign to isolate the Human
         * will eventually return true when the equation is fully simplified.
         * */
        override fun simplify(): Boolean {
            val monkeyA = monkeys.getValue(a)
            val monkeyB = monkeys.getValue(b)
            val simplifiedA = monkeyA.simplify()
            val simplifiedB = monkeyB.simplify()
            return when {
                !simplifiedA && monkeyA is Operation -> {
                    simplifyOperation(monkeyA, monkeyB)
                    simplify()
                }

                !simplifiedB && monkeyB is Operation -> {
                    simplifyOperation(monkeyB, monkeyA)
                    simplify()
                }

                else -> true
            }
        }

        private fun simplifyOperation(monkey: Operation, other: MathMonkey) {
            val nestedA = monkeys.getValue(monkey.a)
            val nestedB = monkeys.getValue(monkey.b)
            val simplifiedA = nestedA.simplify()
            val constant = if (simplifiedA) nestedA.resolveValue() else nestedB.resolveValue()
            val unsimplified = if (simplifiedA) nestedB else nestedA
            when (monkey.operation) {
                "+" -> monkeys[other.name] = Constant(other.resolveValue() - constant, other.name)
                "*" -> monkeys[other.name] = Constant(other.resolveValue() / constant, other.name)

                "-" -> {
                    if (simplifiedA) {
                        monkeys[other.name] = Constant(constant - other.resolveValue(), other.name)
                    } else {
                        monkeys[other.name] = Constant(other.resolveValue() + constant, other.name)
                    }
                }

                "/" -> {
                    if (simplifiedA) {
                        monkeys[other.name] = Constant(constant / other.resolveValue(), other.name)
                    } else {
                        monkeys[other.name] = Constant(other.resolveValue() * constant, other.name)
                    }
                }
            }
            unsimplified.name = monkey.name
            monkeys[monkey.name] = unsimplified
        }

        override fun toString(): String = "${monkeys.getValue(a)} = ${monkeys.getValue(b)}"
    }
}