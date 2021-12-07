import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.abs

typealias Position = Int

class Day7 : Day(7, "The Treachery of Whales") {
    private fun potentialPositionsFrom(horizontalPositions: List<Position>): Sequence<Int> =
        (horizontalPositions.minOf { it }..horizontalPositions.maxOf { it }).toList().asSequence()

    private fun `How much fuel must they spend to align to the cheapest position ?`(
        crabsPositions: List<Position>,
        fuelCalculator: (difference: Int) -> Int
    ): Int = potentialPositionsFrom(crabsPositions)
        .minOf { position -> crabsPositions.sumOf { fuelCalculator(abs(it - position)) } }

    @Test
    fun exercise1() =
        Assertions.assertEquals(
            331067,
            computeIntSeparatedResult { input ->
                `How much fuel must they spend to align to the cheapest position ?`(input) { it }
            })


    @Test
    fun exercise2() =
        Assertions.assertEquals(
            92881128,
            computeIntSeparatedResult { input ->
                `How much fuel must they spend to align to the cheapest position ?`(input) { difference ->
                    difference * (difference + 1) / 2
                }
            })
}