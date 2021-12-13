import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

private data class Point(val x: Int, val y: Int)
private enum class Fold { Left, Up }
private data class FoldInstruction(val fold: Fold, val at: Int)
private typealias TransparentPaper = List<Point>

class Day13 : Day(13, "Transparent Origami") {
    private fun TransparentPaper.below(y: Int): List<Point> = filter { it.y > y }
    private fun TransparentPaper.above(y: Int): List<Point> = filter { it.y < y }
    private fun TransparentPaper.right(x: Int): List<Point> = filter { it.x > x }
    private fun TransparentPaper.left(x: Int): List<Point> = filter { it.x < x }

    private fun Point.foldVertical(instruction: FoldInstruction): Point {
        val dx = x - instruction.at
        return Point(x - (2 * dx), y)
    }

    private fun Point.foldHorizontal(instruction: FoldInstruction): Point {
        val dy = y - instruction.at
        return Point(x, y - (2 * dy))
    }

    private fun String.toPoints(): List<Point> =
        this.splitAtEmptyLine()[0]
            .splitLines()
            .map {
                val (x, y) = it.split(",")
                Point(x.toInt(), y.toInt())
            }

    private fun String.toFoldInstructions(): List<FoldInstruction> =
        this.splitAtEmptyLine()[1]
            .splitLines()
            .map {
                val (fold, at) = it.removePrefix("fold along ").split("=")
                FoldInstruction(if (fold == "x") Fold.Left else Fold.Up, at.toInt())
            }

    private fun TransparentPaper.foldHorizontal(instruction: FoldInstruction): TransparentPaper =
        this.below(instruction.at)
            .map { it.foldHorizontal(instruction) }
            .union(this.above(instruction.at))
            .distinct()

    private fun TransparentPaper.foldVertical(instruction: FoldInstruction): TransparentPaper =
        this.right(instruction.at)
            .map { it.foldVertical(instruction) }
            .union(this.left(instruction.at))
            .distinct()

    private fun TransparentPaper.fold(instruction: FoldInstruction): TransparentPaper =
        when (instruction.fold) {
            Fold.Up -> foldHorizontal(instruction)
            Fold.Left -> foldVertical(instruction)
        }

    private fun TransparentPaper.toArray(): Array<Array<Char>> = this.let {
        Array(this.maxOf { it.y + 1 }) { Array(this.maxOf { it.x + 1 }) { '.' } }
            .apply { it.forEach { p -> this[p.y][p.x] = '#' } }
    }

    private fun TransparentPaper.print() =
        this.toArray()
            .forEach { line ->
                line.forEach { print(it) }
                println()
            }

    private fun `How many dots are visible after completing just the first fold instruction on your transparent paper`(
        input: String
    ): Int =
        input.toFoldInstructions()[0].let {
            return input.toPoints()
                .fold(it)
                .count()
        }

    private fun `What code do you use to activate the infrared thermal imaging camera system`(
        input: String
    ) = input.toFoldInstructions()
        .fold(input.toPoints()) { paper, instruction -> paper.fold(instruction) }
        .print()

    @Test
    fun part1() =
        Assertions.assertEquals(
            701,
            computeStringResult {
                `How many dots are visible after completing just the first fold instruction on your transparent paper`(
                    it
                )
            }
        )

    @Test
    fun part2() = computeStringResult {
        `What code do you use to activate the infrared thermal imaging camera system`(it)
    }
}