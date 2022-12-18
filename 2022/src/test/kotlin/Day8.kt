import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

typealias Tree = Int
typealias TreeGrid = List<List<Tree>>

class Day8 : Day(8, "Treetop Tree House") {
    private fun TreeGrid.left(x: Int, y: Int): List<Tree> = this[x].take(y)
    private fun TreeGrid.right(x: Int, y: Int): List<Tree> = this[x].drop(y + 1)
    private fun TreeGrid.up(x: Int, y: Int): List<Tree> = take(x).map { it[y] }
    private fun TreeGrid.down(x: Int, y: Int): List<Tree> = drop(x + 1).map { it[y] }
    private fun Tree.isVisible(trees: List<Tree>): Boolean = trees.isEmpty() || trees.all { it < this }

    private fun TreeGrid.isVisible(x: Int, y: Int): Boolean =
        this[x][y].let { tree ->
            tree.isVisible(left(x, y))
                    || tree.isVisible(right(x, y))
                    || tree.isVisible(up(x, y))
                    || tree.isVisible(down(x, y))
        }

    private fun TreeGrid.whichTreesAreVisible(): List<Tree> =
        flatMapIndexed { x: Int, line: List<Tree> ->
            line.mapIndexedNotNull { y, tree ->
                if (isVisible(x, y)) tree else null
            }
        }

    private fun List<String>.toTreeGrid(): TreeGrid =
        map { it.map { tree -> tree.toIntDigit() } }

    @Test
    fun part1() =
        assertEquals(
            1807,
            computeResult(
                { it.toTreeGrid() },
                { it.whichTreesAreVisible().size }
            )
        )
}