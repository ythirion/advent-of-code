import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private typealias Compartment = String
private typealias Rucksack = Pair<Compartment, Compartment>
class Day3 : Day(3, "Rucksack Reorganization") {
    private val priorities = (('a'..'z') + ('A' .. 'Z'))
        .mapIndexed { i, c -> Pair(c, i + 1) }
        .toMap()

    private fun Rucksack.misplacedItem(): Char =
        first.toSet()
            .find { item -> second.contains(item) }!!

    private fun String.toRucksack(): Rucksack =
        (this.length / 2).let { half ->
            Rucksack(substring(0, half), substring(half, length))
        }

    private fun List<String>.toRucksacks(): List<Rucksack> = map { it.toRucksack() }

    private fun List<Rucksack>.calculatePriorities(): Int =
        this.map { it.misplacedItem() }
            .sumOf { priorities[it]!! }

    @Test
    fun part1() =
        assertEquals(157,
            computeResult {
                it.toRucksacks()
                    .calculatePriorities()
            }
        )
}