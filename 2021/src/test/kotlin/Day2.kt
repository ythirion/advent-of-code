import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day2 : Day(2, "Dive!") {
    data class Position(val horizontal: Int = 0, val depth: Int = 0, val aim: Int = 0)
    data class Instruction(val text: String, val x: Int)

    private fun String.toInstruction(): Instruction =
        Instruction(this.splitWords().first(), this.splitWords().last().toInt())

    private fun Position.result() = this.horizontal * this.depth

    private fun Instruction.newPosition(from: Position): Position {
        return when (this.text) {
            "down" -> from.copy(depth = from.depth + this.x)
            "up" -> from.copy(depth = from.depth - this.x)
            else -> from.copy(horizontal = from.horizontal + this.x)
        }
    }

    private fun Instruction.newPositionWithAim(from: Position): Position {
        return when (this.text) {
            "down" -> from.copy(aim = from.aim + this.x)
            "up" -> from.copy(aim = from.aim - this.x)
            else -> from.copy(
                horizontal = from.horizontal + this.x,
                depth = from.depth + (from.aim * this.x)
            )
        }
    }

    private fun calculateNewLocation(
        lines: List<Instruction>,
        newLocation: (Position, Instruction) -> Position
    ): Int = lines.fold(Position(), newLocation).result()

    @Test
    fun exercise1() =
        Assertions.assertEquals(
            1690020,
            computeResult({ lines -> lines.map { it.toInstruction() } },
                { calculateNewLocation(it) { position, line -> line.newPosition(position) } })
        )

    @Test
    fun exercise2() =
        Assertions.assertEquals(
            1408487760,
            computeResult({ lines -> lines.map { it.toInstruction() } },
                { calculateNewLocation(it) { position, line -> line.newPositionWithAim(position) } })
        )
}