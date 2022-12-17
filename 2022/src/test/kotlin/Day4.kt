import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private typealias Sections = IntRange
private typealias Elves = Pair<Sections, Sections>

class Day4 : Day(4, "Camp Cleanup") {
    private fun String.toSections(): Sections =
        split("-")
            .let { it[0].toInt()..it[1].toInt() }

    private fun String.toElves(): Elves =
        this.split(",")
            .let { Pair(it[0].toSections(), it[1].toSections()) }

    private fun Elves.areSectionsContain(): Boolean =
        first.intersect(second).size
            .let { it == first.count() || it == second.count() }

    private fun Elves.areSectionsOverlap(): Boolean =
        first.plus(second)
            .groupingBy { it }
            .eachCount()
            .any { it.value > 1 }

    private fun List<String>.howManyAssignmentPairs(predicate: (Elves) -> Boolean): Int =
        map { it.toElves() }
            .count { predicate(it) }

    @Test
    fun part1() =
        assertEquals(580,
            computeResult {
                it.howManyAssignmentPairs { elves ->
                    elves.areSectionsContain()
                }
            }
        )

    @Test
    fun part2() =
        assertEquals(895,
            computeResult {
                it.howManyAssignmentPairs { elves ->
                    elves.areSectionsOverlap()
                }
            }
        )
}