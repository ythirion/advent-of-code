import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Day5 : Day(5, "Hydrothermal Venture") {
    data class Point(val x: Int, val y: Int)
    data class LineOfVents(val from: Point, val to: Point)

    private fun List<LineOfVents>.maxX(): Int = this.map { max(it.from.x, it.to.x) }.maxOf { it }
    private fun List<LineOfVents>.maxY() = this.map { max(it.from.y, it.to.y) }.maxOf { it }
    private fun initDiagram(lineOfVents: List<LineOfVents>): Array<Array<Int>> =
        Array(lineOfVents.maxY() + 1) { Array(lineOfVents.maxX() + 1) { 0 } }

    private val regex = Regex("""^(\d+),(\d+) -> (\d+),(\d+)""")
    private fun String.toLineOfVents(): LineOfVents =
        regex.matchEntire(this)!!
            .destructured
            .let { (x1, y1, x2, y2) -> LineOfVents(Point(x1.toInt(), y1.toInt()), Point(x2.toInt(), y2.toInt())) }

    private fun countIntersections(
        lineOfVents: List<LineOfVents>,
        countDiagonals: Boolean = false,
        filter: (LineOfVents) -> Boolean
    ): Int {
        val diagram = initDiagram(lineOfVents)

        return lineOfVents
            .filter { filter(it) }
            .sumOf { it.countIntersections(diagram, countDiagonals) }
    }

    private fun LineOfVents.countIntersections(
        diagram: Array<Array<Int>>,
        useDiagonals: Boolean
    ): Int {
        var intersections = 0

        val (p1, p2) = this
        val (x1, y1) = p1
        val (x2, y2) = p2

        when {
            x1 == x2 -> {
                for (y in min(y1, y2)..max(y1, y2)) {
                    if (++diagram[y][x1] == 2) intersections++
                }
            }
            y1 == y2 -> {
                for (x in min(x1, x2)..max(x1, x2)) {
                    if (++diagram[y1][x] == 2) intersections++
                }
            }
            else -> {
                if (useDiagonals) {
                    val d = abs(x1 - x2)
                    val dx = (x2 - x1) / d
                    val dy = (y2 - y1) / d

                    for (i in 0..d) {
                        if (++diagram[y1 + dy * i][x1 + dx * i] == 2) intersections++
                    }
                }
            }
        }
        return intersections
    }

    @Test
    fun exercise1() =
        Assertions.assertEquals(
            6397,
            computeResult({ it.map { line -> line.toLineOfVents() } },
                { countIntersections(it) { line -> line.from.x == line.to.x || line.from.y == line.to.y } })
        )

    @Test
    fun exercise2() =
        Assertions.assertEquals(
            22335,
            computeResult({ it.map { line -> line.toLineOfVents() } },
                { countIntersections(it, countDiagonals = true) { true } })
        )
}