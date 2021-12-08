import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day8 : Day(8, "Seven Segment Search") {
    private val digitSegments = mapOf(0 to 6, 1 to 2, 2 to 5, 3 to 5, 4 to 4, 5 to 5, 6 to 6, 7 to 3, 8 to 7, 9 to 6)

    private fun dirtySolve(input: List<String>): Int {
        val searchFor = listOf(1, 4, 7, 8).map { digitSegments[it] }
        return input.sumOf { line ->
            line.split("|")[1]
                .split(" ")
                .count { searchFor.contains(it.length) }
        }
    }

    @Test
    fun part1() =
        Assertions.assertEquals(
            349,
            computeResult { dirtySolve(it) })
}