import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day23 : Day(23, "1 from 2019") {

    /*For a mass of 12, divide by 3 and round down to get 4, then subtract 2 to get 2.
For a mass of 14, dividing by 3 and rounding down still yields 4, so the fuel required is also 2.
For a mass of 1969, the fuel required is 654.
For a mass of 100756, the fuel required is 33583*/

    private fun Int.fuelRequirement(): Int = (this / 3) - 2
    private fun Int.fullFuelRequirement(): Int = this.fuelRequirement().also {
        return if (it <= 0) 0 else it + it.fullFuelRequirement()
    }

    @Test
    fun part1() =
        Assertions.assertEquals(
            3262356,
            computeResult({ it.map { line -> line.toInt() } },
                { mass -> mass.sumOf { it.fuelRequirement() } })
        )

    @Test
    fun part2() =
        Assertions.assertEquals(
            50346,
            computeResult({ it.map { line -> line.toInt() } },
                { mass -> mass.sumOf { it.fullFuelRequirement() } })
        )
}