import Day23.Direction.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.math.abs

typealias ElvesLocations = List<Point2D>

class Day23 : Day(23, "Unstable Diffusion") {
    private val elfSymbol = '#'
    private val emptySymbol = '.'

    private enum class Direction(val value: Point2D) {
        N(Point2D(1, 0)), NE(Point2D(1, 1)), NW(Point2D(1, -1)),
        S(Point2D(-1, 0)), SE(Point2D(-1, 1)), SW(Point2D(-1, -1)),
        E(Point2D(0, 1)), W(Point2D(0, -1));

        fun isNorth() = this in listOf(N, NE, NW)
        fun isSouth() = this in listOf(S, SE, SW)
        fun isWest() = this in listOf(W, NW, SW)
        fun isEast() = this in listOf(E, NE, SE)
    }

    private fun Point2D.moveTo(direction: Direction): Point2D =
        Point2D(x + direction.value.x, y + direction.value.y)

    private fun List<String>.toElves(): ElvesLocations =
        mutableListOf<Point2D>().let { elves ->
            reversed().forEachIndexed { x, line ->
                line.forEachIndexed { y, c ->
                    if (c == elfSymbol) elves.add(Point2D(x, y))
                }
            }
            return elves
        }

    private fun Point2D.whatsAt(direction: Direction, elves: ElvesLocations): Pair<Point2D, Char> =
        moveTo(direction).let { point ->
            return Pair(point, if (elves.firstOrNull { it == point } != null) elfSymbol else emptySymbol)
        }

    private fun Point2D.adjacent(elves: ElvesLocations): Map<Direction, Pair<Point2D, Char>> =
        values().associateWith { direction ->
            whatsAt(direction, elves)
        }

    private fun Map<Direction, Pair<Point2D, Char>>.containsElves(): Boolean = values.any { it.second == elfSymbol }

    private fun Map.Entry<Point2D, Map<Direction, Pair<Point2D, Char>>>.propose(direction: Direction): Point2D? =
        if (!containsElves(direction)) key.moveTo(direction) else null

    private fun Map.Entry<Point2D, Map<Direction, Pair<Point2D, Char>>>.proposeFirstValidDirection(strategy: Queue<Direction>): Point2D? =
        strategy.toList()
            .map { direction -> this.propose(direction) }
            .firstNotNullOfOrNull { it }

    private fun Map.Entry<Point2D, Map<Direction, Pair<Point2D, Char>>>.containsElves(direction: Direction) =
        value.filterKeys {
            when (direction) {
                N -> it.isNorth()
                S -> it.isSouth()
                W -> it.isWest()
                else -> it.isEast()
            }
        }.containsElves()

    private fun ElvesLocations.moveElves(newElvesLocations: Map<Point2D, Point2D>): ElvesLocations =
        toMutableList().let { newElves ->
            newElves.removeAll(newElvesLocations.keys)
            newElves.addAll(newElvesLocations.values)

            return newElves
        }

    private fun Queue<Direction>.circularDequeue(): Direction =
        this.remove().let { strategy ->
            this.add(strategy)
            return strategy
        }

    private fun ElvesLocations.round(strategy: Queue<Direction>): ElvesLocations {
        this.associateWith { elf -> elf.adjacent(this) }
            .filter { it.value.containsElves() }.let { elves ->
                val proposal = elves
                    .map { elf -> elf.key to elf.proposeFirstValidDirection(strategy) }
                    .toMap()

                return moveElves(
                    proposal.filter { p -> p.value != null && proposal.count { it.value == p.value } == 1 }
                        .mapValues { v -> v.value!! }
                )
            }
    }

    private fun ElvesLocations.round(times: Int): ElvesLocations {
        val strategy = LinkedList(listOf(N, S, W, E))
        var elves = this.toList()

        repeat(times) {
            elves = elves.round(strategy)
            strategy.circularDequeue()
        }
        return elves
    }

    private fun ElvesLocations.emptyTiles(): Int =
        (maxBy { it.x }.x + 1 + abs(minBy { it.x }.x)) *
                (maxBy { it.y }.y + 1 + abs(minBy { it.y }.y)) -
                this.count()

    @Test
    fun part1() {
        assertEquals(
            110,
            computeResult {
                it.toElves()
                    .round(10)
                    .emptyTiles()
            }
        )
    }
}