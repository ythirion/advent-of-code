import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.abs

class Day7 : Day(7, "The Treachery of Whales") {
    private fun fuelCostAt(
        position: Int,
        horizontalPositions: List<Int>
    ): Int = horizontalPositions.sumOf { abs(it - position) }

    private fun `How much fuel must they spend to align to the cheapest position ?`(horizontalPositions: List<Int>): Int {
        return horizontalPositions.asSequence().distinct()
            .sorted()
            .map { fuelCostAt(it, horizontalPositions) }
            .sorted()
            .first()
    }

    @Test
    fun exercise1() =
        Assertions.assertEquals(
            6397,
            computeIntSeparatedResult { `How much fuel must they spend to align to the cheapest position ?`(it) }
        )
}