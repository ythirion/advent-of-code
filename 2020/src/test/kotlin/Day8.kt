import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

typealias Game = List<Instruction>
abstract class GameResult(val result: Int)
data class Failure(val acc: Int) : GameResult(acc)
data class Success(val acc: Int) : GameResult(acc)
data class Instruction(val operation: String, val value: Int)

class Day8 : Day(8) {
    private val jump = "jmp"
    private val accumulate = "acc"
    private val nope = "nop"

    private fun List<String>.toGame(): Game = this.map {
        val split = it.split(' ')
        Instruction(split[0], split[1].toInt())
    }

    private fun Instruction.accumulate(acc: Int) = if (this.operation == accumulate) acc + this.value else acc
    private fun Instruction.nextIndex(currentIndex: Int) = if (this.operation == jump) currentIndex + this.value else currentIndex + 1
    private fun Instruction.replaceOperation(newOperation: String) = Instruction(newOperation, this.value)

    private fun Game.isLastInstruction(index: Int) = index == this.size
    private fun Game.fix(indexToFix: Int, to: String): Game = this.replaceAt(indexToFix, this[indexToFix].replaceOperation(to))

    private fun Game.launch(): GameResult {
        val visitedIndexes = mutableSetOf<Int>()
        var currentIndex = 0
        var acc = 0

        while (!this.isLastInstruction(currentIndex)) {
            if (currentIndex in visitedIndexes) return Failure(acc)

            visitedIndexes.add(currentIndex)
            acc = this[currentIndex].accumulate(acc)
            currentIndex = this[currentIndex].nextIndex(currentIndex)
        }
        return Success(acc)
    }

    private fun fixGame(originalGame: Game, indexToFix: Int, fromOperation: String, toOperation: String) =
            if (originalGame[indexToFix].operation == fromOperation) originalGame.fix(indexToFix, toOperation)
            else originalGame

    private fun generateFixingCombinations(game: Game, fromOperation: String, toOperation: String) =
            game.mapIndexed { index, _ -> fixGame(game, index, fromOperation, toOperation) }
                    .plus(game.mapIndexed { index, _ -> fixGame(game, index, toOperation, fromOperation) })

    private fun getFirstGameSuccess(originalGame: Game): GameResult =
            generateFixingCombinations(originalGame, jump, nope)
                    .map { it.launch() }
                    .first { result -> result is Success }

    @Test
    fun exercise1() = computeResult {
        Assertions.assertEquals(Failure(1600), it.toGame().launch())
    }

    @Test
    fun exercise2() = computeResult {
        Assertions.assertEquals(Success(1543), getFirstGameSuccess(it.toGame()))
    }
}