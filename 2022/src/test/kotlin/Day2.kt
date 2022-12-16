import Day2.Shape.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day2 : Day(2, "Rock Paper Scissors") {
    private val win = 6
    private val draw = 3
    private val lost = 0

    private val winningCombinations = listOf(Pair(Rock, Scissors), Pair(Scissors, Paper), Pair(Paper, Rock))

    private enum class Shape(val score: Int) { Rock(1), Paper(2), Scissors(3) }

    private val player1Strategy = mapOf('A' to Rock, 'B' to Paper, 'C' to Scissors)
    private val player2Strategy = mapOf('X' to Rock, 'Y' to Paper, 'Z' to Scissors)

    private data class Round(val player1: Shape, val player2: Shape)

    private fun String.toRound(): Round = Round(player1Strategy[this[0]]!!, player2Strategy[this[2]]!!)
    private fun List<String>.toRounds(): List<Round> = this.map { it.toRound() }

    private fun Round.score(): Int = player2.score + outcomeScore()

    private fun Round.outcomeScore() = when {
        winningCombinations.contains(Pair(player2, player1)) -> win
        player1 == player2 -> draw
        else -> lost
    }

    private fun calculateMyScore(rounds: List<Round>): Int = rounds.sumOf { it.score() }

    @Test
    fun part1() =
        assertEquals(14297,
            computeResult {
                calculateMyScore(
                    it.toRounds()
                )
            }
        )
}