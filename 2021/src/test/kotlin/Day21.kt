import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day21 : Day(21, "Dirac Dice") {
    data class Player(val pawn: Int, val score: Int = 0)
    data class GameResult(val winner: Player, val loser: Player, val totalRolls: Int)

    private val deterministicDice = generateSequence(1) { if (it == 100) 1 else it + 1 }.iterator()
    private val diracDiceCombinations = listOf(1, 2, 3).let {
        it.flatMap { dice1 -> it.flatMap { dice2 -> it.map { dice3 -> dice1 + dice2 + dice3 } } }
    }

    private fun Player.movePawn(dice: Int): Player =
        this.copy(pawn = ((pawn + dice) % 10).let {
            if (it == 0) it + 10 else it
        })

    private fun Player.score(): Player = copy(score = this.score + this.pawn)
    private fun Player.reached(winningScore: Int): Boolean = this.score >= winningScore
    private fun List<Player>.winner(): Player = if (this[0].score > this[1].score) this[0] else this[1]
    private fun List<Player>.reached(winningScore: Int): Boolean = this.any { it.reached(winningScore) }

    private fun play(
        player1: Player,
        player2: Player,
        winningScore: Int,
        roll: () -> List<Int>
    ): GameResult {
        val players = mutableListOf(player1, player2)
        var rolls = 0
        var playerIndex = 0

        while (!players.reached(winningScore)) {
            var currentPlayer = players[playerIndex]
            roll().map { dice ->
                currentPlayer = currentPlayer.movePawn(dice)
                rolls++
            }
            players[playerIndex] = currentPlayer.score()
            playerIndex = (playerIndex + 1) % 2
        }

        return players
            .winner()
            .let { winner -> GameResult(winner, if (winner == players[0]) players[1] else players[0], rolls) }
    }

    private fun playWithDiracDice(
        player1: Player,
        player2: Player,
        cache: HashMap<String, Pair<Long, Long>>,
        winningScore: Int = 21
    ): Pair<Long, Long> {
        if (player1.reached(winningScore)) return Pair(1, 0)
        if (player2.reached(winningScore)) return Pair(0, 1)

        fun key(): String = "$player1-$player2"

        if (key() in cache.keys)
            return cache[key()]!!

        var win1 = 0L
        var win2 = 0L

        diracDiceCombinations.forEach { dice ->
            val newPlayer1 = player1.movePawn(dice).score()
            val inOtherUniverses = playWithDiracDice(player2, newPlayer1, cache)

            cache["$player2-$newPlayer1"] = inOtherUniverses

            win2 += inOtherUniverses.first
            win1 += inOtherUniverses.second
        }
        return Pair(win1, win2)
    }

    private fun `what do you get if you multiply the score of the losing player by the number of times the die was rolled during the game`(): Int =
        play(Player(5), Player(8), 1000) {
            listOf(deterministicDice.next(), deterministicDice.next(), deterministicDice.next())
        }.let { it.loser.score * it.totalRolls }

    private fun `find the player that wins in more universes in how many universes does that player win`() =
        playWithDiracDice(Player(5), Player(8), hashMapOf())
            .toList()
            .maxOf { it }

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