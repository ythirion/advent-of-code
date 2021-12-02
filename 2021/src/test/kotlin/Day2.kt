import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day2 : Day(2, "Dive!") {
    data class Location(val horizontalPosition: Int, val depth: Int)

    fun String.toInstruction(): Pair<String, Int> = Pair(this.splitWords().first(), this.splitWords().last().toInt())

    private fun newLocation(currentLocation: Location, line: String): Location {
        val instruction = line.toInstruction()
        return when (instruction.first) {
            "down" -> currentLocation.copy(depth = currentLocation.depth + instruction.second)
            "up" -> currentLocation.copy(depth = currentLocation.depth - instruction.second)
            else -> currentLocation.copy(horizontalPosition = currentLocation.horizontalPosition + instruction.second)
        }
    }

    private fun calculateNewLocation(lines: List<String>): Int {
        var currentLocation = Location(0, 0)
        lines.forEach { currentLocation = newLocation(currentLocation, it) }
        return currentLocation.horizontalPosition * currentLocation.depth
    }

    @Test
    fun exercise1() =
        Assertions.assertEquals(1690020, computeResult { calculateNewLocation(it) })
}