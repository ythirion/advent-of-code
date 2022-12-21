import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

typealias Monkeys = List<Monkey>
typealias Observers = MutableMap<String, MutableList<Mather>>

abstract class Monkey(open val name: String) {
    open fun canYell(): Boolean = true
    abstract fun numberToYell(): Long
    fun yell(observers: Observers) {
        if (observers.containsKey(name)) {
            observers[name]!!.forEach { it.monkeyYelled(name, numberToYell()) }
        }
    }
}

class Yeller(override val name: String, private val number: Long) : Monkey(name) {
    override fun numberToYell(): Long = number
}

class Mather(
    override val name: String,
    val monkey1: String,
    private val operator: Char,
    val monkey2: String
) : Monkey(name) {
    private var value1: Long? = null
    private var value2: Long? = null

    fun monkeyYelled(monkey: String, number: Long) {
        if (monkey == monkey1) value1 = number
        else value2 = number
    }

    override fun canYell(): Boolean =
        value1 != null && value2 != null

    override fun numberToYell(): Long =
        when (operator) {
            '+' -> value1!! + value2!!
            '-' -> value1!! - value2!!
            '*' -> value1!! * value2!!
            else -> value1!! / value2!!
        }
}

class Day21 : Day(21, "Monkey Math") {
    private fun Pair<String, String>.toMonkey(): Monkey =
        second.splitWords().let {
            return when (it.size) {
                3 -> Mather(first, it[0], it[1][0], it[2])
                else -> Yeller(first, it[0].toLong())
            }
        }

    private fun List<String>.toMonkeys(): Monkeys =
        map {
            it.split(": ").let { words ->
                Pair(words[0], words[1]).toMonkey()
            }
        }

    private fun Observers.add(observer: Mather, observed: String) {
        putIfAbsent(observed, mutableListOf())
        this[observed]!!.add(observer)
    }

    private fun Observers.add(monkey: Mather) {
        add(monkey, monkey.monkey1)
        add(monkey, monkey.monkey2)
    }

    private fun Monkeys.toObservers(): Observers =
        mutableMapOf<String, MutableList<Mather>>().apply {
            filterIsInstance<Mather>()
                .forEach { mather -> add(mather) }
        }

    private fun Monkeys.process(): Long {
        val root = first { it.name == "root" }
        val observers = toObservers()

        filterIsInstance<Yeller>()
            .forEach { it.yell(observers) }

        while (true) {
            filterIsInstance<Mather>()
                .forEach { mather ->
                    if (mather.canYell()) mather.yell(observers)
                }

            if (root.canYell())
                return root.numberToYell()
        }
    }

    @Test
    fun part1() {
        assertEquals(
            66174565793494,
            computeResult {
                it.toMonkeys()
                    .process()
            }
        )
    }
}