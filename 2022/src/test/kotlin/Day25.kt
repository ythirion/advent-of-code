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
    }

    private fun Long.toSNAFU(): SNAFU {
        fun symbol(digit: Long): String =
            when (digit) {
                3L -> "="
                4L -> "-"
                else -> digit.toString()
            }

        fun convertRecursively(decimal: Long, digits: MutableList<Long>): List<Long> {
            var remaining = 0
            val digit = decimal % 5

            if(digit >= 3) remaining = 5

            var result = (decimal + remaining) / 5
            digits.add(digit)

            return if(result != 0L) convertRecursively(result, digits) else digits
        }

        fun convert(decimal: Long): List<Long> = convertRecursively(decimal, mutableListOf())

        return SNAFU(convert(this)
            .reversed()
            .joinToString("") { symbol(it) })
    }

    private fun List<SNAFU>.sum(): Long = sumOf { it.toDecimal() }

    private fun List<String>.toSNAFUs(): List<SNAFU> = map { SNAFU(it) }

    @Test
    fun part1() {
        assertEquals(
            SNAFU("2-=2-0=-0-=0200=--21"),
            computeResult {
                it.toSNAFUs()
                    .sum()
                    .toSNAFU()
            }
        )
    }
}