import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day4 : Day(4, "Giant Squid") {
    data class Board(val grid: List<List<Int>>)
    data class Winner(val suite: List<Int>, val board: Board)

    private fun List<String>.loadSuite(): List<Int> = this.first().split(",").toInts()
    private fun String.toRow(): List<Int> = this.chunked(3).map { it.removeWhitespaces().toInt() }
    private fun List<String>.loadBoards(): List<Board> {
        return this.drop(1)
            .chunked(6)
            .map { lines -> Board(lines.drop(1).map { it.toRow() }) }
    }

    private fun Board.isWinner(suite: List<Int>): Boolean {
        for (i in 0..4) {
            var (row, column) = Pair(true, true)

            for (j in 0..4) {
                row = row && suite.contains(this.grid[i][j])
                column = column && suite.contains(this.grid[j][i])
            }
            if (row || column) return true
        }
        return false
    }

    private fun Board.unmarkedNumbers(suite: List<Int>): List<Int> = this.grid
        .flatten()
        .filterNot { suite.contains(it) }

    private fun Winner.calculateScore(): Int = board.unmarkedNumbers(suite).sum() * suite.last()

    private fun whoWinsFirst(
        suite: List<Int>,
        boards: List<Board>
    ): Winner {
        1.until(suite.size).forEach { round ->
            val currentSuite = suite.subList(0, round)
            boards.firstOrNull { it.isWinner(currentSuite) }
                ?.let { return Winner(currentSuite, it) }
        }
        throw Exception("no winning board ?")
    }

    private fun List<Winner>.containsBoard(board: Board): Boolean = this.any { winner -> winner.board == board }

    private fun retrieveAllWinningBoards(
        suite: List<Int>,
        boards: List<Board>
    ): List<Winner> {
        val winners = mutableListOf<Winner>()

        1.until(suite.size).forEach { round ->
            val currentSuite = suite.subList(0, round)
            val winnersAtThisRound = boards.filter { it.isWinner(currentSuite) }.map { Winner(currentSuite, it) }

            winners.addAll(winnersAtThisRound.filterNot { winner -> winners.containsBoard(winner.board) })

            if (winners.size == boards.size)
                return winners
        }
        throw Exception("guards not reached ?")
    }

    private fun `which board will win first`(lines: List<String>): Int =
        whoWinsFirst(lines.loadSuite(), lines.loadBoards())
            .calculateScore()

    private fun `which board will win last`(lines: List<String>): Int =
        retrieveAllWinningBoards(lines.loadSuite(), lines.loadBoards())
            .last()
            .calculateScore()

    @Test
    fun exercise1() =
        Assertions.assertEquals(
            51034,
            computeResult { `which board will win first`(it) }
        )

    @Test
    fun exercise2() =
        Assertions.assertEquals(
            5434,
            computeResult { `which board will win last`(it) }
        )
}