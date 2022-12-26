import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.pow

class Day25 : Day(25, "Full of Hot Air") {
    @JvmInline
    value class SNAFU(private val number: String) {
        fun toDecimal(): Long =
            number.reversed()
                .map(::toInt)
                .mapIndexed { i, d -> 5.0.pow(i.toDouble()).toLong() * d }
                .sum()

        private fun toInt(it: Char): Int = when (it) {
            '=' -> -2
            '-' -> -1
            else -> it.toIntDigit()
        }

        companion object SNAFU {
            
        }
    }

    private fun List<SNAFU>.sum(): Long = sumOf { it.toDecimal() }

    private fun List<String>.toSNAFUs(): List<SNAFU> = map { SNAFU(it) }

    @Test
    fun part1() {
        assertEquals(
            4890,
            computeResult {
                it.toSNAFUs()
                    .sum()
            }
        )
    }
}