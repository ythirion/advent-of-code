import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

typealias HeightMap = List<List<Int>>

class Day9 : Day(9, "Smoke Basin") {
    data class Point(val x: Int, val y: Int, val value: Int)

    private fun `What is the sum of the risk levels of all low points on your heightmap`(input: List<String>): Int {
        val heightMap = input.map { it.splitInts() }
        val adjacent = mutableMapOf<Point, List<Int>>()

        for (x in heightMap.indices) {
            for (y in 0 until heightMap[0].size) {
                adjacent[Point(x, y, heightMap[x][y])] = heightMap.adjacent(x, y).mapNotNull { it }
            }
        }

        return adjacent
            .filter { entry -> entry.value.all { entry.key.value < it } }
            .keys
            .sumOf { it.value + 1 }
    }

    private fun HeightMap.adjacent(
        x: Int,
        y: Int
    ): List<Int?> {
        return listOf(
            this.safeGet(x + 1, y),
            this.safeGet(x - 1, y),
            this.safeGet(x, y + 1),
            this.safeGet(x, y - 1)
        )
    }

    private fun HeightMap.safeGet(x: Int, y: Int): Int? = this.getOrNull(x)?.getOrNull(y)

    @Test
    fun part1() =
        Assertions.assertEquals(
            585,
            computeResult { `What is the sum of the risk levels of all low points on your heightmap`(it) }
        )
}