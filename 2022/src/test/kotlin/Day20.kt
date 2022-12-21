import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

typealias CircularList = List<Value>

const val decryptionKey = 811589153

data class Value(val originalValue: Int, val originalIndex: Int) {
    val decryptedValue = originalValue * decryptionKey
}

class Day20 : Day(20, "Grove Positioning System") {
    private fun CircularList.move(valueSelector: (Value) -> Long): CircularList {
        val moved = this.toMutableList()
        this.forEach { currentValue ->
            val newIndex = newIndex(moved.indexOf(currentValue), valueSelector(currentValue), size)
            moved.remove(currentValue)
            moved.add(newIndex, currentValue)
        }
        return moved
    }

    private fun newIndex(currentIndex: Int, value: Long, size: Int): Int =
        circularIndexFor(currentIndex + value, size)

    private fun circularIndexFor(index: Long, size: Int): Int {
        var fixedIndex = index
        var wasNegative = false

        if (fixedIndex <= 0) {
            wasNegative = true
            fixedIndex = -fixedIndex
        }
        val offset = (fixedIndex % (size - 1)).toInt()
        return if (wasNegative) size - 1 - offset else offset
    }

    private fun List<String>.toIntValue(): List<Value> =
        mapIndexed { index, value -> Value(value.toInt(), index) }

    private fun CircularList.groveValueAt(index: Int): Int =
        this.indexOfFirst { it.originalValue == 0 }.let { indexOf0 ->
            return this[(indexOf0 + index) % size].originalValue
        }

    private fun CircularList.groveCoordinates(): Int =
        listOf(
            groveValueAt(1000),
            groveValueAt(2000),
            groveValueAt(3000)
        ).sum()


    @Test
    fun part1() {
        assertEquals(
            3473,
            computeResult {
                it.toIntValue()
                    .move { v -> v.originalValue.toLong() }
                    .groveCoordinates()
            }
        )
    }
}