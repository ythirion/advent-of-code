import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigInteger.ONE

class Day11 : Day(11, "Monkey in the Middle") {
    private data class Monkey(
        val id: Int,
        val items: MutableList<Long>,
        val operation: (Long) -> Long,
        val divisor: Int,
        val onTrue: Int,
        val onFalse: Int
    ) {
        fun clear(): Unit = items.clear()
        fun add(item: Long): Boolean = items.add(item)
        fun throwItemTo(worryLevel: Long): Int = if (worryLevel % divisor == 0L) onTrue else onFalse
    }

    private fun String.toId(): Int = splitWords()[1].dropLast(1).toInt()
    private fun String.toItems(): MutableList<Long> = split(": ")[1]
        .split(", ")
        .map { item -> item.toLong() }
        .toMutableList()

    private fun toLambda(operator: String, y: String): (Long) -> Long = { x ->
        when (y) {
            "old" -> compute(operator, x, x)
            else -> compute(operator, x, y.toLong())
        }
    }

    private fun compute(operator: String, x: Long, y: Long): Long =
        if (operator == "+") x + y else x * y

    private fun String.toOperation(): (Long) -> Long = split(": ")[1]
        .splitWords()
        .let { toLambda(it[3], it[4]) }

    private fun String.toDivisor(): Int = splitWords().last().toInt()
    private fun String.toThrowTo(): Int = splitWords().last().toInt()

    private fun List<String>.toMonkey(): Monkey =
        Monkey(
            this[0].toId(),
            this[1].toItems(),
            this[2].toOperation(),
            this[3].toDivisor(),
            this[4].toThrowTo(),
            this[5].toThrowTo()
        )

    private fun String.toMonkeys(): List<Monkey> =
        splitAtEmptyLine().map { group -> group.splitLines().toMonkey() }

    private fun inspections(monkeys: Int): MutableList<Long> =
        (0 until monkeys).map { 0L }.toMutableList()

    private fun List<Monkey>.proceed(rounds: Int, worryLevel: (Long) -> Long): Long {
        val inspectionByMonkey = inspections(size)

        repeat(rounds) {
            forEach { monkey ->
                monkey.items.forEach { item ->
                    inspectionByMonkey[monkey.id] += 1L
                    val worryLevel = worryLevel(monkey.operation(item))
                    this[monkey.throwItemTo(worryLevel)]
                        .add(worryLevel)
                }
                monkey.clear()
            }
        }
        return inspectionByMonkey.sorted()
            .takeLast(2)
            .multiply()
    }

    private fun lowestCommonMultiple(monkeys: List<Monkey>) =
        monkeys.map { it.divisor.toBigInteger() }
            .fold(ONE) { acc, i -> (acc * i) / acc.gcd(i) }
            .toLong()

    @Test
    fun part1() {
        assertEquals(
            56120,
            computeStringResult {
                it.toMonkeys()
                    .proceed(20) { x -> x / 3 }
            }
        )
    }

    @Test
    fun part2() {
        assertEquals(
            2713310158,
            computeStringResult {
                it.toMonkeys().let { monkeys ->
                    val lcm = lowestCommonMultiple(monkeys)
                    monkeys.proceed(10_000) { x -> x % lcm }
                }
            }
        )
    }
}