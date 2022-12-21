import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

typealias CircularList = List<IntValue>

data class IntValue(val value: Int, val originalIndex: Int)

class Day20 : Day(20, "Grove Positioning System") {
    private fun CircularList.move(): CircularList {
        val moved = this.toMutableList()
        this.forEach { currentValue ->
            val newIndex = newIndex(moved.indexOf(currentValue), currentValue.value, size)
            moved.remove(currentValue)
            moved.add(newIndex, currentValue)
        }
        return moved
    }

    private fun newIndex(currentIndex: Int, value: Int, size: Int): Int =
        circularIndexFor(currentIndex + value, size)

    private fun circularIndexFor(index: Int, size: Int): Int {
        var fixedIndex = index
        var wasNegative = false

        if (fixedIndex <= 0) {
            wasNegative = true
            fixedIndex = -fixedIndex
        }
        val offset = fixedIndex % (size - 1)
        return if (wasNegative) size - 1 - offset else offset
    }

    private fun List<String>.toIntValue(): List<IntValue> =
        mapIndexed { index, value -> IntValue(value.toInt(), index) }

    private fun CircularList.groveValueAt(index: Int): Int =
        this.indexOfFirst { it.value == 0 }.let { indexOf0 ->
            return this[(indexOf0 + index) % size].value
        }


    @Test
    fun part1() {
        assertEquals(
            3473,
            computeResult {
                it.toIntValue()
                    .move().let { result ->
                        listOf(
                            result.groveValueAt(1000),
                            result.groveValueAt(2000),
                            result.groveValueAt(3000)
                        ).sum()
                    }
            }
        )
    }
}