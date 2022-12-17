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

    private fun Elves.areSectionsContained(): Boolean =
        first.intersect(second).size
            .let { it == first.count() || it == second.count() }

    @Test
    fun part1() =
        assertEquals(580,
            computeResult {
                it.map { line -> line.toElves() }
                    .count { elves -> elves.areSectionsContained() }
            }
        )
}