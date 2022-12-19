import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

typealias Instructions = List<String>

class Day10 : Day(10, "Cathode-Ray Tube") {
    private val measure = listOf(20, 60, 100, 140, 180, 220)

    private fun Instructions.execute(): Int {
        var x = 1
        var cycles = 0
        var sumStrength = 0

        fun cycle() {
            if ((cycles + 1) in measure)
                sumStrength += (cycles + 1) * x
            cycles += 1
        }

        forEach { instruction ->
            when (instruction) {
                "noop" -> cycle()
                else -> {
                    repeat(2) { cycle() }
                    x += instruction.splitWords()[1].toInt()
                }
            }
        }
        return sumStrength
    }

    @Test
    fun part1() {
        assertEquals(
            16880,
            computeResult {
                it.execute()
            }
        )
    }
}