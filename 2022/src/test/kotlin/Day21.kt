import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

sealed class Monkey(val name: String) {
    abstract fun evaluate(monkeys: Monkeys): Long
}

class Yeller(name: String, private val value: Long) : Monkey(name) {
    override fun evaluate(monkeys: Monkeys): Long = value
}

class Mather(
    name: String,
    val left: String,
    private val operator: Char,
    val right: String
) : Monkey(name) {
    override fun evaluate(monkeys: Monkeys): Long =
        when (operator) {
            '+' -> monkeys[left]!!.evaluate(monkeys) + monkeys[right]!!.evaluate(monkeys)
            '-' -> monkeys[left]!!.evaluate(monkeys) - monkeys[right]!!.evaluate(monkeys)
            '*' -> monkeys[left]!!.evaluate(monkeys) * monkeys[right]!!.evaluate(monkeys)
            else -> monkeys[left]!!.evaluate(monkeys) / monkeys[right]!!.evaluate(monkeys)
        }
}

typealias Monkeys = Map<String, Monkey>

class Day21 : Day(21, "Monkey Math") {
    private fun Pair<String, String>.toMonkey(): Monkey =
        second.splitWords().let {
            return when (it.size) {
                3 -> Mather(first, it[0], it[1][0], it[2])
                else -> Yeller(first, it[0].toLong())
            }
        }

    private fun List<String>.toMonkeys(): Monkeys =
        this.associate { line ->
            val (monkey, expression) = line.split(": ")
            monkey to Pair(monkey, expression).toMonkey()
        }


    private fun Monkeys.findNumberToPassRootEquality(): Long =
        (this["root"]!! as Mather).let { root ->
            val leftMonkey = this[root.left]!!
            val rightMonkey = this[root.right]!!

            val clone = this.toMutableMap()
            val rightValue = rightMonkey.evaluate(clone)

            val y1 = 0L
            clone["humn"] = Yeller("humn", y1)
            val l1 = leftMonkey.evaluate(clone)

            val y2 = 9_000_000_000L
            clone["humn"] = Yeller("humn", y2)
            val l2 = leftMonkey.evaluate(clone)

            val m1 = (y2 - y1).toDouble() / (l2 - l1).toDouble()
            val b1 = y2 - (m1 * l2)
            val y = (m1 * rightValue + b1).toLong()

            for (t in y - 1000..y + 1000) {
                clone["humn"] = Yeller("humn", t)
                if (leftMonkey.evaluate(clone) == rightValue) return t
            }
            error("WTF!!!")
        }

    @Test
    fun part1() {
        assertEquals(
            66174565793494,
            computeResult {
                val expressions = it.toMonkeys()
                expressions["root"]?.evaluate(expressions)
            }
        )
    }

    @Test
    fun part2() {
        assertEquals(
            3327575724809,
            computeResult {
                val expressions = it.toMonkeys()
                expressions.findNumberToPassRootEquality()
            }
        )
    }
}