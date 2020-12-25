import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

typealias Player = ArrayDeque<Int>
typealias Hands = Pair<Player, Player>

class Day22 : Day(22) {
    private fun String.toPlayer() = ArrayDeque(this.splitLines().drop(1).mapNotNull { it.toInt() })
    private fun Player.score() = this.mapIndexed { i, card -> (this.size - i) * card }.sum()
    private enum class Mode { Classic, Recursive }

    private fun playCombat(player1: Player, player2: Player): Int {
        while (player1.isNotEmpty() && player2.isNotEmpty()) {
            val card1 = player1.removeFirst()
            val card2 = player2.removeFirst()
            val winner = if (card1 > card2) player1 else player2
            winner.addAll(listOf(card1, card2).sortedDescending())
        }
        return if (!player1.isEmpty()) 1 else 2
    }

    private fun playRecursiveCombat(player1: Player, player2: Player): Int {
        val previousHands = mutableListOf<Hands>()

        while (player1.isNotEmpty() && player2.isNotEmpty()) {
            val currentHands = Hands(Player(player1.toList()), ArrayDeque(player2.toList()))
            //Avoid Infinite loop
            if (previousHands.contains(currentHands)) return 1

            previousHands.add(currentHands)
            val card1 = player1.removeFirst()
            val card2 = player2.removeFirst()

            val winner = if (player1.size >= card1 && player2.size >= card2) {
                playRecursiveCombat(Player(player1.take(card1)), Player(player2.take(card2)))
            } else if (card1 > card2) 1 else 2

            if (winner == 1) player1.addAll(listOf(card1, card2))
            else player2.addAll(listOf(card2, card1))
        }
        return if (!player1.isEmpty()) 1 else 2
    }

    private fun play(input: String, mode: Mode): Int {
        val players = input.splitAtEmptyLine()
        val player1 = players[0].toPlayer()
        val player2 = players[1].toPlayer()

        val result = when (mode) {
            Mode.Classic -> playCombat(player1, player2)
            Mode.Recursive -> playRecursiveCombat(player1, player2)
        }
        return (if (result == 1) player1 else player2).score()
    }

    @Test
    fun exercise1() = computeStringResult {
        Assertions.assertEquals(34324, play(it, Mode.Classic))
    }

    @Test
    fun exercise2() = computeStringResult {
        Assertions.assertEquals(33259, play(it, Mode.Recursive))
    }
}