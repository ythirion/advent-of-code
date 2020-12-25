import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.sqrt

class Day20 : Day(20) {
    private class Tile(val id: Long, val image: List<String>) {
        val top = image.first()
        val bottom = image.last()
        val left = image.map { line -> line.first() }.joinToString("")
        val right = image.map { line -> line.last() }.joinToString("")
    }

    private val seaMonster = listOf(
            "                  # ",
            "#    ##    ##    ###",
            " #  #  #  #  #  #   ")

    private fun List<String>.flip() = (0 until size).map { this[it].reversed() }
    private fun List<String>.rotate() = (0 until size).map { column(it).reversed() }
    private fun List<String>.column(i: Int) = this.map { it[i] }.joinToString("")
    private fun List<String>.removeBorders() = (1 until lastIndex).map { this[it].substring(1, this.first().length - 1) }
    private fun List<String>.generateVariants(): Set<List<String>> {
        val allVariants = mutableSetOf<List<String>>()
        var current = this

        (1..4).forEach {
            allVariants += current
            current = current.rotate()
        }

        current = current.flip()
        for (i in 1..4) {
            allVariants += current
            current = current.rotate()
        }
        return allVariants
    }

    private fun List<String>.countMonsters(): Int {
        var count = 0
        for (i in 0..size - seaMonster.size) {
            for (j in 0..this[i].length - seaMonster[0].length) {
                var found = true
                for (ii in seaMonster.indices) {
                    for (jj in seaMonster[ii].indices) {
                        if (this[i + ii][j + jj] != seaMonster[ii][jj] && seaMonster[ii][jj] == '#') {
                            found = false
                            break
                        }
                    }
                }
                if (found) count++
            }
        }
        return count
    }

    private fun List<List<String>>.toImage(squareSize: Int): List<String> {
        val result = mutableListOf<CharArray>()
        for (i in 0 until squareSize * 8) {
            result += CharArray(squareSize * 8) {
                this[i / 8 * squareSize + it / 8][i % 8][it % 8]
            }
        }
        return result.map { it.joinToString("") }
    }

    //region BFS algorithm
    //https://fr.wikipedia.org/wiki/Algorithme_de_parcours_en_largeur
    private fun breadthFirstSearch(
            initialState: List<Tile>,
            squareSize: Int,
            tiles: Set<Tile>,
    ): List<Tile>? {
        val closed = mutableSetOf<List<Tile>>()
        var opened = mutableSetOf<List<Tile>>()

        opened.add(initialState)

        while (opened.size > 0) {
            val achievable = mutableSetOf<List<Tile>>()
            for (current in opened) {
                if (current.size == squareSize * squareSize) {
                    return current
                }

                val set = generateNext(current, squareSize, tiles)
                for (next in set) {
                    if (!closed.contains(next)) {
                        achievable.add(next)
                    }
                }
                closed.add(current)
            }
            opened = achievable
        }
        return null
    }

    private fun generateNext(
            current: List<Tile>,
            squareSize: Int,
            tiles: Set<Tile>,
    ): Set<List<Tile>> {
        val result = mutableSetOf<List<Tile>>()
        val details = tiles.filter { t -> current.count { it.id == t.id } == 0 }

        for (detail in details) {
            val possibilities = mutableListOf<Tile>()
            possibilities += current
            possibilities += detail

            if (isValid(squareSize, possibilities)) {
                result += possibilities
            }
        }
        return result
    }

    private fun isValid(squareSize: Int, possibilities: List<Tile>): Boolean {
        val index = possibilities.lastIndex
        val i = index / squareSize
        val j = index % squareSize
        val tile = possibilities[i * squareSize + j]

        if (i > 0) {
            val up = tile.top
            val adjacent = possibilities[(i - 1) * squareSize + j]
            val down = adjacent.bottom
            if (up != down) {
                return false
            }
        }
        if (j > 0) {
            val left = tile.left
            val adjacent = possibilities[i * squareSize + j - 1]
            val right = adjacent.right

            if (left != right) {
                return false
            }
        }
        return true
    }

    //endregion

    private fun extractTiles(input: String): Set<Tile> {
        return input.splitAtEmptyLine().flatMap { group ->
            val lines = group.splitLines()
            val id = lines.first().substring(5, 9).toLong()
            lines.subList(1, lines.size).generateVariants().map { Tile(id, it) }
        }.toSet()
    }

    private fun assembleTiles(input: String): List<Tile> {
        val tiles = extractTiles(input)
        var solution: List<Tile>? = null
        //We have now 8 tiles for each original tile
        val squareSize = sqrt((tiles.size / 8).toDouble()).toInt()

        for (tile in tiles) {
            solution = breadthFirstSearch(listOf(tile), squareSize, tiles)
            if (solution != null) {
                return solution
            }
        }
        throw IllegalArgumentException("No solution found")
    }

    private fun calculateCornersResult(input: String): Long {
        val solution = assembleTiles(input)
        val squareSize = sqrt(solution.size.toDouble()).toInt()

        return solution[0].id * solution[squareSize - 1].id *
                solution[(squareSize - 1) * squareSize].id * solution[squareSize * squareSize - 1].id
    }

    private fun countSeaMonsters(input: String): Int {
        val solution = assembleTiles(input)
        val squareSize = sqrt(solution.size.toDouble()).toInt()
        val solutionImages = solution
                .map { it.image }
                .map { it.removeBorders() }
                .toImage(squareSize)
                .generateVariants()

        solutionImages.forEach { image ->
            val countMonsters = image.countMonsters()

            if (countMonsters > 0)
                return image.fold(0) { acc, line -> acc + line.count { it == '#' } } -
                        countMonsters * seaMonster.fold(0) { acc, line -> acc + line.count { it == '#' } }
        }
        throw IllegalArgumentException("No monster found")
    }

    @Test
    fun exercise1() = computeStringResult {
        Assertions.assertEquals(5775714912743, calculateCornersResult(it))
    }

    @Test
    fun exercise2() = computeStringResult {
        Assertions.assertEquals(1836, countSeaMonsters(it))
    }
}