import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

typealias Age = Int
typealias Count = Long

class Day6 : Day(6, "Lanternfish") {
    private val birthAge = 8
    private val resetAge = 6

    private fun Map<Age, Count>.safeGet(index: Int): Long = this[index] ?: 0L

    private fun Map<Age, Count>.dayPassed(): Map<Age, Count> {
        val birth = this.safeGet(0)
        val newState = mutableMapOf<Age, Count>()

        (0 until birthAge).forEach { newState[it] = this.safeGet(it + 1) }

        newState[resetAge] = this.safeGet(resetAge + 1) + birth
        newState[birthAge] = birth

        return newState
    }

    private fun Map<Age, Count>.grow(daysRemaining: Int): Map<Age, Count> {
        return this.dayPassed()
            .let { newState ->
                if (daysRemaining != 1) newState.grow(daysRemaining - 1)
                else newState
            }
    }

    private fun `How many lanternfish would there be after x days`(
        lanternFiches: List<Age>,
        days: Int
    ): Long {
        return lanternFiches.groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }
            .grow(days).values
            .sum()
    }

    @Test
    fun exercise1() =
        Assertions.assertEquals(362740,
            computeIntSeparatedResult { `How many lanternfish would there be after x days`(it, 80) })

    @Test
    fun exercise2() =
        Assertions.assertEquals(1644874076764,
            computeIntSeparatedResult { `How many lanternfish would there be after x days`(it, 256) })
}