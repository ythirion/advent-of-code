import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

typealias WeightedMatrix = Map<Coordinate, Int>

class Day15 : Day(15, "Chiton") {
    private fun List<String>.toMatrix(): WeightedMatrix =
        this.foldIndexed(mutableMapOf()) { y, map, value ->
            for ((x, v) in value.withIndex()) {
                map[Coordinate(x, y)] = v.toIntDigit()
            }
            map
        }

    private fun List<String>.toExpandedMatrix(): WeightedMatrix = toMatrix().expand()

    private fun WeightedMatrix.findMinCostPath(
        start: Coordinate,
        end: Coordinate
    ): Int {
        return shortestWeightedPath(
            from = start,
            neighbours = { c ->
                c.directNeighbours.mapNotNull { n ->
                    this[n]?.let { Pair(n, it) }
                }
            },
            process = { if (it.destination == end) it.pathLength else null }
        ) ?: error("No path found")
    }

    private fun WeightedMatrix.expand(): WeightedMatrix {
        val sizeX = sizeX
        val sizeY = sizeY
        val expandedMatrix = mutableMapOf<Coordinate, Int>()

        for (k in 0 until 5) {
            for (m in 0 until 5) {
                for (y in 0 until sizeY) {
                    for (x in 0 until sizeX) {
                        val c = Coordinate(k * sizeX + x, m * sizeY + y)
                        val v = this[Coordinate(x, y)]!!
                        expandedMatrix[c] = 1 + (v + k + m - 1) % 9
                    }
                }
            }
        }
        return expandedMatrix
    }

    private fun `What is the lowest total risk of any path from the top left to the bottom right`(lines: List<String>) =
        lines.toMatrix().let {
            it.findMinCostPath(Coordinate.origin, it.maxCoordinate)
        }


    private fun `Using the full map, what is the lowest total risk of any path from the top left to the bottom right`(
        lines: List<String>
    ) =
        lines.toExpandedMatrix().let {
            it.findMinCostPath(Coordinate.origin, it.maxCoordinate)
        }

    @Test
    fun part1() =
        Assertions.assertEquals(
            447,
            computeResult {
                `What is the lowest total risk of any path from the top left to the bottom right`(it)
            }
        )

    @Test
    fun part2() =
        Assertions.assertEquals(
            2825,
            computeResult {
                `Using the full map, what is the lowest total risk of any path from the top left to the bottom right`(it)
            }
        )
}