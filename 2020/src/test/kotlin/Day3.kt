import io.vavr.collection.Vector
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day3 : Day(3) {
    private class Move(val right: Int, val down: Int)

    private fun slope(input: List<String>, move: Move): Long =
            input.mapIndexed { index, row ->
                val (x, cIndex) = Pair(move.right * index, move.right * index / move.down)
                (x.divisible(move.down) && row.at(cIndex, '#')).toInt()
            }.fold(0L, Long::plus)

    @Test
    fun exercise1() = Assertions.assertEquals(280, computeResult { slope(it, Move(3, 1)) })

    @Test
    fun exercise2() = Assertions.assertEquals(4355551200, computeResult { lines ->
        Vector.of(
                Move(1, 1),
                Move(3, 1),
                Move(5, 1),
                Move(7, 1),
                Move(1, 2))
                .map { slope(lines, it) }
                .multiply()
    })


}
