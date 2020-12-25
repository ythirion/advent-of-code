import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.collections.set

typealias Ticket = List<Int>
typealias Rules = Map<String, Pair<IntRange, IntRange>>

class Day16 : Day(16) {
    private data class Train(val rules: Rules, val myTicket: Ticket, val nearby: List<Ticket>)

    private fun Pair<IntRange, IntRange>.isValid(value: Int) = this.first.contains(value) || this.second.contains(value)
    private fun Map.Entry<String, Pair<IntRange, IntRange>>.isValid(value: Int) = this.value.isValid(value)

    private fun extractRange(input: String): IntRange {
        val sanitizedString = input.sanitize(" ").split("-")
        return IntRange(sanitizedString[0].toInt(), sanitizedString[1].toInt())
    }

    private fun extractTicket(input: String): Ticket = input.split(",").map { it.toInt() }

    private fun extractRules(lines: List<String>): Rules {
        val values = mutableMapOf<String, Pair<IntRange, IntRange>>()

        lines.forEach {
            val split = it.split(":")
            val ranges = split[1].split("or")
            values[split[0]] = Pair(extractRange(ranges[0]), extractRange(ranges[1]))
        }
        return values
    }

    private fun mapToTrain(input: String): Train {
        val split = input.split(lineSeparator() + lineSeparator())
        val rules = extractRules(split[0].split(lineSeparator()));
        val myTicket = extractTicket(split[1].split(lineSeparator())[1])
        val nearbyStrings = split[2].split(lineSeparator())
        val nearby = nearbyStrings.subList(1, nearbyStrings.size).map { extractTicket(it) }

        return Train(rules, myTicket, nearby)
    }

    private fun scanTicket(ticket: Ticket, rules: Collection<Pair<IntRange, IntRange>>): Int =
            ticket.map { value -> if (!rules.any { it.isValid(value) }) value else 0 }.sum()

    private fun scanAll(train: Train): Int = train.nearby.fold(0) { acc, ticket -> acc + scanTicket(ticket, train.rules.values) }

    private fun findRulesOrder(couldBe: MutableMap<Int, MutableList<String>>): Map<Int, String> {
        val positionRules = mutableMapOf<Int, String>()

        while (couldBe.isNotEmpty()) {
            val solved = couldBe.entries.first { entry -> entry.value.count() == 1 }
            positionRules[solved.key] = solved.value.single()
            couldBe.remove(solved.key)
            couldBe.forEach { entry -> entry.value.remove(solved.value.single()) }
        }
        return positionRules
    }

    private fun findRulesOrder(train: Train): Map<Int, String> {
        val validTickets = train.nearby.filter { ticket -> !ticket.any { value -> train.rules.all { rule -> !rule.isValid(value) } } }
        val couldBe = mutableMapOf<Int, MutableList<String>>()

        (train.rules.toList().indices).forEach { index ->
            couldBe[index] = train.rules.keys.toMutableList()

            validTickets.forEach { ticket ->
                train.rules.forEach { rule ->
                    if (!rule.isValid(ticket[index])) {
                        couldBe[index]?.remove(rule.key)
                    }
                }
            }
        }
        return findRulesOrder(couldBe)
    }

    private fun scanMyTicket(train: Train): Long {
        val rules = findRulesOrder(train)
        return rules.filter { rule -> rule.value.contains("departure") }
                .map { rule -> train.myTicket[rule.key].toLong() }
                .multiply()
    }

    @Test
    fun exercise1() = computeStringResult {
        Assertions.assertEquals(22977, scanAll(mapToTrain(it)))
    }

    @Test
    fun exercise2() = computeStringResult {
        Assertions.assertEquals(998358379943, scanMyTicket(mapToTrain(it)))
    }
}