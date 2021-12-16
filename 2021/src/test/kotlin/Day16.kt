import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day16 : Day(16, "Packet Decoder") {
    sealed class Packet(val version: Int, val typeId: Int) {
        abstract fun sumVersionNumbers(): Int
        abstract fun evaluate(): Long
    }

    class LiteralValue(version: Int, private val value: Long) : Packet(version, 4) {
        override fun sumVersionNumbers(): Int = version
        override fun evaluate(): Long = value
    }

    class Operator(version: Int, typeId: Int, private val subPackets: List<Packet>) : Packet(version, typeId) {
        override fun sumVersionNumbers(): Int = version + subPackets.sumOf { it.sumVersionNumbers() }
        override fun evaluate(): Long =
            when (typeId) {
                0 -> subPackets.sumOf { it.evaluate() }
                1 -> subPackets.fold(1.toLong()) { acc, packet -> acc * packet.evaluate() }
                2 -> subPackets.minOf { it.evaluate() }
                3 -> subPackets.maxOf { it.evaluate() }
                5 -> (if (subPackets.first().evaluate() > subPackets[1].evaluate()) 1 else 0).toLong()
                6 -> (if (subPackets.first().evaluate() < subPackets[1].evaluate()) 1 else 0).toLong()
                else -> (if (subPackets.first().evaluate() == subPackets[1].evaluate()) 1 else 0).toLong()
            }
    }

    private val transformToBinary = mapOf(
        '0' to "0000", '1' to "0001", '2' to "0010", '3' to "0011", '4' to "0100", '5' to "0101",
        '6' to "0110", '7' to "0111", '8' to "1000", '9' to "1001", 'A' to "1010", 'B' to "1011",
        'C' to "1100", 'D' to "1101", 'E' to "1110", 'F' to "1111"
    )

    private fun String.toBinary(): String = this.fold("") { acc, c -> acc + transformToBinary[c] }

    private fun String.toLiteralValue(version: Int, from: Int): Pair<LiteralValue, Int> =
        StringBuilder().let {
            var index = from
            do {
                val nextBits = this.substring(index, index + 5)
                it.append(nextBits.substring(1))
                index += nextBits.length
            } while (!nextBits.startsWith('0'))

            LiteralValue(version, it.toString().toLong(2)) to index
        }

    private fun String.toOperator(version: Int, typeId: Int, from: Int): Pair<Operator, Int> =
        if (this[from].digitToInt(2) == 0) from15BitsNumberOperator(from + 1, version, typeId)
        else from11BitsNumberOperator(from + 1, version, typeId)

    private fun String.from11BitsNumberOperator(
        from: Int,
        version: Int,
        typeId: Int
    ): Pair<Operator, Int> {
        val numberOfSubPackets = this.substring(from, from + 11).toInt(2)
        var index = from + 11

        mutableListOf<Packet>().let { subPackets ->
            repeat(numberOfSubPackets) {
                val (subPacket, newIndex) = this.decode(index)
                index = newIndex
                subPackets.add(subPacket)
            }
            return Operator(version, typeId, subPackets) to index
        }
    }

    private fun String.from15BitsNumberOperator(
        from: Int,
        version: Int,
        typeId: Int
    ): Pair<Operator, Int> {
        val numberOfBitsInTheSubPackets = this.substring(from, from + 15).toInt(2)
        var currentIndex = from + 15
        val endIndex = currentIndex + numberOfBitsInTheSubPackets

        mutableListOf<Packet>().let {
            while (currentIndex < endIndex) {
                val (subPacket, newIndex) = this.decode(currentIndex)
                currentIndex = newIndex
                it.add(subPacket)
            }
            return Operator(version, typeId, it) to currentIndex
        }
    }

    private fun String.versionAt(index: Int) = this.substring(index, index + 3).toInt(2)
    private fun String.typeAt(index: Int) = this.substring(index + 3, index + 6).toInt(2)

    private fun String.decode(from: Int): Pair<Packet, Int> =
        if (typeAt(from) == 4) this.toLiteralValue(versionAt(from), from + 6)
        else this.toOperator(versionAt(from), typeAt(from), from + 6)

    private fun String.decode(): Packet = decode(0).first

    private fun `what do you get if you add up the version numbers in all packets`(input: String): Int =
        input.toBinary()
            .decode()
            .sumVersionNumbers()

    private fun `what do you get if you evaluate the expression represented by your hexadecimal-encoded BITS transmission`(
        input: String
    ): Long =
        input.toBinary()
            .decode()
            .evaluate()

    @Test
    fun part1() =
        Assertions.assertEquals(
            929,
            computeStringResult {
                `what do you get if you add up the version numbers in all packets`(it)
            }
        )

    @Test
    fun part2() =
        Assertions.assertEquals(
            911945136934,
            computeStringResult {
                `what do you get if you evaluate the expression represented by your hexadecimal-encoded BITS transmission`(
                    it
                )
            }
        )
}

