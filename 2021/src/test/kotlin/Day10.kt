import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

typealias Line = String
typealias CheckResult = Pair<Char?, List<Char>>

class Day10 : Day(10, "Syntax Scoring") {
    private val closers = mapOf(')' to '(', ']' to '[', '}' to '{', '>' to '<')
    private val errorScorer = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
    private val autocompleteScorer = mapOf('(' to 1, '[' to 2, '{' to 3, '<' to 4)

    private fun Char.isClosing(): Boolean = closers.contains(this)
    private fun Char.isInvalid(stack: MutableList<Char>) = stack.isEmpty() || stack.last() != closers[this]
    private fun CheckResult.isLineIncomplete(): Boolean = this.first == null && this.second.isNotEmpty()
    private fun CheckResult.errorScore(): Int = errorScorer.getOrDefault(this.first, 0)
    private fun CheckResult.autocompleteScore(): Long =
        this.second.reversed().fold(0L) { total, character -> 5 * total + autocompleteScorer[character]!! }

    private fun List<Long>.middleScore() = this.sorted()[this.size / 2]

    private fun Line.checkCorruption(): CheckResult {
        mutableListOf<Char>().let { stack ->
            this.forEach { character ->
                when {
                    character.isClosing() -> {
                        if (character.isInvalid(stack))
                            return Pair(character, stack)
                        stack.removeLast()
                    }
                    else -> stack.add(character)
                }
            }
            return Pair(null, stack)
        }
    }

    private fun `What is the total syntax error score for those errors`(lines: List<Line>): Int =
        lines.map { it.checkCorruption() }
            .sumOf { it.errorScore() }

    private fun `What is the middle score`(lines: List<Line>): Long =
        lines.map { it.checkCorruption() }
            .filter { it.isLineIncomplete() }
            .map { it.autocompleteScore() }
            .middleScore()


    @Test
    fun part1() =
        Assertions.assertEquals(
            362271,
            computeResult { `What is the total syntax error score for those errors`(it) }
        )

    @Test
    fun part2() =
        Assertions.assertEquals(
            1698395182,
            computeResult { `What is the middle score`(it) }
        )
}