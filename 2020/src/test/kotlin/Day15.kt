import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day15 : Day(15) {
    private fun List<Int>.toMap(): MutableMap<Int, Int> {
        val previousNumbers = mutableMapOf<Int, Int>()
        this.subList(0, this.size - 1).forEachIndexed { i, v -> previousNumbers[v] = i }
        
        return previousNumbers
    }

    private fun guess(last: Int, input: List<Int>): Int {
        val previousNumbers = input.toMap()
        var lastNumber = input.last()

        (previousNumbers.size until last - 1).forEach { turn ->
            val newNumber = if (previousNumbers.containsKey(lastNumber)) turn - previousNumbers.getOrDefault(lastNumber, 0) else 0
            previousNumbers[lastNumber] = turn
            lastNumber = newNumber
        }
        return lastNumber
    }

    @Test
    fun exercise1() = computeIntSeparatedResult {
        Assertions.assertEquals(403, guess(2020, it))
    }

    @Test
    fun exercise2() = computeIntSeparatedResult {
        Assertions.assertEquals(6823, guess(30000000, it))
    }
}