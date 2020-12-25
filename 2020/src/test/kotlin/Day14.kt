import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.pow

class Day14 : Day(14) {
    private fun String.memoryAddress() = this.substring(4, this.indexOf(']')).toLong()
    private fun String.mask() = this.substring(7)
    private fun String.value() = this.substring(this.indexOf('=') + 2).toLong()
    private fun String.floaters() = this.allIndicesOf('X')

    private fun applyMask(mask: String, value: Long): Long = value or mask.replace("X", "0").binaryToLong() and mask.replace("X", "1").binaryToLong()
    private fun applyBitMask(mask: String, value: Long): String {
        return value.toBinaryString(mask.length).mapIndexed { i, c ->
            when (mask[i]) {
                '1' -> '1'
                'X' -> 'X'
                else -> c
            }
        }.joinToString("")
    }

    private fun generateAddresses(memoryAddress: Long, mask: String): List<Long> {
        val bitMasked = applyBitMask(mask, memoryAddress)
        val floaters = bitMasked.floaters()
        var currentAddress = bitMasked.replace('X', '0')
        val addresses = mutableListOf<Long>()

        (0 until 2f.pow(floaters.size).toInt()).forEach { mainIndex ->
            var binaryIndex = mainIndex.toBinaryString(floaters.size)

            floaters.forEachIndexed { i, floater ->
                currentAddress = currentAddress.replaceAt(floater, binaryIndex[i])
            }
            addresses.add(currentAddress.binaryToLong())
        }
        return addresses
    }

    private fun List<String>.compute(allocateMemory: (instruction: String, mask: String, memory: MutableMap<Long, Long>) -> Unit): Long {
        val memory = mutableMapOf<Long, Long>()
        var currentMask = ""

        this.forEach { line ->
            when (line.substring(0, 4)) {
                "mask" -> currentMask = line.mask()
                else -> allocateMemory(line, currentMask, memory)
            }
        }
        return memory.values.sum()
    }

    private fun List<String>.v1(): Long = compute { instruction, mask, memory ->
        memory[instruction.memoryAddress()] = applyMask(mask, instruction.value())
    }

    private fun List<String>.v2(): Long = compute { instruction, mask, memory ->
        generateAddresses(instruction.memoryAddress(), mask)
                .forEach { memoryAddress -> memory[memoryAddress] = instruction.value() }
    }

    @Test
    fun exercise1() = computeResult {
        Assertions.assertEquals(15403588588538, it.v1())
    }

    @Test
    fun exercise2() = computeResult {
        Assertions.assertEquals(3260587250457, it.v2())
    }
}