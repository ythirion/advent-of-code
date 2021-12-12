import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

typealias Cave = String
typealias MiniPath = Pair<Cave, Cave>

class Day12 : Day(12, "Passage Pathing") {
    private val start = "start"
    private val end = "end"

    private fun Cave.isBigCave() = this[0].isUpperCase()
    private fun List<Cave>.notAlreadyPassedInSmallCave(cave: Cave) = !contains(cave)
    private fun List<Cave>.reachedEnd() = last() == end
    private fun List<Cave>.canVisitSmallCave(smallCave: Cave) = count { it == smallCave } < 2

    private fun List<String>.toPaths(): List<MiniPath> = this.map {
        val (from, to) = it.split("-")
        from to to
    }

    private fun List<MiniPath>.connectedCaves(): Map<Cave, List<Cave>> =
        mutableMapOf<Cave, MutableList<String>>().let { paths ->
            this.forEach { (from, to) ->
                paths.getOrPut(from) { mutableListOf() }.add(to)
                paths.getOrPut(to) { mutableListOf() }.add(from)
            }
            return paths
        }

    private fun List<MiniPath>.smallCaves(): List<String> =
        this.flatMap { it.toList() }
            .filter { !it.isBigCave() && it !in listOf(start, end) }
            .distinct()

    private fun Map<Cave, List<Cave>>.findAllPaths(constraint: (visitedCaves: List<Cave>, currentCave: Cave) -> Boolean): Set<List<Cave>> =
        mutableSetOf<List<String>>().let {
            findAllPaths(listOf(start), it, constraint)
            return it
        }

    private fun Map<Cave, List<Cave>>.findAllPaths(
        visitedCaves: List<Cave>,
        result: MutableSet<List<Cave>>,
        constraint: (visitedCaves: List<Cave>, currentCave: Cave) -> Boolean
    ) {
        if (visitedCaves.reachedEnd()) {
            result += visitedCaves
            return
        }

        this.getValue(visitedCaves.last())
            .minus(start)
            .filter { cave ->
                cave.isBigCave() || visitedCaves.notAlreadyPassedInSmallCave(cave)
                        || constraint(visitedCaves, cave)
            }
            .forEach { potentialNextCave ->
                findAllPaths(visitedCaves + potentialNextCave, result, constraint)
            }
    }


    private fun `How many paths through this cave system are there that visit small caves at most once`(
        paths: List<MiniPath>
    ): Int = paths.connectedCaves()
        .findAllPaths { _, _ -> false }
        .count()

    private fun `How many paths through this cave system are there`(
        paths: List<MiniPath>
    ): Int = mutableSetOf<List<String>>().apply {
        val connectedCaves = paths.connectedCaves()

        paths.smallCaves()
            .forEach { smallCave ->
                this + connectedCaves.findAllPaths { visitedCaves, currentCave ->
                    currentCave == smallCave && visitedCaves.canVisitSmallCave(smallCave)
                }
            }
    }.count()

    @Test
    fun part1() =
        Assertions.assertEquals(
            3713,
            computeResult(
                { it.toPaths() },
                { `How many paths through this cave system are there that visit small caves at most once`(it) }
            )
        )

    @Test
    fun part2() =
        Assertions.assertEquals(
            91292,
            computeResult(
                { it.toPaths() },
                { `How many paths through this cave system are there`(it) }
            )
        )
}