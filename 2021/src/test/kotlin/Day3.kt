import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day3 : Day(3, "Binary Diagnostic") {
    private enum class Mode {
        MostCommon, LeastCommon
    }

    private fun List<String>.calculateRate(mode: Mode): Int {
        return (0 until this[0].length).map { index ->
            this.column(index)
                .groupingBy { it }
                .eachCount()
                .let { entry ->
                    when (mode) {
                        Mode.MostCommon -> entry.maxByOrNull { it.value }!!
                        Mode.LeastCommon -> entry.minByOrNull { it.value }!!
                    }.key
                }
        }.joinToString("").binaryToInt()
    }

    private fun gammaRate(lines: List<String>): Int = lines.calculateRate(Mode.MostCommon)

    private fun epsilonRate(lines: List<String>): Int = lines.calculateRate(Mode.LeastCommon)

    private fun List<String>.filter(mode: Mode): Int {
        var remainingNumbers = this
        var number = ""

        (0 until this[0].length).map { index ->
            if (remainingNumbers.size > 1) {
                remainingNumbers = filterRating(remainingNumbers, mode, index)

                if (remainingNumbers.isNotEmpty()) {
                    number = remainingNumbers.first()
                }
            }
        }
        return number.binaryToInt()
    }

    private fun oxygenGeneratorRating(lines: List<String>): Int = lines.filter(Mode.MostCommon)
    private fun co2ScrubberRating(lines: List<String>): Int = lines.filter(Mode.LeastCommon)

    private fun filterRating(
        remainingNumbers: List<String>,
        mode: Mode,
        index: Int
    ): List<String> {
        remainingNumbers.column(index)
            .groupingBy { it }
            .eachCount()
            .let { entry ->
                return remainingNumbers.filter {
                    val expectedBitAtIndex = when (mode) {
                        Mode.MostCommon -> {
                            if (entry['1']!! >= entry['0']!!) '1'
                            else '0'
                        }
                        Mode.LeastCommon -> {
                            if (entry['0']!! <= entry['1']!!) '0'
                            else '1'
                        }
                    }
                    it.at(index, expectedBitAtIndex)
                }
            }
    }

    @Test
    fun exercise1() =
        Assertions.assertEquals(
            2035764,
            computeResult { gammaRate(it) * epsilonRate(it) }
        )

    @Test
    fun exercise2() =
        Assertions.assertEquals(
            2817661,
            computeResult { oxygenGeneratorRating(it) * co2ScrubberRating(it) }
        )
}