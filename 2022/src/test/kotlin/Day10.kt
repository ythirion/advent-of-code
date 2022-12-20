import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.abs

typealias Instructions = List<String>
typealias Signals = MutableList<Int>
typealias CRT = MutableList<Point2D>

class Day10 : Day(10, "Cathode-Ray Tube") {
    private val measure = listOf(20, 60, 100, 140, 180, 220)

    private data class Memory(val cycles: Int, val x: Int)

    private fun Memory.updateX(add: Int): Memory = copy(x = x + add)
    private fun Memory.needToMeasure(): Boolean = cycles + 1 in measure
    private fun Memory.cycle(
        signals: Signals,
        crt: CRT
    ): Memory =
        copy(cycles = cycles + 1).let {
            if (needToMeasure())
                signals += (cycles + 1) * x

            if (abs(cycles % 40 - x) < 2)
                crt.add(Point2D(cycles % 40, cycles / 40))

            return it
        }

    private fun Instructions.execute(): List<Int> =
        Memory(0, 1).let {
            var memory = it
            val signals = mutableListOf<Int>()
            val crt = mutableListOf<Point2D>()

            forEach { instruction ->
                when (instruction) {
                    "noop" -> memory = memory.cycle(signals, crt)
                    else -> {
                        repeat(2) { memory = memory.cycle(signals, crt) }
                        memory = memory.updateX(instruction.splitWords()[1].toInt())
                    }
                }
            }
            println(crt.draw())

            return signals
        }

    @Test
    fun part1() {
        assertEquals(
            16880,
            computeResult {
                it.execute().sum()
            }
        )
    }
}