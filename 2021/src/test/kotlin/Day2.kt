import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day2 : Day(2, "Dive!") {
    data class Location(val horizontalPosition: Int = 0, val depth: Int = 0, val aim: Int = 0)

    private fun String.toInstruction(): Pair<String, Int> =
        Pair(this.splitWords().first(), this.splitWords().last().toInt())

    private fun newLocation(currentLocation: Location, line: String): Location {
        val instruction = line.toInstruction()
        return when (instruction.first) {
            "down" -> currentLocation.copy(depth = currentLocation.depth + instruction.second)
            "up" -> currentLocation.copy(depth = currentLocation.depth - instruction.second)
            else -> currentLocation.copy(horizontalPosition = currentLocation.horizontalPosition + instruction.second)
        }
    }

    private fun newLocationWithAim(currentLocation: Location, line: String): Location {
        val instruction = line.toInstruction()
        return when (instruction.first) {
            "down" -> currentLocation.copy(aim = currentLocation.aim + instruction.second)
            "up" -> currentLocation.copy(aim = currentLocation.aim - instruction.second)
            else -> currentLocation.copy(
                horizontalPosition = currentLocation.horizontalPosition + instruction.second,
                depth = currentLocation.depth + (currentLocation.aim * instruction.second)
            )
        }
    }

    private fun calculateNewLocation(
        lines: List<String>,
        newLocation: (Location, String) -> Location
    ): Int {
        var currentLocation = Location(0, 0)
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