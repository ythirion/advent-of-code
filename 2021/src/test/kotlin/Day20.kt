import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

typealias ImageEnhancement = String
typealias Image = Map<Point2D, Char>

data class Point2D(val x: Int, val y: Int)

class Day20 : Day(20, "Trench Map") {
    private val infinite = '.'
    private fun Char.isLit() = this == '#'
    private fun Map<Point2D, Char>.minX(): Int = minByOrNull { it.key.x }!!.key.x
    private fun Map<Point2D, Char>.maxX(): Int = maxByOrNull { it.key.x }!!.key.x
    private fun Map<Point2D, Char>.minY(): Int = minByOrNull { it.key.y }!!.key.y
    private fun Map<Point2D, Char>.maxY(): Int = maxByOrNull { it.key.y }!!.key.y

    private fun Char.newDefault(imageEnhancement: ImageEnhancement): Char =
        List(9) { this }.enhance(imageEnhancement)

    private fun String.toImage(): Image =
        splitLines()
            .flatMapIndexed { y, row -> row.mapIndexed { x, value -> Point2D(x, y) to value } }
            .toMap()
            .withDefault { infinite }

    private fun List<Char>.enhance(imageEnhancement: ImageEnhancement): Char =
        map { if (it.isLit()) '1' else '0' }
            .joinToString("")
            .toInt(2)
            .let { index -> imageEnhancement[index] }

    private fun Point2D.squareAround(): List<Point2D> = listOf(
        Point2D(x - 1, y - 1),
        Point2D(x, y - 1),
        Point2D(x + 1, y - 1),
        Point2D(x - 1, y),
        this,
        Point2D(x + 1, y),
        Point2D(x - 1, y + 1),
        Point2D(x, y + 1),
        Point2D(x + 1, y + 1)
    )

    private fun Image.enhance(point: Point2D, imageEnhancement: ImageEnhancement): Char =
        point.squareAround()
            .map { getValue(it) }
            .enhance(imageEnhancement)

    private fun Image.countLitPixels(): Int = values.count { it.isLit() }

    private fun Image.enhance(
        imageEnhancement: ImageEnhancement
    ): Image = mutableMapOf<Point2D, Char>().let {
        (minY() - 1..maxY() + 1).forEach { y ->
            (minX() - 1..maxX() + 1).forEach { x ->
                val point = Point2D(x, y)
                it[point] = enhance(point, imageEnhancement)
            }
        }
        return it
    }

    private fun Image.process(
        times: Int,
        imageEnhancement: ImageEnhancement
    ): Image {
        var default = '.'
        var image = this

        repeat(times) {
            image = image.enhance(imageEnhancement)
            default = default.newDefault(imageEnhancement)
            image = image.withDefault { default }
        }
        return image
    }

    private fun `how many pixels are lit in the resulting image after n enhancements`(
        input: String,
        repeated: Int
    ): Int = input.splitAtEmptyLine().let {
        it[1].toImage()
            .process(repeated, it[0])
            .countLitPixels()
    }

    @Test
    fun part1() =
        Assertions.assertEquals(
            5225,
            computeStringResult { `how many pixels are lit in the resulting image after n enhancements`(it, 2) }
        )

    @Test
    fun part2() =
        Assertions.assertEquals(
            18131,
            computeStringResult { `how many pixels are lit in the resulting image after n enhancements`(it, 50) }
        )
}