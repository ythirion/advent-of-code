import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day2 : Day(2) {
    private fun extractPassword(line: String): Password {
        val splitLine = line.split(' ')

        return Password(splitLine[0].substringBefore('-').toInt(),
                splitLine[0].substringAfter('-').toInt(),
                splitLine[1].substringBefore(':')[0],
                splitLine[2].substringBefore(':'))
    }

    private class Password(val x: Int, val y: Int, val character: Char, val value: String)

    private fun countValidPasswords(
            lines: List<String>,
            isValid: (password: Password) -> Boolean,
    ): Int =
            lines.map { extractPassword(it) }
                    .filter { isValid(it) }
                    .count();

    @Test
    fun exercise1() = Assertions.assertEquals(622, computeResult { lines ->
        countValidPasswords(lines) {
            val count = it.value.count(it.character)
            count >= it.x && count <= it.y
        }
    })

    @Test
    fun exercise2() = Assertions.assertEquals(263, computeResult { lines ->
        countValidPasswords(lines) { (it.value[it.x - 1] + it.value[it.y - 1]).count(it.character) == 1 }
    })
}