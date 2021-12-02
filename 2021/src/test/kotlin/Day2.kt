import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day2 : Day(2, "Dive!") {
    data class Location(val horizontalPosition: Int = 0, val depth: Int = 0, val aim: Int = 0)
    data class Instruction(val text: String, val x: Int)

    private fun Instruction.newLocation(from: Location): Location {
        return when (this.text) {
            "down" -> from.copy(depth = from.depth + this.x)
            "up" -> from.copy(depth = from.depth - this.x)
            else -> from.copy(horizontalPosition = from.horizontalPosition + this.x)
        }
    }

    private fun Instruction.newLocationWithAim(from: Location): Location {
        return when (this.text) {
            "down" -> from.copy(aim = from.aim + this.x)
            "up" -> from.copy(aim = from.aim - this.x)
            else -> from.copy(
                horizontalPosition = from.horizontalPosition + this.x,
                depth = from.depth + (from.aim * this.x)
            )
        }
    }

    private fun String.toInstruction(): Instruction =
        Instruction(this.splitWords().first(), this.splitWords().last().toInt())

    private fun newLocation(currentLocation: Location, line: String): Location =
        line.toInstruction().newLocation(currentLocation)

    private fun newLocationWithAim(currentLocation: Location, line: String): Location =
        line.toInstruction().newLocationWithAim(currentLocation)

    private fun calculateNewLocation(
        lines: List<String>,
        newLocation: (Location, String) -> Location
    ): Int {
        var currentLocation = Location()
        lines.forEach { currentLocation = newLocation(currentLocation, it) }
        return currentLocation.horizontalPosition * currentLocation.depth
    }

    @Test
    fun exercise1() =
        Assertions.assertEquals(1690020, computeResult {
            calculateNewLocation(it) { current, line -> newLocation(current, line) }
        })

    @Test
    fun exercise2() =
        Assertions.assertEquals(1408487760, computeResult {
            calculateNewLocation(it) { current, line -> newLocationWithAim(current, line) }
        })
}