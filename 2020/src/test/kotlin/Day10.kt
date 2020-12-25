import io.vavr.kotlin.toVavrList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day10 : Day(10) {
    private fun rate(values: List<Int>) =
            values.sorted().let {
                it.mapIndexed { i, v -> if (i == 0) v else v - it[i - 1] }
            }

    private fun calculateDifferences(values: List<Int>): Int =
            rate(values).let { rates -> (rates.count { it == 3 } + 1) * rates.count { it == 1 } }

    private fun calculateCombinations(values: List<Int>): Long {
        val adapters = (values.map { it.toInt() } + 0).sorted().toVavrList()
        val ways = mutableMapOf(0 to 1L)

        adapters.tail().forEach { index ->
            ways[index] = listOf(1, 2, 3)
                    .filter { it <= index }
                    .sumOf { ways.getOrDefault(index - it, 0L) }
        }
        return ways.getOrDefault(ways.keys.max() ?: 0L, 0L)
    }

    @Test
    fun exercise1() = computeIntResult {
        Assertions.assertEquals(1848, calculateDifferences(it))
    }

    @Test
    fun exercise2() = computeIntResult {
        Assertions.assertEquals(8099130339328, calculateCombinations(it))
    }
}