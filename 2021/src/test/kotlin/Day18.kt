import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day18 : Day(18, "Snailfish") {
    sealed class SnailNumber
    class Regular(var value: Int) : SnailNumber()
    class SnailPair(var left: SnailNumber, var right: SnailNumber) : SnailNumber()

    private fun String.toSnailFishNumber(): SnailNumber {
        var depth = 0
        forEachIndexed { i, c ->
            when (c) {
                '[' -> depth++
                ']' -> depth--
                ',' -> if (depth == 1)
                    return SnailPair(
                        substring(1, i).toSnailFishNumber(),
                        substring(i + 1, lastIndex).toSnailFishNumber()
                    )
                else -> if (depth == 0) return Regular(toInt())
            }
        }
        throw Exception("WTF ?")
    }

    private fun SnailNumber.findDepth(a: List<SnailPair>, depth: Int): List<SnailPair>? =
        when (this) {
            is Regular -> null
            is SnailPair -> when (depth) {
                0 -> a + this
                else -> left.findDepth(a + this, depth - 1) ?: right.findDepth(a + this, depth - 1)
            }
        }

    private fun SnailNumber.findValue(pair: SnailPair, value: Int): Pair<SnailPair, Regular>? =
        when (this) {
            is Regular -> if (this.value >= value) pair to this else null
            is SnailPair -> left.findValue(this, value) ?: right.findValue(this, value)
        }

    private fun SnailNumber.right(): Regular =
        when (this) {
            is Regular -> this
            is SnailPair -> right.right()
        }

    private fun SnailNumber.left(): Regular =
        when (this) {
            is Regular -> this
            is SnailPair -> left.left()
        }

    private fun Regular.split() =
        SnailPair(Regular(value / 2), Regular(value / 2 + value % 2))

    private fun SnailPair.reduce(): Boolean {
        findDepth(listOf(this), 4)?.let { a ->
            //explode
            val pairs = a.reversed().windowed(2) { x -> x[1] to x[0] }

            pairs.find { (p, s) -> s == p.right }
                ?.let { (p, _) -> p.left.right().value += (a.last().left as Regular).value }
            pairs.find { (p, s) -> s == p.left }
                ?.let { (p, _) -> p.right.left().value += (a.last().right as Regular).value }

            val s = a.last()
            val p = a[a.lastIndex - 1]
            if (s == p.left) p.left = Regular(0) else p.right = Regular(0)

            return true
        }
        findValue(this, 10)?.let { (p, s) ->
            if (p.left == s) p.left = s.split() else p.right = s.split()
            return true
        }
        return false
    }

    private fun SnailPair.reduceDeep() = this.apply {
        while (reduce()){}
    }

    private fun SnailNumber.magnitude(): Int = when (this) {
        is Regular -> value
        is SnailPair -> 3 * left.magnitude() + 2 * right.magnitude()
    }

    private fun `what is the magnitude of the final sum`(homework: List<SnailNumber>): Int =
        homework.reduce { a, b -> SnailPair(a, b).reduceDeep() }
            .magnitude()

    private fun `what is the largest magnitude of any sum of two different snailfish numbers from the homework assignment`(
        homework: List<String>
    ): Int {
        return homework.product(homework)
            .filter { (a, b) -> a != b }
            .map { (a, b) -> a.toSnailFishNumber() to b.toSnailFishNumber() }
            .maxOf { (a, b) -> SnailPair(a, b).reduceDeep().magnitude() }
    }

    @Test
    fun part1() =
        Assertions.assertEquals(
            3359,
            computeResult({ it.map { line -> line.toSnailFishNumber() } },
                { `what is the magnitude of the final sum`(it) })
        )

    @Test
    fun part2() =
        Assertions.assertEquals(
            4616,
            computeResult {
                `what is the largest magnitude of any sum of two different snailfish numbers from the homework assignment`(
                    it
                )
            }
        )
}

