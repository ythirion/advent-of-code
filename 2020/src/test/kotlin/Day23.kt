import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class Day23 : Day(23) {
    private data class Cup(val label: Int, var next: Cup? = null)

    //region Cup extensions
    private fun Cup.pick() = listOf(this.next(), this.next(2), this.next(3))
    private fun Cup.next(times: Int = 1) = (1..times).fold(this) { acc, _ -> acc.next!! }

    private fun Cup.takeNextLabels(times: Int = 1): List<Int> {
        var current = this
        return (1..times).map {
            val label = current.next().label
            current = current.next()
            label
        }
    }

    private fun Cup.nextDestination(
            bounds: Pair<Int, Int>,
            cups: MutableMap<Int, Cup>,
            pickedCups: List<Cup>,
    ): Cup {
        var destinationLabel = if (label - 1 < bounds.first) bounds.second else label - 1
        var destination = cups[destinationLabel]!!

        while (pickedCups.contains(destination)) {
            destinationLabel--
            if (destinationLabel < bounds.first) destinationLabel = bounds.second
            destination = cups[destinationLabel]!!
        }
        return destination
    }

    //endregion

    private fun String.toCups(nbOfCups: Int): MutableMap<Int, Cup> {
        val numbers = this.toIntList().toMutableList()
        numbers.addAll(numbers.max()!! + 1..nbOfCups)

        val cups = mutableMapOf<Int, Cup>()
        val rootNode = Cup(numbers[0])
        var currentNode = rootNode
        cups[rootNode.label] = rootNode

        for (i in 1 until numbers.size) {
            val node = Cup(numbers[i])
            cups[node.label] = node
            currentNode.next = node
            currentNode = node
        }
        currentNode.next = rootNode

        return cups
    }

    private fun chain(currentCup: Cup, destination: Cup, pickedCups: List<Cup>) {
        currentCup.next = currentCup.next(4)

        val endNext = destination.next
        destination.next = pickedCups[0]
        pickedCups[2].next = endNext
    }

    private fun <TResult> playCups(
            input: String,
            nbOfCups: Int,
            moves: Int,
            mapResult: (cups: MutableMap<Int, Cup>) -> TResult,
    ): TResult {
        return mapResult(play(input.toCups(nbOfCups), moves))
    }

    private fun play(cups: MutableMap<Int, Cup>, moves: Int): MutableMap<Int, Cup> {
        var currentCup = cups.entries.first().value
        val bounds = Pair(1, cups.size)

        (1..moves).forEach { _ ->
            val pickedCups = currentCup.pick()
            var destination = currentCup.nextDestination(bounds, cups, pickedCups)
            chain(currentCup, destination, pickedCups)

            currentCup = currentCup.next()
        }
        return cups
    }

    private fun round1(input: String): String {
        return playCups(input, 9, 100) { cups ->
            cups[1]!!.takeNextLabels(8).joinToString("")
        }
    }

    private fun round2(input: String): Long {
        val nbOfCups = 1_000_000
        return playCups(input, nbOfCups, 10_000_000) { cups ->
            cups[1]!!.takeNextLabels(2).map { it.toLong() }.multiply()
        }
    }

    @Test
    fun exercise1() = computeStringResult {
        Assertions.assertEquals("98742365", round1(it))
    }

    @Test
    fun exercise2() = computeStringResult {
        Assertions.assertEquals(294320513093, round2(it))
    }
}