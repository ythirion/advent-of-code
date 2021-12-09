import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

typealias HeightMap = List<Day9.Point>

class Day9 : Day(9, "Smoke Basin") {
    data class Point(val x: Int, val y: Int, val height: Int)

    private fun List<String>.toPoints(): List<Point> {
        val points = mutableListOf<Point>()

        this.forEachIndexed { x, line ->
            line.forEachIndexed { y, value ->
                points += Point(x, y, value.toIntDigit())
            }
        }
        return points
    }

    private
    fun `What is the sum of the risk levels of all low points on your heightmap`(input: List<String>): Int {
        input.toPoints().let { heightMap ->
            return heightMap.filter { point ->
                point.adjacent(heightMap).all { point.height < it.height }
            }.sumOf { it.height + 1 }
        }
    }

    private fun Point.adjacent(heightMap: HeightMap): List<Point> {
        return listOfNotNull(
            heightMap.safeGet(x + 1, y),
            heightMap.safeGet(x - 1, y),
            heightMap.safeGet(x, y + 1),
            heightMap.safeGet(x, y - 1)
        )
    }

    private fun HeightMap.safeGet(x: Int, y: Int): Point? = this.find { it.x == x && it.y == y }

    @Test
    fun part1() =
        Assertions.assertEquals(
            585,
            computeResult { `What is the sum of the risk levels of all low points on your heightmap`(it) }
        )
}