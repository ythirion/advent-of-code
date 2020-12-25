import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day17 : Day(17) {
    data class Point4d(val x: Int, val y: Int, val z: Int = 0, val w: Int = 0)

    private fun Point4d.add(x: Int, y: Int, z: Int, w: Int): Point4d = Point4d(this.x + x, this.y + y, this.z + z, this.w + w)

    private fun Point4d.countActiveNeighbors(
            actives: MutableSet<Point4d>,
            inFourD: Boolean = false,
            leftToCheck: MutableSet<Point4d> = mutableSetOf(),
    ): Int {
        var count = 0
        var startW = if (inFourD) -1 else 0

        (startW..1).forEach { w ->
            (-1..1).forEach { x ->
                (-1..1).forEach { y ->
                    (-1..1).forEach { z ->
                        if (Point4d(0, 0, 0, 0) != Point4d(x, y, z, w)) {
                            val neighbor = this.add(x, y, z, w)
                            if (actives.contains(neighbor)) count++
                            leftToCheck.add(neighbor)
                        }
                    }
                }
            }
        }
        return count
    }

    private fun runCycle(actives: MutableSet<Point4d>, inFourD: Boolean): MutableSet<Point4d> {
        val nextCycle = mutableSetOf<Point4d>()
        val leftToCheck = mutableSetOf<Point4d>()

        actives.forEach { active ->
            val neighbors = active.countActiveNeighbors(actives, inFourD, leftToCheck)

            if (neighbors == 2 || neighbors == 3)
                nextCycle.add(active)
        }
        leftToCheck.forEach { inactive ->
            if (!actives.contains(inactive)) {
                val neighbors = inactive.countActiveNeighbors(actives, inFourD = inFourD)

                if (neighbors == 3)
                    nextCycle.add(inactive)
            }
        }
        return nextCycle
    }

    private fun init(input: List<String>): MutableSet<Point4d> {
        val state = mutableSetOf<Point4d>()

        input.forEachIndexed { x, line ->
            line.forEachIndexed { y, c ->
                if (c == '#') state.add(Point4d(x, y))
            }
        }
        return state
    }

    private fun getStateAfter6Cycles(input: List<String>, inFourD: Boolean = false): Int {
        var gameOfLife = init(input)

        (1..6).forEach { _ ->
            gameOfLife = runCycle(gameOfLife, inFourD)
        }
        return gameOfLife.count()
    }

    @Test
    fun exercise1() = computeResult {
        Assertions.assertEquals(237, getStateAfter6Cycles(it))
    }

    @Test
    fun exercise2() = computeResult {
        Assertions.assertEquals(2448, getStateAfter6Cycles(it, true))
    }
}