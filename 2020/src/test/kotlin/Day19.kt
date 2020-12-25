import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day19 : Day(19) {
    private fun generateRegexFor(rule: String,
                                 rules: Map<Int, String>,
                                 regexGenerator: (ruleId: Int, rules: Map<Int, String>) -> String) =
            rule.split(" ").joinToString(separator = "") { regexGenerator(it.toInt(), rules) }

    private fun generateRegex(ruleId: Int, rules: Map<Int, String>): String {
        val rule = rules.getOrDefault(ruleId, "")

        return when {
            rule.startsWith("\"") -> rule.substring(1, 2)
            !rule.contains("|") -> generateRegexFor(rule, rules, this::generateRegex)
            else -> rule.split("|").joinToString("|", "(", ")") { part -> generateRegexFor(part.trim(), rules, this::generateRegex) }
        }
    }

    private fun generateRegexWith42(ruleId: Int, rules: Map<Int, String>): String {
        val rule = rules.getOrDefault(ruleId, "")

        return when {
            ruleId == 8 -> "(" + generateRegexWith42(42, rules) + ")+"
            ruleId == 11 -> (1..3).joinToString("|", "(", ")") { i ->
                val expressionBuilder = StringBuilder()
                (0 until i).forEach { _ -> expressionBuilder.append(generateRegexWith42(42, rules)) }
                (0 until i).forEach { _ -> expressionBuilder.append(generateRegexWith42(31, rules)) }
                expressionBuilder.toString()
            }
            rule.startsWith("\"") -> rule.substring(1, 2)
            !rule.contains("|") -> generateRegexFor(rule, rules, this::generateRegexWith42)
            else -> rule.split("|").joinToString("|", "(", ")") { part -> generateRegexFor(part.trim(), rules, this::generateRegexWith42) }
        }
    }

    private fun countMatchingLines(
            input: String,
            regexGenerator: (ruleId: Int, rules: Map<Int, String>) -> String,
    ): Int {
        val split = input.splitAtEmptyLine()
        val rules = split[0].splitLines().map {
            val line = it.split(":")
            Pair(line[0].toInt(), line[1].substring(1))
        }.toMap()
        val regex = ('^' + regexGenerator(0, rules) + '$').toRegex()

        return split[1].splitLines().count { line -> line.matches(regex) }
    }

    @Test
    fun exercise1() = computeStringResult {
        Assertions.assertEquals(129, countMatchingLines(it, this::generateRegex))
    }

    @Test
    fun exercise2() = computeStringResult {
        Assertions.assertEquals(243, countMatchingLines(it, this::generateRegexWith42))
    }
}