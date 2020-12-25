import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day24 : Day(24) {
    private enum class Direction(val deltaX: Int, val deltaY: Int) {
        E(1, -1), NE(0, -1), SE(1, 0),
        W(-1, 1), NW(-1, 0), SW(0, 1)
    }

    private data class Tile(val x: Int, val y: Int)

    private fun Tile.moveTo(direction: Direction) = Tile(direction.deltaX + x, direction.deltaY + y)
    private fun Tile.adjacentTiles() = setOf(moveTo(Direction.E), moveTo(Direction.NE), moveTo(Direction.SE), moveTo(Direction.W), moveTo(Direction.NW), moveTo(Direction.SW))

    private fun moveFromReferenceTile(directions: List<Direction>) =
            directions.fold(Tile(0, 0)) { previousTile, direction -> previousTile.moveTo(direction) }

    private fun String.toDirections(): List<Direction> {
        val directions = mutableListOf<Direction>()
        var index = 0

        while (index != this.length) {
            when (this[index]) {
                'e' -> directions.add(Direction.E)
                'w' -> directions.add(Direction.W)
                'n' -> directions.add(if (this[++index] == 'e') Direction.NE else Direction.NW)
                's' -> directions.add(if (this[++index] == 'e') Direction.SE else Direction.SW)
            }
            index++
        }
        return directions
    }

    private fun getBlackTiles(lines: List<String>): Set<Tile> {
        return lines.mapNotNull { line -> moveFromReferenceTile(line.toDirections()) }
                .groupingBy { it }.eachCount()
                .filter { it.value == 1 }
                .keys.toSet()
    }

    private fun countBlackTilesOnDay(lines: List<String>, day: Int) =
            (1..day).fold(getBlackTiles(lines)) { previousDay, _ -> spendDay(previousDay) }.size

    private fun spendDay(blackTilesOfTheDay: Set<Tile>): Set<Tile> {
        return listOf(
                // Any black tile with zero or more than 2 black tiles immediately adjacent to it is flipped to white.
                blackTilesOfTheDay.filter { blackTile -> blackTile.adjacentTiles().count { blackTilesOfTheDay.contains(it) } in (1..2) },
                // Any white tile with exactly 2 black tiles immediately adjacent to it is flipped to black.
                blackTilesOfTheDay.flatMap { blackTile -> blackTile.adjacentTiles() }
                        .filter { tile -> !blackTilesOfTheDay.contains(tile) }
                        .filter { whiteTile -> whiteTile.adjacentTiles().count { blackTilesOfTheDay.contains(it) } == 2 })
                .flatten().toSet()
    }

    @Test
    fun exercise1() = computeResult {
        Assertions.assertEquals(320, getBlackTiles(it).size)
    }

    @Test
    fun exercise2() = computeResult {
        Assertions.assertEquals(3777, countBlackTilesOnDay(it, 100))
    }
}