import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.abs

typealias Position = Int

class Day7 : Day(7, "The Treachery of Whales") {
    private fun Position.constantFuelCostAt(horizontalPositions: List<Int>): Int =
        horizontalPositions.sumOf { abs(it - this) }

    private fun Position.fuelCost(to: Int): Int {
        val cost = abs(to - this)
        return (0 until cost).fold(cost) { acc, i -> acc + i }
    }

    private fun Position.fuelCostAt(horizontalPositions: List<Int>): Int =
        horizontalPositions.sumOf { this.fuelCost(it) }

    private fun generatePotentialPositions(horizontalPositions: List<Position>): Sequence<Int> =
        (horizontalPositions.minOf { it }..horizontalPositions.maxOf { it }).toList().asSequence()

    private

    fun `How much fuel must they spend to align to the cheapest position ?`(
        horizontalPositions: List<Position>,
        fuelCalculator: (Position, List<Position>) -> Int
    ): Int {
        return generatePotentialPositions(horizontalPositions)
            .distinct()
            .sorted()
            .map { fuelCalculator(it, horizontalPositions) }
            .sorted()
            .first()
    }

    @Test
    fun exercise1() =
        Assertions.assertEquals(
            331067,
            computeIntSeparatedResult {
                `How much fuel must they spend to align to the cheapest position ?`(
                    it
                ) { position, horizontalPositions -> position.constantFuelCostAt(horizontalPositions) }
            })


    @Test
    fun exercise2() =
        Assertions.assertEquals(
            92881128,
            computeIntSeparatedResult {
                `How much fuel must they spend to align to the cheapest position ?`(
                    it
                ) { position, horizontalPositions -> position.fuelCostAt(horizontalPositions) }
            })
}