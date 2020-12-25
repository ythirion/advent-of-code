import io.vavr.collection.List
import io.vavr.kotlin.toVavrList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day9 : Day(9) {
    private fun isValid(values: List<Long>, index: Int, rangeSize: Int) =
            values.slice(index - rangeSize, index).combinations(2).map { it.sum() }.contains(values[index])

    private fun findFirstError(values: List<Long>, rangeSize: Int) =
            values.elementAt((rangeSize until values.size()).first { i -> !isValid(values, i, rangeSize) })

    private fun findWeakness(values: List<Long>, rangeSize: Int): Long {
        val searchedValue = findFirstError(values, rangeSize)

        (List.rangeClosed(2, values.size())).forEach { slice ->
            (0 until values.size()).forEach { index ->
                values.slice(index, index + slice).let { slicedValues ->
                    if (slicedValues.sum() == searchedValue)
                        slicedValues.sorted().let { return it.first() + it.last() }
                }
            }
        }
        throw IllegalArgumentException("WTF")
    }

    @Test
    fun exercise1() = computeLongResult {
        Assertions.assertEquals(1504371145, findFirstError(it.toVavrList(), 25))
    }

    @Test
    fun exercise2() = computeLongResult {
        Assertions.assertEquals(183278487, findWeakness(it.toVavrList(), 25))
    }
}