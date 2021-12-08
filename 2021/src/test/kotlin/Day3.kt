import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day3 : Day(3, "Binary Diagnostic") {
    private enum class Mode {
        MostCommon, LeastCommon
    }

    private fun List<String>.groupBitsAt(index: Int): Map<Char, Int> {
        return this.column(index)
            .groupingBy { it }
            .eachCount()
    }

    private fun List<String>.calculateConsumptionRate(mode: Mode): Int {
        return (0 until this[0].length).map { index ->
            groupBitsAt(index)
                .let { group ->
                    when (mode) {
                        Mode.MostCommon -> group.maxByOrNull { it.value }!!
                        Mode.LeastCommon -> group.minByOrNull { it.value }!!
                    }.key
                }
        }.joinToString("").binaryToInt()
    }

    private fun gammaRate(lines: List<String>): Int = lines.calculateConsumptionRate(Mode.MostCommon)
    private fun epsilonRate(lines: List<String>): Int = lines.calculateConsumptionRate(Mode.LeastCommon)
    private fun powerConsumption(lines: List<String>) = gammaRate(lines) * epsilonRate(lines)

    private fun List<String>.filterLifeRate(mode: Mode): Int {
        var remainingNumbers = this
        var number = ""

        (0 until this[0].length).map { index ->
            if (remainingNumbers.size > 1) {
                remainingNumbers = filterLifeRate(remainingNumbers, mode, index)

                if (remainingNumbers.isNotEmpty()) {
                    number = remainingNumbers.first()
                }
            }
        }
        return number.binaryToInt()
    }

    private fun filterLifeRate(
        remainingNumbers: List<String>,
        mode: Mode,
        index: Int
    ): List<String> {
        remainingNumbers.groupBitsAt(index)
            .let { group ->
                return remainingNumbers.filter { binaryNumber ->
                    binaryNumber.at(
                        index, when (mode) {
                            Mode.MostCommon -> if (group['1']!! >= group['0']!!) '1' else '0'
                            Mode.LeastCommon -> if (group['0']!! <= group['1']!!) '0' else '1'
                        }
                    )
                }
            }
    }

    private fun oxygenGeneratorRate(lines: List<String>): Int = lines.filterLifeRate(Mode.MostCommon)
    private fun co2ScrubberRate(lines: List<String>): Int = lines.filterLifeRate(Mode.LeastCommon)
    private fun lifeSupportRate(lines: List<String>): Int = oxygenGeneratorRate(lines) * co2ScrubberRate(lines)

    @Test
    fun part1() =
        Assertions.assertEquals(
            2035764,
            computeResult { powerConsumption(it) }
        )


    @Test
    fun part2() =
        Assertions.assertEquals(
            2817661,
            computeResult { lifeSupportRate(it) }
        )
}