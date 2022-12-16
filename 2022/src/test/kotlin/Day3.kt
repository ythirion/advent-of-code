import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private typealias Rucksack = String
private typealias Group = List<Rucksack>
class Day3 : Day(3, "Rucksack Reorganization") {
    private val priorities = (('a'..'z') + ('A' .. 'Z'))
        .mapIndexed { i, c -> Pair(c, i + 1) }
        .toMap()

    private fun Rucksack.misplacedItem(): Char =
        splitInCompartments().let { compartments ->
            compartments.first
            .toSet()
            .first { item -> compartments.second.contains(item) }
        }

    private fun String.splitInCompartments(): Pair<String, String> =
        (this.length / 2).let { half ->
            Pair(substring(0, half), substring(half, length))
        }

    private fun List<Rucksack>.organizeInGroups(): List<Group> = this.chunked(3)
    private fun List<Char>.calculatePriorities(): Int = sumOf { priorities[it]!! }

    private fun Group.badge(): Char =
        elementAt(0)
            .keepOnlyItemsAlsoIn(elementAt(1))
            .keepOnlyItemsAlsoIn(elementAt(2))
            .single()

    private fun Rucksack.keepOnlyItemsAlsoIn(other: Rucksack): String =
        this.toSet()
            .filter { i -> other.contains(i) }
            .joinToString()

    @Test
    fun part1() =
        assertEquals(7568,
            computeResult {
                it.map { rucksack -> rucksack.misplacedItem() }
                    .calculatePriorities()
            }
        )

    @Test
    fun part2() =
        assertEquals(2780,
            computeResult {
                it.organizeInGroups()
                    .map { group -> group.badge() }
                    .calculatePriorities()
            }
        )
}