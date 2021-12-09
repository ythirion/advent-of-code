import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

typealias HeightMap = List<Day9.Point>
typealias Basin = Set<Day9.Point>

class Day9 : Day(9, "Smoke Basin") {
    data class Point(val x: Int, val y: Int, val height: Int)

    private fun Point.adjacent(heightMap: HeightMap): List<Point> {
        return listOfNotNull(
            heightMap.safeGet(x + 1, y),
            heightMap.safeGet(x - 1, y),
            heightMap.safeGet(x, y + 1),
            heightMap.safeGet(x, y - 1)
        )
    }

    private fun Point.findBasin(
        heightMap: HeightMap
    ): Basin {
        var basin = setOf(this)
        var expandedBasin: Set<Point>

        while (true) {
            expandedBasin = basin.expand(heightMap)

            // reach only 9 adjacent to the basin
            if (expandedBasin.size == basin.size)
                return expandedBasin

            basin = expandedBasin
        }
    }

    private fun Basin.expand(
        heightMap: HeightMap
    ): Basin {
        this.toMutableSet().let { expandedBasin ->
            this.forEach { point ->
                expandedBasin.addAll(point.adjacent(heightMap).filter { it.height != 9 })
            }
            return expandedBasin
        }
    }

    private fun List<String>.toHeightMap(): HeightMap {
        mutableListOf<Point>().let { points ->
            this.forEachIndexed { x, line ->
                line.forEachIndexed { y, value ->
                    points += Point(x, y, value.toIntDigit())
                }
            }
            return points
        }
    }

    private fun HeightMap.lowPoints(): List<Point> {
        return this.filter { point ->
            point.adjacent(this).all { point.height < it.height }
        }
    }

    private fun HeightMap.safeGet(x: Int, y: Int): Point? = this.find { it.x == x && it.y == y }


    private fun `What is the sum of the risk levels of all low points on your heightmap`(heightMap: HeightMap): Int {
        return heightMap.lowPoints().sumOf { it.height + 1 }
    }

    private fun `What do you get if you multiply together the sizes of the three largest basins`(heightMap: HeightMap): Int {
        return heightMap.lowPoints()
            .asSequence()
            .map { it.findBasin(heightMap) }
            .sortedByDescending { it.size }
            .take(3)
            .map { it.size }
            .multiply()
    }


    @Test
    fun part1() =
        Assertions.assertEquals(
            585,
            computeResult(
                { it.toHeightMap() },
                { `What is the sum of the risk levels of all low points on your heightmap`(it) }
            )
        )

    @Test
    fun part2() =
        Assertions.assertEquals(
            827904,
            computeResult(
                { it.toHeightMap() },
                { `What do you get if you multiply together the sizes of the three largest basins`(it) }
            )
        )
}