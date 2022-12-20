import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

typealias Instructions = List<String>

class Day10 : Day(10, "Cathode-Ray Tube") {
    private val measure = listOf(20, 60, 100, 140, 180, 220)

    private data class Memory(val cycles: Int, val x: Int)

    private fun Memory.updateX(add: Int): Memory = copy(x = x + add)
    private fun Memory.needToMeasure(): Boolean = cycles + 1 in measure
    private fun Memory.cycle(signals: MutableList<Int>): Memory =
        copy(cycles = cycles + 1).let {
            if (needToMeasure())
                signals += (cycles + 1) * x
            return it
        }

    private fun Instructions.execute(): List<Int> =
        Memory(0, 1).let {
            var memory = it
            val signals = mutableListOf<Int>()

            forEach { instruction ->
                when (instruction) {
                    "noop" -> memory = memory.cycle(signals)
                    else -> {
                        repeat(2) { memory = memory.cycle(signals) }
                        memory = memory.updateX(instruction.splitWords()[1].toInt())
                    }
                }
            }
            return signals
        }

    @Test
    fun part1() {
        assertEquals(
            16880,
            computeResult {
                it.execute()
                    .sum()
            }
        )
    }
}