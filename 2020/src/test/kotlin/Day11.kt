import io.vavr.collection.Seq
import io.vavr.kotlin.toVavrList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

typealias Seats = Seq<String>
typealias Position = Pair<Int, Int>
data class Strategy(val seatSelector: (position: Position, seats: Seats) -> List<Char>, val occupiedLimit: Int)

//Crappy code
class Day11 : Day(11) {
    private val empty = 'L'
    private val occupied = '#'

    private val adjacentStrategy = Strategy({ position, seats -> seats.adjacent(position) }, 4)
    private val seenStrategy = Strategy({ position, seats -> seats.whatICanSeeFromHere(position) }, 5)

    private fun Seats.north(position: Position, selector: (currentX: Int, y: Int, countX: Int) -> Int): Seq<Char> {
        val slice = this.slice(0, position.first)
        return slice.mapIndexedNotNull { x, line ->
            line.elementAtOrNull(selector(x, position.second, slice.count()))
        }.toVavrList().reverse()
    }

    private fun Seats.south(position: Position, selector: (currentX: Int, y: Int) -> Int) =
            this.slice(position.first + 1, this.length()).mapIndexedNotNull { x, line ->
                line.elementAtOrNull(selector(x, position.second))
            }
    
    private fun Char.isASeat() = this == empty || this == occupied
    private fun Seats.equilibrium(otherSeats: Seats) = this == otherSeats
    private fun Seats.seatAtOrNull(position: Position) = this.elementAtOrNull(position.first)?.elementAtOrNull(position.second)
    private fun Seats.adjacent(position: Position) =
            listOf(Pair(-1, -1), Pair(-1, 0), Pair(-1, 1), Pair(0, -1), Pair(0, 1), Pair(1, -1), Pair(1, 0), Pair(1, 1))
                    .mapNotNull { seatAtOrNull(it + position) }

    //    private fun Seats.north(position: Position) = (0 until position.first).mapNotNull { seatAtOrNull(Pair(it, position.second)) }.reversed()
    private fun Seats.north(position: Position): Seq<Char> = this.slice(0, position.first).map { line -> line[position.second] }.reverse()
    private fun Seats.south(position: Position) = this.slice(position.first + 1, this.length()).map { it[position.second] }
    private fun Seats.west(position: Position) = this[position.first].substring(0, position.second).reversed()
    private fun Seats.east(position: Position) = this[position.first].substring(position.second + 1)
    private fun Seats.northWest(position: Position) = this.north(position) { currentX, y, countX -> y - countX + currentX }
    private fun Seats.northEast(position: Position) = this.north(position) { currentX, y, countX -> y + countX - currentX }
    private fun Seats.southEast(position: Position) = this.south(position) { currentX, y -> y + currentX + 1 }
    private fun Seats.southWest(position: Position) = this.south(position) { currentX, y -> y - currentX - 1 }

    private fun Seats.whatICanSeeFromHere(position: Position): List<Char> =
            listOfNotNull(
                    this.north(position).firstOrNull { it.isASeat() },
                    this.south(position).firstOrNull { it.isASeat() },
                    this.east(position).firstOrNull { it.isASeat() },
                    this.west(position).firstOrNull { it.isASeat() },
                    this.northWest(position).firstOrNull { it.isASeat() },
                    this.northEast(position).firstOrNull { it.isASeat() },
                    this.southWest(position).firstOrNull { it.isASeat() },
                    this.southEast(position).firstOrNull { it.isASeat() }
            )

    private fun Seats.assignSeat(position: Position, newValue: Char): Seats =
            this.replaceAt(position.first, this[position.first].replaceAt(position.second, newValue))

    private fun shiftPeople(seats: Seats, strategy: Strategy): Seats {
        var newSeats = seats

        seats.forEachIndexed { x, line ->
            line.forEachIndexed() { y, seat ->
                if (seat.isASeat()) {
                    val position = Pair(x, y)
                    val seatsToCheck = strategy.seatSelector.invoke(position, seats)

                    if (seat == empty && seatsToCheck.count { it == occupied } == 0)
                        newSeats = newSeats.assignSeat(position, occupied)
                    else if (seat == occupied && seatsToCheck.count { it == occupied } >= strategy.occupiedLimit)
                        newSeats = newSeats.assignSeat(position, empty)
                }
            }
        }

        if (!seats.equilibrium(newSeats))
            return shiftPeople(newSeats, strategy)

        return newSeats
    }

    private fun countShiftPeople(seats: Seq<String>, strategy: Strategy): Int {
        return shiftPeople(seats, strategy).joinToString(separator = "").count { it == occupied }
    }

    @Test
    fun exercise1() = computeResult { lines ->
        Assertions.assertEquals(2183, countShiftPeople(lines.toVavrList(), adjacentStrategy))
    }

    @Test
    fun exercise2() = computeResult { lines ->
        Assertions.assertEquals(1990, countShiftPeople(lines.toVavrList(), seenStrategy))
    }
}