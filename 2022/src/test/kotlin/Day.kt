import java.nio.file.Files
import java.nio.file.Path
import java.util.*

abstract class Day(day: Int, name: String) {
    init {
        println("Day $day : $name")
    }

    //region Compute functions
    private val input = Day::class.java.classLoader.getResource("day$day.txt")

    fun <R> computeResult(answer: (input: List<String>) -> R): R = answer(getInput())
    fun <TInput, R> computeResult(
        transform: (input: List<String>) -> List<TInput>,
        answer: (input: List<TInput>) -> R
    ): R = answer(transform(getInput()))

    fun <TKey, TValue, R> computeMapResult(
        transform: (input: List<String>) -> Map<TKey, TValue>,
        answer: (input: Map<TKey, TValue>) -> R
    ): R = answer(transform(getInput()))

    fun <R> computeLongResult(answer: (input: List<Long>) -> R): R = answer(getInput().map { it.toLong() })
    fun <R> computeIntResult(answer: (input: List<Int>) -> R): R = answer(getInput().map { it.toInt() })
    fun <R> computeIntSeparatedResult(answer: (input: List<Int>) -> R): R =
        answer(getInputAsString().split(',').map { it.toInt() })

    fun <R> computeStringResult(answer: (input: String) -> R): R = answer(getInputAsString())
    fun <R> computeStringSeparatedLinesResult(answer: (input: List<String>) -> R): R =
        answer(getInputAsSeparatedLines())
    
    private fun getInput() = Files.readAllLines(Path.of(Objects.requireNonNull(input).toURI()))
    private fun getInputAsString() = Files.readString(Path.of(Objects.requireNonNull(input).toURI()))
    private fun getInputAsSeparatedLines() = getInputAsString().split("\n\n")
    //endregion
}