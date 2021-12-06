import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

typealias LanternFish = Int

class Day6 : Day(6, "Lanternfish") {
    private fun simulateDay(lanternFiches: MutableList<LanternFish>) {
        var birth = 0
        lanternFiches.replaceAll { lanternFish ->
            when (lanternFish) {
                0 -> {
                    birth++
                    6
                }
                else -> lanternFish - 1
            }
        }
        lanternFiches += Array(birth) { _ -> 8 }
    }

    private fun `How many lanternfish would there be after x days`(
        lanternFiches: MutableList<LanternFish>,
        days: Int
    ): Int {
        for (day in days downTo 1) {
            simulateDay(lanternFiches)
        }
        return lanternFiches.size
    }

    @Test
    fun exercise1() =
        Assertions.assertEquals(362740,
            computeIntSeparatedResult { `How many lanternfish would there be after x days`(it.toMutableList(), 80) })
}