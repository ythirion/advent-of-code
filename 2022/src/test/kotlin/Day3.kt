import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private typealias Compartment = String
private typealias Rucksack = Pair<Compartment, Compartment>
private typealias Group = List<Rucksack>
class Day3 : Day(3, "Rucksack Reorganization") {
    private val priorities = (('a'..'z') + ('A' .. 'Z'))
        .mapIndexed { i, c -> Pair(c, i + 1) }
        .toMap()

    private fun Rucksack.misplacedItem(): Char =
        first.toSet()
            .first { item -> second.contains(item) }

    private fun String.toRucksack(): Rucksack =
        (this.length / 2).let { half ->
            Rucksack(substring(0, half), substring(half, length))
        }

    private fun List<String>.toRucksacks(): List<Rucksack> = map { it.toRucksack() }

    private fun List<Rucksack>.organizeInGroups(): List<Group> = this.chunked(3)
    private fun Rucksack.allItems(): String = first + second

    private fun List<Char>.calculatePriorities(): Int = sumOf { priorities[it]!! }

    private fun Group.badge(): Char =
        this.elementAt(0)
            .allItems()
            .first {item ->
                elementAt(1).allItems().contains(item)
                        && elementAt(2).allItems().contains(item)
            }

    @Test
    fun part1() =
        assertEquals(7568,
            computeResult {
                it.toRucksacks()
                    .map { rucksack -> rucksack.misplacedItem() }
                    .calculatePriorities()
            }
        )

    @Test
    fun part2() =
        assertEquals(2780,
            computeResult {
                it.toRucksacks()
                    .organizeInGroups()
                    .map { group -> group.badge() }
                    .calculatePriorities()
            }
        )
}