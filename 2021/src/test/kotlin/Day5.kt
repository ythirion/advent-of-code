import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.max
import kotlin.math.min

class Day5 : Day(5, "Hydrothermal Venture") {
    data class Point(val x: Int, val y: Int)
    data class LineOfVents(val from: Point, val to: Point)

    private fun List<LineOfVents>.maxX(): Int = this.map { max(it.from.x, it.to.x) }.maxOf { it }
    private fun List<LineOfVents>.maxY() = this.map { max(it.from.y, it.to.y) }.maxOf { it }

    private val regex = Regex("""^(\d+),(\d+) -> (\d+),(\d+)""")
    private fun String.toLineOfVents(): LineOfVents =
        regex.matchEntire(this)!!
            .destructured
            .let { (x1, y1, x2, y2) -> LineOfVents(Point(x1.toInt(), y1.toInt()), Point(x2.toInt(), y2.toInt())) }

    private fun solve(
        lineOfVents: List<LineOfVents>
    ): Int {
        val diagram = Array(lineOfVents.maxY() + 1) { Array(lineOfVents.maxX() + 1) { 0 } }
        var intersections = 0

        lineOfVents
            .filter { line -> line.from.x == line.to.x || line.from.y == line.to.y }
            .forEach { line ->
                val (p1, p2) = line
                val (x1, y1) = p1
                val (x2, y2) = p2

                when {
                    x1 == x2 -> {
                        for (y in min(y1, y2)..max(y1, y2)) {
                            if (++diagram[y][x1] == 2) {
                                intersections++
                            }
                        }
                    }
                    y1 == y2 -> {
                        for (x in min(x1, x2)..max(x1, x2)) {
                            if (++diagram[y1][x] == 2) {
                                intersections++
                            }
                        }
                    }
                }
            }
        return intersections
    }

    @Test
    fun exercise1() =
        Assertions.assertEquals(
            51034,
            computeResult({ lines -> lines.map { it.toLineOfVents() } },
                { solve(it) })
        )

    @Test
    fun exercise2() =
        Assertions.assertEquals(
            5434,
            computeResult { }
        )
}