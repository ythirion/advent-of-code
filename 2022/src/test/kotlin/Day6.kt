import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day6 : Day(6, "Tuning Trouble") {
    private fun String.firstMarker(size: Int) = drop(1)
        .windowed(size, 1)
        .first { it.areAllCharDifferent() }

    private fun String.firstStartOfMarker(): String = firstMarker(4)
    private fun String.firstStartOfMessageMarker(): String = firstMarker(14)

    private fun String.indexOfFirst(marker: String): Int =
        marker.let {
            indexOf(it) + it.length
        }

    @Test
    fun part1() =
        assertEquals(1093,
            computeStringResult {
                it.indexOfFirst(it.firstStartOfMarker())
            }
        )

    @Test
    fun part2() =
        assertEquals(3534,
            computeStringResult {
                it.indexOfFirst(it.firstStartOfMessageMarker())
            }
        )
}