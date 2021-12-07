import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.abs

typealias Position = Int

class Day7 : Day(7, "The Treachery of Whales") {
    private fun List<Position>.potentialPositions() = minOf { it }..maxOf { it }
    private fun Position.distance(position: Int) = abs(this - position)

    private fun `How much fuel must they spend to align to the cheapest position ?`(
        crabsPositions: List<Position>,
        fuelCalculator: (distance: Int) -> Int
    ): Int = crabsPositions.potentialPositions()
        .minOf { position -> crabsPositions.sumOf { fuelCalculator(it.distance(position)) } }


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
                `How much fuel must they spend to align to the cheapest position ?`(input) { distance ->
                    distance * (distance + 1) / 2
                }
            })
}