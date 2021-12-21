package advent2021

import java.io.File
import kotlin.math.max
import kotlin.math.min

/** Day 21: Dirac Dice */
fun main() {
    parseInput()
    part1(1000)
    part2(21)
}

private lateinit var startPositions: List<Int>
private fun parseInput() {
    startPositions = File("input.txt").readLines().map {
        it.split(":").last().trim().toInt() - 1
    }
}

private fun part2(winningScore: Int) {
    val (player1Wins, player2Wins) = countWinningUniverses(
        GameState(
            player1 = PlayerState(position = startPositions[0]),
            player2 = PlayerState(position = startPositions[1]),
            isPlayer1Turn = true
        ),
        winningScore
    )
    println(max(player1Wins, player2Wins))
}

private val cache = hashMapOf<GameState, Pair<Long, Long>>()
private fun countWinningUniverses(gameState: GameState, winningScore: Int): Pair<Long, Long> {
    cache[gameState]?.let { return it }
    if (gameState.player1.score >= winningScore) {
        return 1L to 0L
    }
    if (gameState.player2.score >= winningScore) {
        return 0L to 1L
    }
    var player1Wins = 0L
    var player2Wins = 0L
    gameState.takeNextTurn().forEach { newState ->
        val (p1Win, p2Win) = countWinningUniverses(newState, winningScore)
        player1Wins += p1Win
        player2Wins += p2Win
    }
    cache[gameState] = player1Wins to player2Wins
    return player1Wins to player2Wins
}

private data class GameState(
    val player1: PlayerState,
    val player2: PlayerState,
    val isPlayer1Turn: Boolean
) {
    fun takeNextTurn(): List<GameState> = if (isPlayer1Turn) {
        player1.takeTurn().map { newPlayerState ->
            this.copy(
                player1 = newPlayerState,
                isPlayer1Turn = false
            )
        }
    } else {
        player2.takeTurn().map { newPlayerState ->
            this.copy(
                player2 = newPlayerState,
                isPlayer1Turn = true
            )
        }
    }
}

private data class PlayerState(
    val position: Int,
    val score: Int = 0
) {
    fun takeTurn(): List<PlayerState> {
        val outcomes = arrayListOf<PlayerState>()
        (1..3).forEach { r1 ->
            (1..3).forEach { r2 ->
                (1..3).forEach { r3 ->
                    val newPosition = (position + r1 + r2 + r3) % 10
                    outcomes.add(
                        this.copy(
                            position = newPosition,
                            score = score + newPosition + 1
                        )
                    )
                }
            }
        }
        return outcomes
    }
}

private fun part1(winningScore: Int) {
    var player1 = startPositions[0]
    var player2 = startPositions[1]
    var player1Score = 0
    var player2Score = 0
    var nextDie = 0
    var rollCount = 0
    val rollDie = {
        val roll = nextDie + 1
        nextDie = roll % 100
        rollCount++
        roll
    }
    var isPlayer1Turn = true
    while (player1Score < winningScore && player2Score < winningScore) {
        val dieSum = rollDie() + rollDie() + rollDie()
        if (isPlayer1Turn) {
            player1 += dieSum
            player1 %= 10
            player1Score += player1 + 1
        } else {
            player2 += dieSum
            player2 %= 10
            player2Score += player2 + 1
        }
        isPlayer1Turn = !isPlayer1Turn
    }
    println(min(player1Score, player2Score) * rollCount)
}