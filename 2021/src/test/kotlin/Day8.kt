import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

data class LineNote(val patterns: Set<Set<Char>>, val digits: List<Set<Char>>)
typealias Notes = List<LineNote>

class Day8 : Day(8, "Seven Segment Search") {
    private fun Set<Char>.containsDigit(digit: Set<Char>) = containsAll(digit)

    private fun String.toLineNote(): LineNote =
        LineNote(
            split(" | ")[0].splitWords().map(String::toSet).toSet(),
            split(" | ")[1].splitWords().map(String::toSet).toList()
        )

    private fun `how many times do digits 1, 4, 7, or 8 appear`(notes: Notes): Int =
        notes.sumOf { line -> line.digits.count { it.size in listOf(2, 3, 4, 7) } }

    private fun `add up all of the output values`(notes: Notes): Int = notes.sumOf { it.decodeNumber() }

    private fun LineNote.decodeNumber(): Int =
        decoder().let { lineDecoder ->
            digits.map { lineDecoder[it] }
                .joinToString("")
                .toInt()
        }

    private fun LineNote.decoder(): Map<Set<Char>, Char> {
        // Simple digits
        val digit1 = patterns.first { it.size == 2 }
        val digit7 = patterns.first { it.size == 3 }
        val digit4 = patterns.first { it.size == 4 }
        val digit8 = patterns.first { it.size == 7 }

        // Sizes
        val sizeOf5 = patterns.filter { it.size == 5 }
        val sizeOf6 = patterns.filter { it.size == 6 }

        // Composed digits
        val digit3 = sizeOf5.first { it.containsDigit(digit1) }
        val digit9 = sizeOf6.first { it.containsDigit(digit3) }
        val digit0 = sizeOf6.first { it != digit9 && it.containsDigit(digit7) }
        val digit6 = (sizeOf6 - setOf(digit0, digit9)).first()
        val digit2 = (sizeOf5.filter { it.containsAll(digit8 - digit6) }).first { it != digit3 }
        val digit5 = (sizeOf5 - setOf(digit2, digit3)).first()

        return mapOf(
            digit1 to '1',
            digit2 to '2',
            digit3 to '3',
            digit4 to '4',
            digit5 to '5',
            digit6 to '6',
            digit7 to '7',
            digit8 to '8',
            digit9 to '9',
            digit0 to '0',
        )
    }


    @Test
    fun part1() =
        Assertions.assertEquals(
            349,
            computeResult({ it.map { line -> line.toLineNote() } },
                { `how many times do digits 1, 4, 7, or 8 appear`(it) })
        )

    @Test
    fun part2() =
        Assertions.assertEquals(
            1070957,
            computeResult({ it.map { line -> line.toLineNote() } },
                { `add up all of the output values`(it) })
        )
}