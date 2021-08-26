import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class Day12 : Day(12) {
    private val forward = 'F'
    private val right = 'R'
    private val left = 'L'
    private val cardinalPoints = CardinalPoint.values()


    private enum class CardinalPoint(val deltaX: Int, val deltaY: Int) { N(0, 1), E(1, 0), S(0, -1), W(-1, 0) }
    private data class Point2d(val x: Int, val y: Int)
    private data class NavigationInstruction(val action: Char, val value: Int)

    private operator fun Point2d.plus(point: Point2d): Point2d = Point2d(this.x + point.x, this.y + point.y)

    private fun Char.toCardinalPoint() = CardinalPoint.valueOf(this.toString())
    private fun String.toNavigationInstruction() = NavigationInstruction(this[0], this.substring(1).toInt())

    //To rotate clockwise, replace (𝑥,𝑦) with (𝑦,−𝑥).
    //To rotate counter clockwise replace (𝑥,𝑦) with (−𝑦,𝑥)
    //https://math.stackexchange.com/questions/1330161/how-to-rotate-points-through-90-degree
    private fun Point2d.rotateClockwise(times: Int): Point2d = (0 until times).fold(this) { newPoint, _ -> Point2d(newPoint.y, -newPoint.x) }
    private fun Point2d.rotateCounterClockwise(times: Int): Point2d = (0 until times).fold(this) { newPoint, _ -> Point2d(-newPoint.y, newPoint.x) }
    private fun Point2d.manhattanDistance() = this.x.absoluteValue + this.y.absoluteValue
    private fun Point2d.move(direction: CardinalPoint, value: Int) = this + Point2d(direction.deltaX * value, direction.deltaY * value)
    private fun CardinalPoint.changeDirection(instruction: NavigationInstruction): CardinalPoint {
        var newIndex = if (instruction.action == left) (this.ordinal - (instruction.value / 90)) % 4 else (this.ordinal + (instruction.value / 90)) % 4
        if (newIndex < 0) newIndex += cardinalPoints.size
        return cardinalPoints[newIndex]
    }

    private fun moveFromStartingLocation(instructions: List<NavigationInstruction>): Point2d {
        var position = Point2d(0, 0)
        var direction = CardinalPoint.E

        instructions.forEach { navigationInstruction ->
            when (navigationInstruction.action) {
                forward -> position = position.move(direction, navigationInstruction.value)
                left, right -> direction = direction.changeDirection(navigationInstruction)
                else -> position = position.move(navigationInstruction.action.toCardinalPoint(), navigationInstruction.value)
            }
        }
        return position
    }

    private fun moveWithWaypoint(instructions: List<NavigationInstruction>): Point2d {
        var position = Point2d(0, 0)
        var waypoint = Point2d(10, 1)

        instructions.forEach { navigationInstruction ->
            when (navigationInstruction.action) {
                forward -> position += Point2d(waypoint.x * navigationInstruction.value, waypoint.y * navigationInstruction.value)
                left -> waypoint = waypoint.rotateCounterClockwise(navigationInstruction.value / 90)
                right -> waypoint = waypoint.rotateClockwise(navigationInstruction.value / 90)
                else -> waypoint = waypoint.move(navigationInstruction.action.toCardinalPoint(), navigationInstruction.value)
            }
        }
        return position
    }

    @Test
    fun exercise1() = computeResult {
        Assertions.assertEquals(2297, moveFromStartingLocation(it.map { l -> l.toNavigationInstruction() }).manhattanDistance())
    }

    @Test
    fun exercise2() = computeResult {
        Assertions.assertEquals(89984, moveWithWaypoint(it.map { l -> l.toNavigationInstruction() }).manhattanDistance())
    }
}