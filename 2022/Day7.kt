package advent2022

import bucketize
import java.io.File

/** Day 7: No Space Left On Device */
fun main() {
    parseInput()
    handleLines(input)
    part1()
    part2()
}

private lateinit var input: List<String>
private val rootDir = Dir(name = "/")
private var currentDir = rootDir

private fun parseInput() {
    input = File("input.txt").readLines()
}

private fun part1() {
    var size = 0L
    dfs(rootDir) { dir ->
        dir.size().takeIf { it <= 100000 }
            ?.let { size += it }
    }
    println(size)
}

private fun part2() {
    val total = 70000000L
    val used = rootDir.size()
    val freeSpace = total - used
    val target = 30000000 - freeSpace
    var bestDir: Dir? = null
    dfs(rootDir) { dir ->
        val size = dir.size()
        if (size >= target && (bestDir == null || size < bestDir!!.size())) {
            bestDir = dir
        }
    }
    println(bestDir!!.size())
}

private fun dfs(dir: Dir, callback: (Dir) -> Unit) {
    callback(dir)
    dir.children.forEach { dfs(it.value, callback) }
}

private fun handleLines(lines: List<String>) {
    lines.bucketize { _, newElement, addToCurrentBucket, startNewBucket ->
        if (newElement.startsWith("$ ")) {
            startNewBucket()
            addToCurrentBucket(newElement.replaceFirst("$ ", ""))
        } else {
            addToCurrentBucket(newElement)
        }
    }.forEach { bucket ->
        performCommand(bucket.first(), bucket.drop(1))
    }
}

private fun performCommand(command: String, output: List<String>) {
    when {
        command.startsWith("cd ") -> {
            val name = command.replaceFirst("cd ", "")
            currentDir = when (name) {
                "/" -> rootDir
                ".." -> currentDir.parent!!
                else -> currentDir.getChild(name)
            }
        }
        command == "ls" -> {
            output.forEach { line ->
                when {
                    line.startsWith("dir ") -> {
                    }
                    else -> {
                        val split = line.split(" ", limit = 2)
                        currentDir.addFile(MyFile(name = split[1], size = split[0].toLong()))
                    }
                }
            }
        }
    }
}

private class Dir(
    val parent: Dir? = null,
    val name: String,
    val children: HashMap<String, Dir> = hashMapOf(),
    val files: HashMap<String, MyFile> = hashMapOf()
) {
    fun size(): Long = children.values.sumOf { it.size() } + files.values.sumOf { it.size }

    fun addChild(child: Dir) {
        if (children[child.name] == null) {
            children[child.name] = child
        }
    }

    fun addFile(file: MyFile) {
        if (files[file.name] == null) {
            files[file.name] = file
        }
    }

    fun getChild(name: String): Dir =
        children[name] ?: run {
            val newChild = Dir(parent = this, name = name)
            addChild(newChild)
            newChild
        }
}

private class MyFile(
    val name: String,
    val size: Long
)