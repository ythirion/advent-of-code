import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class Day25 : Day(25) {
    private fun Long.transform(
            start: Int,
            times: Int,
            subjectNumber: Long,
    ): Long {
        var transform = this
        repeat((start until times).count()) {
            transform = (transform * subjectNumber) % 20201227
        }
        return transform
    }

    private fun findLoopSize(cardPublicKey: Long): Int {
        var value = 1L
        var loopSize = 1

        while (true) {
            value = value.transform(loopSize - 1, loopSize, 7)
            if (value == cardPublicKey) return loopSize else loopSize++
        }
    }

    private fun findEncryptionKey(lines: List<String>): Long {
        val cardPublicKey = lines[0].toLong()
        val doorPublicKey = lines[1].toLong()
        val loopSize = findLoopSize(cardPublicKey)

        return 1L.transform(0, loopSize, doorPublicKey)
    }

    @Test
    fun exercise1() = computeResult {
        Assertions.assertEquals(290487, findEncryptionKey(it))
    }
}