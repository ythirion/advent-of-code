import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day8 : Day(8, "Seven Segment Search") {
    private val digitSegments = mapOf(0 to 6, 1 to 2, 2 to 5, 3 to 5, 4 to 4, 5 to 5, 6 to 6, 7 to 3, 8 to 7, 9 to 6)

    private fun dirtySolve(input: List<String>): Int {
        return input.sumOf { line ->
            line.split("|")[1]
                .split(" ")
                .count {
                    it.length == digitSegments[1] || it.length == digitSegments[4] ||
                            it.length == digitSegments[7] || it.length == digitSegments[8]
                }
        }
    }

    @Test
    fun part1() =
        Assertions.assertEquals(
            349,
            computeResult { dirtySolve(it) })
}