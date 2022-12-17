import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

typealias Crate = Char
typealias Crates = Stack<Crate>
typealias Ship = List<Crates>

private data class Step(val size: Int, val from: Int, val to: Int)

private abstract class CrateMover {
    fun rearrange(ship: Ship, steps: List<Step>): Ship =
        ship.let {
            steps.forEach { step -> apply(ship, step) }
            return it
        }

    abstract fun apply(ship: Ship, step: Step)
}

private class CrateMover9000 : CrateMover() {
    override fun apply(ship: Ship, step: Step) {
        repeat(step.size) {
            ship[step.to].push(
                ship[step.from].pop()
            )
        }
    }
}

private class CrateMover9001 : CrateMover() {
    override fun apply(ship: Ship, step: Step) {
        ship[step.from]
            .takeLast(step.size)
            .forEach {
                ship[step.to].push(it)
                ship[step.from].pop()
            }
    }
}

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
            .let { (size, from, to) -> Step(size.toInt(), from.toInt() - 1, to.toInt() - 1) }

    private fun String.toSteps(): List<Step> =
        lines().map { it.toInstruction() }

    private fun List<String>.rearrangeShip(crateMover: CrateMover): String =
        Pair(this[0].toShip(), this[1].toSteps())
            .let { (ship, steps) -> crateMover.rearrange(ship, steps) }
            .map { crates -> crates.lastElement() }
            .joinToString("")


    @Test
    fun part1() =
        assertEquals("ZRLJGSCTR",
            computeStringSeparatedLinesResult {
                it.rearrangeShip(CrateMover9000())
            }
        )

    @Test
    fun part2() =
        assertEquals("PRTTGRFPB",
            computeStringSeparatedLinesResult {
                it.rearrangeShip(CrateMover9001())
            }
        )
}