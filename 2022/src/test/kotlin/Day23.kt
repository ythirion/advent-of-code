import Day23.Direction.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.math.abs

typealias ElvesLocations = List<Point2D>

class Day23 : Day(23, "Unstable Diffusion") {
    private enum class Direction(val value: Point2D) {
        N(Point2D(1, 0)), NE(Point2D(1, 1)), NW(Point2D(1, -1)),
        S(Point2D(-1, 0)), SE(Point2D(-1, 1)), SW(Point2D(-1, -1)),
        E(Point2D(0, 1)), W(Point2D(0, -1));
    }

    private fun Point2D.moveTo(direction: Direction): Point2D =
        Point2D(x + direction.value.x, y + direction.value.y)

    private fun List<String>.toElves(): ElvesLocations =
        mutableListOf<Point2D>().let { elves ->
            reversed().forEachIndexed { x, line ->
                line.forEachIndexed { y, c ->
                    if (c == '#') elves.add(Point2D(x, y))
                }
            }
            return elves
        }

    private fun Point2D.whatsAt(direction: Direction, elves: ElvesLocations): Point2D? =
        moveTo(direction).let { point ->
            return if (elves.firstOrNull { it == point } != null) point else null
        }

    private fun Point2D.adjacentElves(elves: ElvesLocations): Map<Direction, Point2D> =
        Direction.values()
            .associateWith { direction -> whatsAt(direction, elves) }
            .filterNot { it.value == null }
            .mapValues { it.value!! }

    private fun Map.Entry<Point2D, Map<Direction, Point2D>>.propose(direction: Direction): Point2D? =
        if (!containsElves(direction)) key.moveTo(direction) else null

    private fun Map.Entry<Point2D, Map<Direction, Point2D>>.proposeFirstValidDirection(strategy: Queue<Direction>): Point2D? =
        strategy.toList()
            .map { direction -> this.propose(direction) }
            .firstNotNullOfOrNull { it }

    private fun Map.Entry<Point2D, Map<Direction, Point2D>>.containsElves(direction: Direction) =
        value.filterKeys {
            when (direction) {
                N -> it in listOf(N, NE, NW)
                S -> it in listOf(S, SE, SW)
                W -> it in listOf(W, NW, SW)
                else -> it in listOf(E, NE, SE)
            }
        }.any()

    private fun ElvesLocations.moveElves(newElvesLocations: Map<Point2D, Point2D>): ElvesLocations =
        toMutableList().let { newElves ->
            newElves.removeAll(newElvesLocations.keys)
            newElves.addAll(newElvesLocations.values)

            return newElves
        }

    private fun Queue<Direction>.updateStrategy(): Direction =
        this.remove().let { strategy ->
            this.add(strategy)
            return strategy
        }

    private fun ElvesLocations.elvesToMove(strategy: Queue<Direction>): Map<Point2D, Point2D> =
        this.associateWith { elf -> elf.adjacentElves(this) }
            .filter { it.value.any() }
            .let { elves ->
                elves.map { elf -> elf.key to elf.proposeFirstValidDirection(strategy) }
                    .toMap()
                    .let { proposal ->
                        return proposal
                            .filter { p -> p.value != null && proposal.count { it.value == p.value } == 1 }
                            .mapValues { v -> v.value!! }
                    }
            }

    private fun ElvesLocations.round(stop: (times: Int, elvesToMove: Int) -> Boolean): Result =
        LinkedList(listOf(N, S, W, E)).let { strategy ->
            var elves = this
            var round = 0

            do {
                val toMove = elves.elvesToMove(strategy)
                elves = elves.moveElves(toMove)
                strategy.updateStrategy()

                round++
            } while (!stop(round, toMove.size))

            return Result(round, elves)
        }

    private data class Result(val round: Int, val elves: ElvesLocations)

    private fun ElvesLocations.emptyTiles(): Int =
        (maxBy { it.x }.x + 1 + abs(minBy { it.x }.x)) *
                (maxBy { it.y }.y + 1 + abs(minBy { it.y }.y)) - count()

    @Test
    fun part1() {
        assertEquals(
            4146,
            computeResult {
                it.toElves()
                    .round { times, _ -> times == 10 }
                    .elves
                    .emptyTiles()
            }
        )
    }

    @Test
    fun part2() {
        assertEquals(
            957,
            computeResult {
                it.toElves()
                    .round { _, elvesToMove -> elvesToMove == 0 }
                    .round
            }
        )
    }
}