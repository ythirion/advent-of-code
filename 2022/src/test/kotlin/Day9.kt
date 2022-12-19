import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue
import kotlin.math.max

typealias Direction = Char
typealias Rope = MutableList<Point2D>

class Day9 : Day(9, "Rope Bridge") {
    private val directionToMovement = mapOf(
        'L' to Pair(0, -1),
        'R' to Pair(0, 1),
        'U' to Pair(1, 0),
        'D' to Pair(-1, 0)
    )

    private data class Instruction(val direction: Direction, val steps: Int)

    private fun String.toInstruction(): Instruction =
        splitWords().let { words -> return Instruction(words[0][0], words[1].toInt()) }

    private fun List<String>.toInstructions(): List<Instruction> = map { it.toInstruction() }

    private fun createRope(size: Int): Rope =
        mutableListOf<Point2D>().let { rope ->
            (0 until size).forEach { _ -> rope.add(Point2D(0, 0)) }
            return rope
        }

    operator fun Point2D.plus(movement: Pair<Int, Int>): Point2D =
        Point2D(x + movement.first, y + movement.second)

    private fun Point2D.abs(): Point2D = Point2D(x.absoluteValue, y.absoluteValue)
    private fun Point2D.isNotTouching() = max(x, y) == 2

    private fun List<Instruction>.moveRope(size: Int): MutableSet<Point2D> =
        createRope(size).let { rope ->
            mutableSetOf<Point2D>().let { visited ->
                forEach { instruction ->
                    rope.move(instruction) { tail ->
                        visited.add(tail)
                    }
                }
                return visited
            }
        }

    private fun Rope.move(
        instruction: Instruction,
        visitor: (Point2D) -> Unit
    ) {
        repeat(instruction.steps) {
            this[0] = this[0] + directionToMovement[instruction.direction]!!

            (1 until size).forEach { i ->
                val (head, tail) = Pair(this[i - 1], this[i])
                val absoluteDiff = (head - tail).abs()

                if (absoluteDiff.isNotTouching())
                    this[i] = Point2D(
                        head.x.distanceFrom(tail.x, absoluteDiff.x == 2),
                        head.y.distanceFrom(tail.y, absoluteDiff.y == 2)
                    )
            }
            visitor(this.last())
        }
    }

    private fun Int.distanceFrom(tail: Int, check: Boolean): Int =
        if (check) (this + tail) / 2 else this

    @Test
    fun part1() =
        assertEquals(
            6243,
            computeResult {
                it.toInstructions()
                    .moveRope(2)
                    .size
            }
        )

    @Test
    fun part2() =
        assertEquals(
            2630,
            computeResult {
                it.toInstructions()
                    .moveRope(10)
                    .size
            }
        )
}