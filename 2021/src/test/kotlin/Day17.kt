import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day17 : Day(17, "Trick Shot") {
    private val targetArea = TargetArea(192..251, -89..-59)

    data class TargetArea(val x: IntRange, val y: IntRange)
    data class Point(val x: Int, val y: Int)
    data class Velocity(val x: Int, val y: Int)

    private fun TargetArea.contains(p: Point): Boolean = this.x.contains(p.x) && this.y.contains(p.y)
    private fun TargetArea.isOut(p: Point): Boolean = p.x > this.x.last || p.y < this.y.first
    private fun TargetArea.allVelocity(): List<Velocity> =
        (0..this.x.last).flatMap { xVelocity ->
            (this.y.first..-this.y.first).map { yVelocity -> Velocity(xVelocity, yVelocity) }
        }

    private fun Point.step(velocity: Velocity): Point = copy(x = x + velocity.x, y = y + velocity.y)
    private fun Velocity.step(): Velocity = copy(x = if (x == 0) 0 else x - 1, y = y - 1)
    private fun Pair<Point, Velocity>.step(): Pair<Point, Velocity> = Pair(first.step(second), second.step())
    private fun List<Velocity>.maxHigh() = this.map { it.y }.maxOf { dy -> dy * (dy + 1) / 2 }

    private fun Velocity.willReach(targetArea: TargetArea): Boolean {
        return loopUntilGet(
            Pair(Point(0, 0), this),
            { it.step() },
            {
                when {
                    targetArea.contains(it.first) -> true
                    targetArea.isOut(it.first) -> false
                    else -> null
                }
            }
        )
    }

    private fun TargetArea.findAllInitialPositionsReachingTarget(): List<Velocity> =
        allVelocity().filter { it.willReach(this) }

    private fun `what is the highest y position it reaches on this trajectory`(): Int =
        targetArea
            .findAllInitialPositionsReachingTarget()
            .maxHigh()

    private fun `how many distinct initial velocity values cause the probe to be within the target area after any step`(): Int =
        targetArea
            .findAllInitialPositionsReachingTarget()
            .size

    @Test
    fun part1() =
        Assertions.assertEquals(
            3916,
            `what is the highest y position it reaches on this trajectory`()
        )

    @Test
    fun part2() =
        Assertions.assertEquals(
            2986,
            `how many distinct initial velocity values cause the probe to be within the target area after any step`()
        )
}

