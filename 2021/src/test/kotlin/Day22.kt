import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.max
import kotlin.math.min

class Day22 : Day(22, "Reactor Reboot") {
    private val stepRegex = Regex("(on|off) x=(-?\\d+)..(-?\\d+),y=(-?\\d+)..(-?\\d+),z=(-?\\d+)..(-?\\d+)")

    data class Step(
        val state: Int,
        val xMin: Long,
        val yMin: Long,
        val zMin: Long,
        val xMax: Long,
        val yMax: Long,
        val zMax: Long
    )

    private fun Step.countCubesOn(): Long = state * (xMax - xMin + 1) * (yMax - yMin + 1) * (zMax - zMin + 1)
    private fun List<Step>.countCubesOn(): Long = sumOf { it.countCubesOn() }
    private fun Step.isOn(): Boolean = state == 1
    private fun Step.toList(): List<Long> = listOf(xMin, xMax, yMin, yMax, zMin, zMax)
    private fun Step.overlapOrNull(other: Step): Step? {
        val x1 = max(other.xMin, this.xMin)
        val x2 = min(other.xMax, this.xMax)
        val y1 = max(other.yMin, this.yMin)
        val y2 = min(other.yMax, this.yMax)
        val z1 = max(other.zMin, this.zMin)
        val z2 = min(other.zMax, this.zMax)

        // Overlapping so reverse the stats
        return if (x1 <= x2 && y1 <= y2 && z1 <= z2)
            Step(-state, x1, y1, z1, x2, y2, z2)
        else null
    }

    private fun IntRange.contains(step: Step): Boolean = step.toList().all { contains(it) }
    private fun String.stateToInt(): Int = if (this == "on") 1 else -1

    private fun String.toStep(): Step =
        stepRegex.matchEntire(this)!!
            .destructured
            .let { (onOrOff, xMin, xMax, yMin, yMax, zMin, zMax) ->
                Step(
                    onOrOff.stateToInt(),
                    xMin.toLong(),
                    yMin.toLong(),
                    zMin.toLong(),
                    xMax.toLong(),
                    yMax.toLong(),
                    zMax.toLong()
                )
            }

    private fun List<Step>.rebootReactor(): Long =
        mutableListOf<Step>().also { cubes ->
            this.forEach { step ->
                cubes.addAll(cubes.mapNotNull { it.overlapOrNull(step) })
                if (step.isOn()) cubes.add(step)
            }
        }.countCubesOn()

    @Test
    fun part1() =
        Assertions.assertEquals(
            601104,
            computeResult({ it.map { line -> line.toStep() } },
                { steps ->
                    steps.filter { (-50..50).contains(it) }
                        .rebootReactor()
                })
        )

    @Test
    fun part2() =
        Assertions.assertEquals(
            1262883317822267,
            computeResult({ it.map { line -> line.toStep() } },
                { it.rebootReactor() })
        )
}