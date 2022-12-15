import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

typealias Elf = List<Int>

class Day1 : Day(1, "Calorie Counting") {
    private fun String.toElf(): Elf = this.splitLines().map { it.toInt() }
    private fun Elf.calories(): Int = this.sum()

    private fun `how many Calories are being carried by the Elf carrying the most Calorie`(elfes: List<Elf>) =
        elfes.maxOf { it.calories() }

    @Test
    fun part1() =
        Assertions.assertEquals(66719,
            computeStringSeparatedLinesResult {
                `how many Calories are being carried by the Elf carrying the most Calorie`(
                    it.map { x -> x.toElf() }
                )
            }
        )
}