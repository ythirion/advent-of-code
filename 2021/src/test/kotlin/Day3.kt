import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day3 : Day(3, "Binary Diagnostic") {
    private enum class Mode {
        MostCommon, LessCommon
    }

    private fun List<String>.calculateRate(mode: Mode): Int {
        return (0 until this.first().length).map { column ->
            this.column(column)
                .groupingBy { it }
                .eachCount()
                .entries
                .let { entry ->
                    when (mode) {
                        Mode.MostCommon -> entry.maxByOrNull { it.value }!!
                        Mode.LessCommon -> entry.minByOrNull { it.value }!!
                    }.key
                }
        }.joinToString("").binaryToInt()
    }

    private fun gammaRate(lines: List<String>): Int = lines.calculateRate(Mode.MostCommon)

    private fun epsilonRate(lines: List<String>): Int = lines.calculateRate(Mode.LessCommon)

    @Test
    fun exercise1() =
        Assertions.assertEquals(
            2035764,
            computeResult { gammaRate(it) * epsilonRate(it) }
        )

    @Test
    fun exercise2() =
        Assertions.assertEquals(
            2035764,
            computeResult { gammaRate(it) * epsilonRate(it) }
        )
}