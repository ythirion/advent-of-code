import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

typealias DeterministicDice = Int
typealias DiracDice = Triple<Int, Int, Int>

class Day21 : Day(21, "Dirac Dice") {
    private val deterministicDice = generateSequence(1) { if (it == 100) 1 else it + 1 }.iterator()
    private val diracDiceCombinations = listOf(1, 2, 3).let {
        it.flatMap { dice1 -> it.flatMap { dice2 -> it.map { dice3 -> Triple(dice1, dice2, dice3) } } }
    }

    data class Player(val pawn: Int, val score: Int = 0)
    data class GameResult(val winner: Int, val players: Pair<Player, Player>, val totalRolls: Int)

    private fun Player.movePawn(dice: List<Int>): Player =
        ((pawn + dice.sum()) % 10).apply { if (this == 0) this + 10 }.let { copy(pawn = it, score = score + it) }

    private fun Player.whoWins(other: Player): Player = if (this.score > other.score) this else other

    private fun play(
        player1Position: Int,
        player2Position: Int,
        winningScore: Int,
        roll: () -> List<Int>
    ): GameResult {
        var totalRolls = 0
        var player1 = Player(player1Position)
        var player2 = Player(player2Position)

        fun play(player: Player): Player =
            roll().let { die ->
                player.movePawn(die).let {
                    totalRolls += die.size
                    return it
                }
            }

        fun endGame() = player1.score >= winningScore || player2.score >= winningScore

        fun result(): GameResult = player1.whoWins(player2).let { winner ->
            return GameResult(if (winner == player1) 1 else 2, Pair(player1, player2), totalRolls)
        }

        while (true) {
            player1 = play(player1)
            if (endGame()) break
            player2 = play(player2)
            if (endGame()) break
        }
        return result()
    }

    private fun playWithDiracDice(
        position1: Int,
        position2: Int,
        cache: HashMap<String, Pair<Long, Long>>,
        score1: Int = 0,
        score2: Int = 0,
    ): Pair<Long, Long> {
        if (score1 >= 21) return Pair(1, 0)
        if (score2 >= 21) return Pair(0, 1)

        fun key(): String = "$position1,$score1-$position2,$score2"

        if (key() in cache.keys)
            return cache[key()]!!

        var player1 = 0L
        var player2 = 0L

        diracDiceCombinations.forEach { diracDice ->
            val newPosition1 = ((position1 + diracDice.toList().sum()) % 10).let {
                if (it == 0) it + 10 else it
            }
            val newScore1 = score1 + newPosition1

            val inOtherUniverses = playWithDiracDice(position2, newPosition1, cache, score2, newScore1)
            cache["$position2,$score2-$newPosition1,$newScore1"] = inOtherUniverses

            player2 += inOtherUniverses.first
            player1 += inOtherUniverses.second
        }
        return Pair(player1, player2)
    }

    private fun `what do you get if you multiply the score of the losing player by the number of times the die was rolled during the game`(): Int =
        play(5, 8, 1000) {
            listOf(
                deterministicDice.next(),
                deterministicDice.next(),
                deterministicDice.next()
            )
        }.let { (if (it.winner == 1) it.players.second.score else it.players.first.score) * it.totalRolls }

    private fun `find the player that wins in more universes in how many universes does that player win`() =
        playWithDiracDice(5, 8, hashMapOf()).let {
            if (it.first > it.second) it.first else it.second
        }

    @Test
    fun part1() =
        Assertions.assertEquals(
            1067724,
            `what do you get if you multiply the score of the losing player by the number of times the die was rolled during the game`()
        )

    @Test
    fun part2() =
        Assertions.assertEquals(
            630947104784464,
            `find the player that wins in more universes in how many universes does that player win`()
        )
}