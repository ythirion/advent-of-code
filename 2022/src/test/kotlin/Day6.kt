import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day6 : Day(6, "Tuning Trouble") {
    private fun String.firstMarker(size: Int) = drop(1)
        .windowed(size, 1)
        .first { it.areAllCharDifferent() }

    private fun String.firstStartOfMarker(): String = firstMarker(4)

    private fun String.indexOfFirstStartOfMarker(): Int =
        firstStartOfMarker().let {
            indexOf(it) + it.length
        }

    @Test
    fun part1() =
        assertEquals(1093,
            computeStringResult {
                it.indexOfFirstStartOfMarker()
            }
        )
}