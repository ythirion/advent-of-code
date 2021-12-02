import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day1 : Day(1, "Sonar Sweep") {
    private fun `number of times a depth measurement increases`(
        lines: List<String>,
        size: Int
    ): Int =
        lines.toInts()
            .windowed(size) { it.sum() }
            .windowed(2).count { it.last() > it.first() }

    @Test
    fun exercise1() =
        Assertions.assertEquals(1393, computeResult { `number of times a depth measurement increases`(it, 1) })

    @Test
    fun exercise2() =
        Assertions.assertEquals(1359, computeResult { `number of times a depth measurement increases`(it, 3) })
}