import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

typealias LanternFish = Int

class Day6 : Day(6, "Lanternfish") {
    private fun simulateDay(lanternFiches: List<LanternFish>): List<LanternFish> {
        var birth = 0
        val newStates = lanternFiches.map { lanternFish ->
            when (lanternFish) {
                0 -> {
                    birth++
                    6
                }
                else -> lanternFish - 1
            }
        }
        return newStates.toMutableList()
            .add(Array(birth) { _ -> 8 }.toList())
    }

    private fun `How many lanternfish would there be after x days`(
        lanternFiches: List<LanternFish>,
        days: Int
    ): Int {
        var state = lanternFiches
        for (day in days downTo 1) {
            state = simulateDay(state)
        }
        return state.size
    }


    @Test
    fun exercise1() =
        Assertions.assertEquals(362740,
            computeIntSeparatedResult { `How many lanternfish would there be after x days`(it, 80) })
}