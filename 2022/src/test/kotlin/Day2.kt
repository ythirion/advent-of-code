import Day2.Shape.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day2 : Day(2, "Rock Paper Scissors") {
    private val win = 6
    private val draw = 3
    private val lost = 0

    private val winningCombinations = listOf(Pair(Rock, Scissors), Pair(Scissors, Paper), Pair(Paper, Rock))

    private enum class Shape(val score: Int) { Rock(1), Paper(2), Scissors(3) }

    private val player1Mapping = mapOf('A' to Rock, 'B' to Paper, 'C' to Scissors)

    private data class Round(val player1: Shape, val player2: Shape)

    private val thoughtStrategy: (player1: Shape, player2: Char) -> Shape = { _, player2 ->
        mapOf('X' to Rock, 'Y' to Paper, 'Z' to Scissors)[player2]!!
    }

    private val elfStrategy: (player1: Shape, player2: Char) -> Shape = { player1, player2 ->
        when (player2) {
            'X' -> loseAgainst(player1)
            'Y' -> player1
            else -> winAgainst(player1)
        }
    }

    private fun loseAgainst(shape: Shape): Shape = winningCombinations.first { r -> r.first == shape }.second
    private fun winAgainst(shape: Shape) = winningCombinations.first { r -> r.second == shape }.first

    private fun List<String>.toRounds(strategy: (player1: Shape, player2: Char) -> Shape): List<Round> =
        this.map {
            player1Mapping[it[0]]!!.let { player1 ->
                Round(player1, strategy(player1, it[2]))
            }
        }

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
                calculateMyScore(it.toRounds(thoughtStrategy))
            }
        )

    @Test
    fun part2() =
        assertEquals(10498,
            computeResult {
                calculateMyScore(it.toRounds(elfStrategy))
            }
        )
}