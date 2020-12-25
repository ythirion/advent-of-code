import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day13 : Day(13) {
    private data class Bus(val id: Long, val position: Long)
    private data class TimeTable(val time: Long, val busses: List<Bus>)

    private fun List<String>.toTable() = TimeTable(this.first().toLong(), this[1].split(',').mapIndexedNotNull { i, b ->
        if (b != "x") Bus(b.toLong(), i.toLong()) else null
    })

    private fun findResult(input: List<String>): Long {
        val table = input.toTable()
        (table.time until Long.MAX_VALUE).forEach { t ->
            table.busses.forEach { bus ->
                if (t % bus.id == 0L) return bus.id * (t - table.time)
            }
        }
        return -1
    }

    private fun multInv(a: Long, b: Long): Long {
        if (b == 1L) return 1L
        var aa = a
        var bb = b
        var x0 = 0L
        var x1 = 1L
        while (aa > 1) {
            val q = aa / bb
            var t = bb
            bb = aa % bb
            aa = t
            t = x0
            x0 = x1 - q * x0
            x1 = t
        }
        if (x1 < 0) x1 += b
        return x1
    }

    //https://rosettacode.org/wiki/Chinese_remainder_theorem#Kotlin
    private fun chineseRemainder(n: LongArray, a: LongArray): Long {
        val prod = n.fold(1L) { acc, i -> acc * i }
        var sum = 0L

        for (i in n.indices) {
            val p = prod / n[i]
            sum += a[i] * multInv(p, n[i]) * p
        }
        return sum % prod
    }

    @Test
    fun exercise1() = computeResult {
        Assertions.assertEquals(2947, findResult(it))
    }

    @Test
    fun exercise2() = computeResult {
        val timeTable = it.toTable()
        val n = timeTable.busses.map { bus -> bus.id }.toLongArray()
        val a = timeTable.busses.map { bus -> (bus.id - bus.position) % bus.id }.toLongArray()
        Assertions.assertEquals(526090562196173, chineseRemainder(n, a))
    }
}