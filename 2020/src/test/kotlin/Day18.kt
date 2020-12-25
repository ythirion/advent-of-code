import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class Day18 : Day(18) {
    private fun evaluate(tokens: List<String>) = Stack<Long>().let { stack ->
        tokens.forEach { token ->
            stack += when (token) {
                "+" -> stack.pop() + stack.pop()
                "*" -> stack.pop() * stack.pop()
                else -> token.toLong()
            }
        }
        stack.last()
    }

    private fun parse(str: String, invertPrecedence: Boolean = false): List<String> {
        val expression = mutableListOf<String>()
        val stack = Stack<String>()

        str.sanitize(" ").toStringList().forEach { token ->
            when (token) {
                "(" -> stack += token
                in "0".."9" -> expression += token
                "+", "*" -> {
                    while (!stack.isEmpty() && !stack.lastIs("(") &&
                            (!invertPrecedence || (!stack.lastIs("*") || token != "+")))
                        expression += stack.pop()
                    stack += token
                }
                ")" -> {
                    while (!stack.lastIs("("))
                        expression += stack.pop()
                    stack.popIf("(")
                }
            }
        }
        return expression.add(stack.reversed())
    }

    private fun calculateResult(lines: List<String>): Long {
        return lines.mapNotNull { evaluate(parse(it)) }.sum()
    }

    private fun calculateWithInvertPrecedence(lines: List<String>): Long {
        return lines.mapNotNull { evaluate(parse(it, true)) }.sum()
    }

    @Test
    fun exercise1() = computeResult {
        Assertions.assertEquals(209335026987, calculateResult(it))
    }

    @Test
    fun exercise2() = computeResult {
        Assertions.assertEquals(33331817392479, calculateWithInvertPrecedence(it))
    }
}