import io.vavr.kotlin.toVavrList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day1 : Day(1) {
    private fun calculateFor2020(lines: List<String>, entries: Int): Int =
            lines.map { it.toInt() }
                    .toVavrList()
                    .crossProduct(entries)
                    .filter { it.sum() == 2020L }
                    .getOrElseThrow { IllegalStateException("No valid input.") }
                    .multiply();

    @Test
    fun exercise1() = Assertions.assertEquals(73371, computeResult { calculateFor2020(it, 2) })

    @Test
    fun exercise2() = Assertions.assertEquals(127642310, computeResult { calculateFor2020(it, 3) })
}