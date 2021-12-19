import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.abs

data class Point3D(val x: Int, val y: Int, val z: Int)
typealias Scanner = List<Point3D>

class Day19 : Day(19, "Beacon Scanner") {
    private fun Point3D.allRotations(rotation: Int): Point3D =
        when (rotation) {
            0 -> this
            1 -> Point3D(x, z, -y)
            2 -> Point3D(x, -y, -z)
            3 -> Point3D(x, -z, y)
            4 -> Point3D(-x, y, -z)
            5 -> Point3D(-x, -z, -y)
            6 -> Point3D(-x, -y, z)
            7 -> Point3D(-x, z, y)
            8 -> Point3D(y, -x, z)
            9 -> Point3D(y, z, x)
            10 -> Point3D(y, x, -z)
            11 -> Point3D(y, -z, -x)
            12 -> Point3D(-y, x, z)
            13 -> Point3D(-y, z, -x)
            14 -> Point3D(-y, -x, -z)
            15 -> Point3D(-y, -z, x)
            16 -> Point3D(z, y, -x)
            17 -> Point3D(z, -x, -y)
            18 -> Point3D(z, -y, x)
            19 -> Point3D(z, x, y)
            20 -> Point3D(-z, y, x)
            21 -> Point3D(-z, x, -y)
            22 -> Point3D(-z, -y, -x)
            23 -> Point3D(-z, -x, y)
            else -> this
        }

    private fun Point3D.manhattanDistance(other: Point3D): Int =
        abs(x - other.x) + abs(y - other.y) + abs(z - other.z)

    private fun List<Point3D>.allRotations(): List<List<Point3D>> =
        (0..23).map { rotation -> map { it.allRotations(rotation) } }

    private fun List<Point3D>.associate(): List<Pair<Point3D, Point3D>> =
        this.map { from -> this.map { to -> Pair(from, to) } }.flatten()

    private tailrec fun MutableList<Scanner>.normalizeOtherScanners(
        scannersToNormalize: MutableList<Scanner>,
        scannerLocations: MutableList<Point3D>
    ): MutableList<Scanner> {
        if (scannersToNormalize.isEmpty())
            return this

        this.forEach { normalized ->
            scannersToNormalize.forEachIndexed { toNormalizeIndex, toNormalize ->
                toNormalize.allRotations().forEach { toNormalizeRotation ->
                    normalized.findPointMakingOverlap(toNormalizeRotation)?.let { location ->
                        scannersToNormalize.removeAt(toNormalizeIndex)
                        this.add(toNormalizeRotation.normalizeAt(location))
                        scannerLocations.add(location)

                        return this.normalizeOtherScanners(scannersToNormalize, scannerLocations)
                    }
                }
            }
        }
        throw Exception("WTF?")
    }

    private fun Scanner.normalizeAt(offset: Point3D): List<Point3D> =
        this.map { Point3D(it.x + offset.x, it.y + offset.y, it.z + offset.z) }

    private fun Scanner.isOverlapping(other: Scanner, dx: Int, dy: Int, dz: Int): Boolean =
        this
            .map { Point3D(it.x + dx, it.y + dy, it.z + dz) }
            .intersect(other).size >= 12

    private fun Scanner.findPointMakingOverlap(other: Scanner): Point3D? {
        this.forEach { point ->
            other.forEach { otherPoint ->
                val dx = point.x - otherPoint.x
                val dy = point.y - otherPoint.y
                val dz = point.z - otherPoint.z

                if (other.isOverlapping(this, dx, dy, dz))
                    return Point3D(dx, dy, dz)
            }
        }
        return null
    }

    private fun String.to3DPoint(): Point3D = split(",").toInts().let { Point3D(it[0], it[1], it[2]) }

    private fun String.toScanners(): List<Scanner> =
        splitAtEmptyLine()
            .map { scanner -> scanner.splitLines().mapFrom(1) { it.to3DPoint() } }

    private fun `how many beacons are there`(input: String): Int =
        input.toScanners().let {
            mutableListOf(it[0])
                .normalizeOtherScanners(it.toMutableList(), mutableListOf())
                .flatten()
                .toSet()
                .size
        }

    private fun `what is the largest Manhattan distance between any two scanners`(input: String): Int =
        input.toScanners().let {
            val locations = mutableListOf<Point3D>()
            mutableListOf(it[0]).normalizeOtherScanners(it.toMutableList(), locations)

            return locations
                .associate()
                .map { points -> points.first.manhattanDistance(points.second) }
                .maxOf { d -> d }
        }

    @Test
    fun part1() =
        Assertions.assertEquals(
            320,
            computeStringResult { `how many beacons are there`(it) }
        )

    @Test
    fun part2() =
        Assertions.assertEquals(
            9655,
            computeStringResult { `what is the largest Manhattan distance between any two scanners`(it) }
        )
}

