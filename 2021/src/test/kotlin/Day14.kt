import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

typealias InsertionRules = Map<String, String>

data class PolymerRules(val template: String, val rules: InsertionRules)

class Day14 : Day(14, "Extended Polymerization") {
    private fun String.toPolymerRules(): PolymerRules =
        PolymerRules(
            template = splitAtEmptyLine()[0],
            rules = splitAtEmptyLine()[1]
                .splitLines()
                .associate {
                    val (from, to) = it.split(" -> ")
                    from to to
                }
        )

    @Test
    fun part1() =
        Assertions.assertEquals(
            2712,
            computeStringResult { Mutator(it.toPolymerRules()).calculate(10) }
        )

    @Test
    fun part2() =
        Assertions.assertEquals(
            8336623059567,
            computeStringResult { Mutator(it.toPolymerRules()).calculate(40) }
        )
}

typealias Polymer = Map<String, Long>

class Mutator(private val rules: PolymerRules) {
    fun calculate(iterations: Int): Long {
        val template =
            rules.template.windowed(2)
                .groupingBy { it }
                .eachCount()
                .mapValues { (_, value) -> value.toLong() }

        val mutated = (1..iterations)
            .fold(template) { acc, _ -> acc.mutated }
            .map { it.key to it.value }

        val total = (mutated + (rules.template.takeLast(1) to 1L))
            .groupingBy { it.first[0] }
            .aggregate { _, acc: Long?, item, _ -> (acc ?: 0) + item.second }
            .values.sorted()

        return total.last() - total.first()
    }

    private val Polymer.mutated: Polymer
        get() = this.entries.flatMap { (chars, total) ->
            val newChar = rules.rules[chars]
            if (newChar == null)
                listOf(chars to total)
            else {
                listOf(
                    chars[0] + newChar to total,
                    newChar + chars[1] to total
                )
            }
        }.groupingBy { it.first }
            .aggregate { _, acc, element, _ -> (acc ?: 0) + element.second }
}