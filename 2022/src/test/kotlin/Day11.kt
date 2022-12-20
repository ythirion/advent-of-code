import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day11 : Day(11, "Monkey in the Middle") {
    private data class Monkey(
        val id: Int,
        val items: MutableList<Int>,
        val operation: (Int) -> Int,
        val divisor: Int,
        val onTrue: Int,
        val onFalse: Int
    ) {
        fun clear(): Unit = items.clear()
        fun add(item: Int): Boolean = items.add(item)
        fun throwItemTo(worryLevel: Int): Int = if (worryLevel % divisor == 0) onTrue else onFalse
        fun worryLevel(item: Int) = operation(item) / 3
    }

    private fun String.toId(): Int = splitWords()[1].dropLast(1).toInt()
    private fun String.toItems(): MutableList<Int> = split(": ")[1]
        .split(", ")
        .map { item -> item.toInt() }
        .toMutableList()

    private fun toLambda(operator: String, y: String): (Int) -> Int = { x ->
        when (y) {
            "old" -> compute(operator, x, x)
            else -> compute(operator, x, y.toInt())
        }
    }

    private fun compute(operator: String, x: Int, y: Int): Int =
        if (operator == "+") x + y else x * y

    private fun String.toOperation(): (Int) -> Int = split(": ")[1]
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

    private fun inspections(monkeys: Int): MutableList<Int> =
        (0 until monkeys).map { 0 }.toMutableList()

    private fun List<Monkey>.proceed(rounds: Int): Int {
        val inspectionByMonkey = inspections(size)

        repeat(rounds) {
            forEach { monkey ->
                monkey.items.forEach { item ->
                    inspectionByMonkey[monkey.id] += 1
                    val worryLevel = monkey.worryLevel(item)
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

    @Test
    fun part1() {
        assertEquals(
            56120,
            computeStringResult {
                it.toMonkeys()
                    .proceed(20)
            }
        )
    }
}