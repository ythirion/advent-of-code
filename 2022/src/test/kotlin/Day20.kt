import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.abs

typealias CircularList = List<Value>

const val decryptionKey = 811589153L

data class Value(val originalValue: Int, val originalIndex: Int) {
    val decryptedValue = originalValue * decryptionKey
}

class Day20 : Day(20, "Grove Positioning System") {
    private fun MutableList<Value>.move(v: Value, newIndex: Int) {
        remove(v)
        add(newIndex, v)
    }

    private fun CircularList.move(times: Int, valueSelector: (Value) -> Long): CircularList =
        toMutableList().let { movedValues ->
            repeat(times) {
                this.forEach { currentValue ->
                    movedValues.move(
                        currentValue,
                        circularIndexFor(movedValues.indexOf(currentValue) + valueSelector(currentValue))
                    )
                }
            }
            return movedValues
        }

    private fun CircularList.circularIndexFor(calculatedIndex: Long): Int =
        (abs(calculatedIndex) % (size - 1)).toInt().let { offset ->
            return if (calculatedIndex <= 0) size - 1 - offset else offset
        }

    private fun List<String>.toIntValue(): List<Value> =
        mapIndexed { index, value -> Value(value.toInt(), index) }

    private fun CircularList.groveValueAt(index: Int): Value =
        this.indexOfFirst { it.originalValue == 0 }.let { indexOf0 ->
            return this[(indexOf0 + index) % size]
        }

    private fun CircularList.groveCoordinates(): List<Value> =
        listOf(
            groveValueAt(1000),
            groveValueAt(2000),
            groveValueAt(3000)
        )


    @Test
    fun part1() {
        assertEquals(
            3473,
            computeResult {
                it.toIntValue()
                    .move(1) { v -> v.originalValue.toLong() }
                    .groveCoordinates()
                    .sumOf { v -> v.originalValue }
            }
        )
    }

    @Test
    fun part2() {
        assertEquals(
            7496649006261,
            computeResult {
                it.toIntValue()
                    .move(10) { v -> v.decryptedValue }
                    .groveCoordinates()
                    .sumOf { v -> v.decryptedValue }
            }
        )
    }
}