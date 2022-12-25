package advent2022

import java.io.File

/** Day 25: Full of Hot Air */
fun main() {
    println(File("input.txt").readLines().sumOf { it.toBase10() }.toSnafu())
}

private fun String.toBase10(): Long {
    var sum = 0L
    var power5 = 1L
    this.reversed().forEach {
        sum += power5 * when (it) {
            '2' -> 2
            '1' -> 1
            '0' -> 0
            '-' -> -1
            else -> -2
        }
        power5 *= 5
    }
    return sum
}

private fun Long.toSnafu(): String {
    var s = this.toString(5)
    if (s.startsWith("3")) {
        s = "1=" + s.substring(1)
    }
    if (s.startsWith("4")) {
        s = "1-" + s.substring(1)
    }
    while (s.contains(Regex("[34]"))) {
        s = s.replace("03", "1=")
            .replace("13", "2=")
            .replace("23", "3=")
            .replace("=3", "-=")
            .replace("-3", "0=")
            .replace("04", "1-")
            .replace("14", "2-")
            .replace("24", "3-")
            .replace("=4", "--")
            .replace("-4", "0-")
    }
    return s
}