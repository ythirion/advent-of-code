import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day6 : Day(6, "Tuning Trouble") {
    private fun String.firstMarker(): String =
        drop(1)
            .windowed(4, 1)
            .first { it.areAllCharDifferent() }

    private fun String.indexOfFirstMarker(): Int =
        firstMarker().let {
            indexOf(it) + it.length
        }

    @Test
    fun part1() =
        assertEquals(5,
            computeStringResult {
                it.indexOfFirstMarker()
            }
        )
}