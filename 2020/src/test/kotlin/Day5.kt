import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day5 : Day(5) {
    private val mapping = mapOf('F' to '0', 'B' to '1', 'L' to '0', 'R' to '1')

    private fun getSeatIds(lines: List<String>): List<Int> = lines.map { it.replaceChars(mapping).binaryToInt() }

    private fun findMySeat(lines: List<String>): Int {
        val seatIds = getSeatIds(lines).sorted()
        return generateSequence(seatIds.first(), Int::inc).first { !seatIds.contains(it) }
    }

    @Test
    fun exercise1() = computeResult {
        Assertions.assertEquals(832, getSeatIds(it).maxOf { seatId -> seatId })
    }

    @Test
    fun exercise2() = computeResult {
        Assertions.assertEquals(517, findMySeat(it))
    }
}