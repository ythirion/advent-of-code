import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

typealias OctopusGrid = Map<Day11.Point, Day11.Octopus>

class Day11 : Day(11, "Dumbo Octopus") {
    data class Octopus(var energy: Int)
    data class Point(val x: Int, val y: Int)

    private fun List<String>.toOctopusGrid(): OctopusGrid {
        mutableMapOf<Point, Octopus>().let { grid ->
            this.forEachIndexed { x, line ->
                line.forEachIndexed { y, value ->
                    grid[Point(x, y)] = Octopus(value.toIntDigit())
                }
            }
            return grid
        }
    }

    private fun Point.adjacent(): List<Point> =
        mutableListOf<Point>().let { adjacent ->
            for (x in -1..1) {
                for (y in -1..1) {
                    if (x == 0 && y == 0) continue
                    adjacent.add(Point(this.x + x, this.y + y))
                }
            }
            return adjacent
        }

    private fun Octopus.shouldFlash(): Boolean = energy > 9
    private fun Octopus.notFlashed(): Boolean = energy != 0
    private fun Octopus.flash() {
        this.energy = 0
    }

    private fun OctopusGrid.containsFlashedOctopus(): Boolean = this.any { it.value.shouldFlash() }
    private fun OctopusGrid.increaseAll(): OctopusGrid {
        this.values.forEach { it.energy++ }
        return this
    }

    private fun OctopusGrid.increaseAdjacentEnergy(point: Point) {
        point.adjacent()
            .mapNotNull { adjacent -> this[adjacent] }
            .filter { it.notFlashed() }
            .forEach { it.energy++ }
    }

    private fun OctopusGrid.newStep(): Int {
        var flashes = 0
        increaseAll()

        while (containsFlashedOctopus()) {
            filter { it.value.shouldFlash() }.let { flashed ->
                flashes += flashed.size
                flashed.forEach { (point, octopus) ->
                    octopus.flash()
                    increaseAdjacentEnergy(point)
                }
            }
        }
        return flashes
    }

    private fun `How many total flashes are there after 100 steps`(grid: OctopusGrid): Int =
        (0 until 100).fold(0) { flashes, _ -> flashes + grid.newStep() }

    private fun `What is the first step during which all octopuses flash`(grid: OctopusGrid): Int =
        loopUntil(grid,
            { grid.newStep() },
            { flashes -> flashes == 100 })

    @Test
    fun part1() =
        Assertions.assertEquals(
            1702,
            computeMapResult(
                { it.toOctopusGrid() },
                { `How many total flashes are there after 100 steps`(it) }
            )
        )

    @Test
    fun part2() =
        Assertions.assertEquals(
            251,
            computeMapResult(
                { it.toOctopusGrid() },
                { `What is the first step during which all octopuses flash`(it) }
            )
        )
}