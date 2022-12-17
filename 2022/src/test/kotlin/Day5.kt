import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

typealias Crate = Char
typealias Crates = Stack<Crate>
typealias Ship = List<Crates>

private data class Step(val size: Int, val from: Int, val to: Int)
class Day5 : Day(5, "Supply Stacks") {
    private val instructionRegex = Regex("""move (\d+) from (\d+) to (\d+)""")
    private fun List<String>.cratesIndexes(): List<Int> =
        this[0].mapIndexedNotNull { index, c ->
            if (c.isDigit()) index else null
        }

    private fun List<String>.toCrates(index: Int): Crates =
        mapNotNull { it.elementAtOrNull(index) }
            .filter { it != ' ' }
            .toStack()

    private fun String.toShip(): Ship =
        lines().reversed().let {
            it.cratesIndexes()
                .map { column -> it.drop(1).toCrates(column) }
        }

    private fun String.toInstruction(): Step =
        instructionRegex.matchEntire(this)!!
            .destructured
            .let { (size, from, to) -> Step(size.toInt(), from.toInt(), to.toInt()) }

    private fun String.toSteps(): List<Step> =
        lines().map { it.toInstruction() }

    private fun Ship.applyStep(step: Step): Unit =
        repeat(step.size) {
            this[step.to - 1].push(
                this[step.from - 1].pop()
            )
        }

    private fun Ship.rearrange(steps: List<Step>) =
        steps.forEach { applyStep(it) }

    private fun List<String>.rearrangeShip(): Ship =
        Pair(this[0].toShip(), this[1].toSteps())
            .let { (ship, steps) ->
                ship.rearrange(steps)
                return ship
            }

    private fun Ship.endsUpWith(): String =
        map { crates -> crates.lastElement() }
            .joinToString("")

    @Test
    fun part1() =
        assertEquals("ZRLJGSCTR",
            computeStringSeparatedLinesResult {
                it.rearrangeShip()
                    .endsUpWith()
            }
        )
}