import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day6 : Day(6) {
    private fun countAnswers(groups: List<String>) = groups.sumBy { it.sanitize(lineSeparator()).countChars().size }

    private fun countEveryoneAnswers(groups: List<String>) = groups.sumBy { answer ->
        answer.countChars().values.filter { count -> count == answer.count(lineSeparator()) }.size
    }

    @Test
    fun exercise1() = computeStringSeparatedLinesResult {
        Assertions.assertEquals(6799, countAnswers(it))
    }

    @Test
    fun exercise2() = computeStringSeparatedLinesResult {
        Assertions.assertEquals(3354, countEveryoneAnswers(it))
    }
}