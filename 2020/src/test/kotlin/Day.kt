import io.vavr.collection.Seq
import io.vavr.kotlin.toVavrList
import java.lang.Integer.parseInt
import java.lang.Long.parseLong
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

abstract class Day(day: Int) {
    //region Boolean
    protected fun Boolean.toInt() = if (this) 1 else 0
    //endregion

    //region Int, Long
    protected fun Int.divisible(other: Int) = this % other == 0
    protected fun Long.toBinaryString(length: Int): String {
        val bin = StringBuilder(this.toString(2))
        while (bin.length < length) {
            bin.insert(0, '0')
        }
        return bin.toString()
    }

    protected fun Int.toBinaryString(length: Int): String = this.toLong().toBinaryString(length)

    //endregion

    //region String
    protected fun String.containsAll(vararg strings: String) = strings.map { this.contains(it) }.count { it } == strings.size
    protected fun String.count(c: Char): Int = this.count { it == c }
    protected fun String.count(s: String): Int = this.split(s).count()
    private fun String.at(pos: Int) = this[pos % this.length]
    protected fun String.at(pos: Int, c: Char) = this.at(pos) == c
    protected fun String.toIntList() = this.map { it.toString().toInt() }
    protected fun String.countChars() = this.groupBy { it }.mapValues { c -> this.count(c.key) }
    protected fun String.binaryToInt() = parseInt(this, 2)
    protected fun String.binaryToLong() = parseLong(this, 2)
    protected fun String.replaceChars(mapping: Map<Char, Char>): String = this.map { c -> mapping[c] }.joinToString("")
    protected fun String.toStringList(): List<String> = map { it.toString() }
    protected fun String.splitAtEmptyLine() = this.split(lineSeparator() + lineSeparator())
    protected fun String.splitWords() = this.split(" ")
    protected fun String.splitLines() = this.split(lineSeparator())
    protected fun String.allIndicesOf(c: Char): List<Int> = this.indices.filter { this[it] == c }
    protected fun String.replaceAt(pos: Int, c: Char): String {
        val mutableList = this.toMutableList()
        mutableList[pos] = c
        return mutableList.joinToString(separator = "")
    }


    protected fun String.sanitize(vararg strings: String): String {
        var sanitized = this
        strings.forEach { s -> sanitized = sanitized.replace(s, "") }
        return sanitized
    }

    //endregion

    //region vavr collections
    fun Seq<Int>.multiply(): Int = this.reduce { a, b -> a * b }
    fun Seq<Long>.multiply(): Long = this.reduce { a, b -> a * b }
    fun <T> Seq<T>.replaceAt(index: Int, item: T): Seq<T> {
        val mutableList = this.toMutableList()
        mutableList[index] = item
        return mutableList.toVavrList()
    }
    //endregion

    //region Kotlin collections
    fun List<Long>.multiply(): Long = this.reduce { a, b -> a * b }

    fun <T> List<T>.replaceAt(index: Int, item: T): List<T> {
        val mutableList = this.toMutableList()
        mutableList[index] = item
        return mutableList
    }

    protected fun <T> Stack<T>.popIf(t: T): T? = if (last() == t) pop() else null
    protected fun <T> Stack<T>.lastIs(t: T): Boolean = last() == t
    protected fun <T> MutableList<T>.add(elements: Collection<T>): List<T> {
        this.addAll(elements)
        return this
    }

    protected fun <T> MutableSet<T>.removeAllElements(elements: Collection<T>): Set<T> {
        this.removeAll(elements)
        return this
    }
    //endregion

    //region Operators override
    operator fun Pair<Int, Int>.plus(pair: Pair<Int, Int>): Pair<Int, Int> = Pair(first + pair.first, second + pair.second)
    operator fun Char.plus(c: Char): String = this.toString() + c
    operator fun <T> MutableSet<T>.plus(elements: Iterable<T>): MutableSet<T> {
        this.addAll(elements)
        return this
    }
    //endregion

    //region Compute functions
    private val input = Day::class.java.classLoader.getResource("day$day.txt")

    protected fun <R> computeResult(answer: (input: List<String>) -> R): R = answer(getInput())
    protected fun <R> computeLongResult(answer: (input: List<Long>) -> R): R = answer(getInput().map { it.toLong() })
    protected fun <R> computeIntResult(answer: (input: List<Int>) -> R): R = answer(getInput().map { it.toInt() })
    protected fun <R> computeIntSeparatedResult(answer: (input: List<Int>) -> R): R = answer(getInputAsString().split(',').map { it.toInt() })
    protected fun <R> computeStringResult(answer: (input: String) -> R): R = answer(getInputAsString())
    protected fun <R> computeStringSeparatedLinesResult(answer: (input: List<String>) -> R): R = answer(getInputAsSeparatedLines())
    protected fun lineSeparator() = "\n"

    private fun getInput() = Files.readAllLines(Path.of(Objects.requireNonNull(input).toURI()))
    private fun getInputAsString() = Files.readString(Path.of(Objects.requireNonNull(input).toURI()))
    private fun getInputAsSeparatedLines() = getInputAsString().split("\n\n")
    //endregion
}