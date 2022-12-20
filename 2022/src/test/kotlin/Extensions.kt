import io.vavr.collection.Seq
import io.vavr.kotlin.toVavrList
import java.util.*
import java.util.function.Predicate

fun lineSeparator() = "\n"

//region Boolean
fun Boolean.toInt() = if (this) 1 else 0
//endregion

//region Int, Long
fun Int.divisible(other: Int) = this % other == 0
fun Long.toBinaryString(length: Int): String {
    val bin = StringBuilder(this.toString(2))
    while (bin.length < length) {
        bin.insert(0, '0')
    }
    return bin.toString()
}

fun Int.toBinaryString(length: Int): String = this.toLong().toBinaryString(length)

//endregion

//region String
fun String.containsAll(vararg strings: String) =
    strings.map { this.contains(it) }.count { it } == strings.size

fun String.count(c: Char): Int = this.count { it == c }
fun String.count(s: String): Int = this.split(s).count()
private fun String.at(pos: Int) = this[pos % this.length]
fun String.at(pos: Int, c: Char) = this.at(pos) == c
fun String.toIntList() = this.map { it.toString().toInt() }
fun String.countChars() = this.groupBy { it }.mapValues { c -> this.count(c.key) }
fun CharSequence.binaryToInt() = Integer.parseInt(this.toString(), 2)
fun String.binaryToInt() = Integer.parseInt(this, 2)
fun String.binaryToLong() = java.lang.Long.parseLong(this, 2)
fun String.replaceChars(mapping: Map<Char, Char>): String = this.map { c -> mapping[c] }.joinToString("")
fun String.toStringList(): List<String> = map { it.toString() }
fun String.splitAtEmptyLine(): List<String> = this.split(lineSeparator() + lineSeparator())
fun String.splitWords() = this.split(" ").map { word -> word.removeWhitespaces() }
fun String.splitInts() = this.map { Character.getNumericValue(it) }
fun String.splitLines() = this.split(lineSeparator())
fun String.allIndicesOf(c: Char): List<Int> = this.indices.filter { this[it] == c }
fun String.replaceAt(pos: Int, c: Char): String {
    val mutableList = this.toMutableList()
    mutableList[pos] = c
    return mutableList.joinToString(separator = "")
}

fun String.removeWhitespaces() = replace(" ", "")

fun String.sanitize(vararg strings: String): String {
    var sanitized = this
    strings.forEach { s -> sanitized = sanitized.replace(s, "") }
    return sanitized
}

fun String.mostCommonChar(): Pair<Char, Int> = this.countChars().maxByOrNull { e -> e.value }?.toPair()!!
fun String.leastCommonChar(): Pair<Char, Int> = this.countChars().minByOrNull { e -> e.value }?.toPair()!!

fun String.areAllCharDifferent(): Boolean =
    groupingBy { it }
        .eachCount()
        .values
        .all { it == 1 }
//endregion

//region vavr collections
fun Seq<Int>.multiply(): Int = this.reduce { a, b -> a * b }
fun Seq<Long>.multiply(): Long = this.reduce { a, b -> a * b }
fun <T> Seq<T>.replaceAt(index: Int, item: T): Seq<T> {
    val mutableList = this.toMutableList()
    mutableList[index] = item
    return mutableList.toVavrList()
}

fun List<String>.toInts(): List<Int> = this.map { it.toInt() }
fun List<String>.column(y: Int): List<Char> = this.map { it.at(y) }
fun <S, T> Iterable<S>.product(t: Iterable<T>) = asSequence().flatMap { l -> t.map { r -> l to r } }

//endregion

//region Kotlin collections
fun List<Long>.multiply(): Long = this.reduce { a, b -> a * b }
fun Sequence<Int>.multiply(): Int = this.reduce { a, b -> a * b }

fun <T> List<T>.replaceAt(index: Int, item: T): List<T> {
    val mutableList = this.toMutableList()
    mutableList[index] = item
    return mutableList
}

fun <T, R> List<T>.mapFrom(index: Int, transform: (T) -> R): List<R> =
    this.subList(index, this.size).map(transform)

fun <T> Stack<T>.popIf(t: T): T? = if (last() == t) pop() else null
fun <T> Stack<T>.lastIs(t: T): Boolean = last() == t
fun <T> List<T>.toStack(): Stack<T> {
    Stack<T>().let { stack ->
        forEach { stack.push(it) }
        return stack
    }
}

fun <T> MutableList<T>.add(elements: Collection<T>): List<T> {
    this.addAll(elements)
    return this
}

fun <T> MutableSet<T>.removeAllElements(elements: Collection<T>): Set<T> {
    this.removeAll(elements)
    return this
}
//endregion

//region Operators override
operator fun Pair<Int, Int>.plus(pair: Pair<Int, Int>): Pair<Int, Int> =
    Pair(first + pair.first, second + pair.second)

operator fun Char.plus(c: Char): String = this.toString() + c
fun Char.toIntDigit(): Int = Character.getNumericValue(this)
operator fun <T> MutableSet<T>.plus(elements: Iterable<T>): MutableSet<T> {
    this.addAll(elements)
    return this
}
//endregion

fun <TInput, R> loopUntil(
    input: TInput,
    execute: (TInput) -> R,
    breakCondition: Predicate<R>
): Int {
    var iterations = 0

    while (true) {
        iterations++

        if (breakCondition.test(execute(input)))
            break
    }
    return iterations
}

fun <TInput, TResult> loopUntilGet(
    input: TInput,
    updateInput: (TInput) -> TInput,
    breakCondition: (TInput) -> TResult?
): TResult {
    var state = input

    while (true) {
        state = updateInput(state)
        breakCondition(state).let {
            if (it != null) return it
        }
    }
}

data class Point2D(val x: Int, val y: Int)

operator fun Point2D.minus(other: Point2D): Point2D = Point2D(x - other.x, y - other.y)

fun List<Point2D>.draw(fill: String = "#"): String =
    mutableListOf<List<String>>().let { crt ->
        (0..maxOf { it.y }).map { y ->
            crt += (0..maxOf { it.x }).map { x ->
                if (contains(Point2D(x, y))) fill else " "
            }
        }

        return crt.joinToString(lineSeparator()) { line ->
            line.joinToString("")
        }
    }