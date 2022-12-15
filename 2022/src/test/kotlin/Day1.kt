import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

typealias Elf = List<Int>

class Day1 : Day(1, "Calorie Counting") {
    private fun String.toElf(): Elf = this.splitLines().map { it.toInt() }
    private fun Elf.calories(): Int = this.sum()
    private fun List<String>.toElves() = this.map { x -> x.toElf() }

    private fun `how many Calories are being carried by the Elf carrying the most Calorie`(elves: List<Elf>) =
        elves.maxOf { it.calories() }

    private fun `top three Elves carrying the most Calories total of their calories`(elves: List<Elf>) =
        elves
            .map { it.calories() }
            .sortedDescending()
            .take(3)
            .sum()

    @Test
    fun part1() =
        assertEquals(66719,
            computeStringSeparatedLinesResult {
                `how many Calories are being carried by the Elf carrying the most Calorie`(
                    it.toElves()
                )
            }
        )

    @Test
    fun part2() =
        assertEquals(198551,
            computeStringSeparatedLinesResult {
                `top three Elves carrying the most Calories total of their calories`(
                    it.toElves()
                )
            }
        )
}