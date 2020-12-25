import io.vavr.collection.Map
import io.vavr.collection.Seq
import io.vavr.kotlin.toVavrList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

typealias ColorCounts = kotlin.collections.Map<Color, Int>
typealias Color = String
typealias BagRules = Map<Color, ColorCounts>
typealias SearchResult = MutableSet<Color>

class Day7 : Day(7) {
    private val shinyGold = "shiny gold"
    private val extractColors = Regex("(\\d) (\\w+ \\w+)(?=\\s+bag)")
    private val noBagLine = Regex("(.*)( bags contain no other bags)")

    private fun extractBagRules(lines: Seq<String>): Map<Color, ColorCounts> =
            lines.filter { !noBagLine.containsMatchIn(it) }
                    .map {
                        val split = it.split(" bags contain ")
                        val matches = extractColors.findAll(split[1])
                        Pair(split[0], matches.map { m -> Pair(m.groupValues[2], m.groupValues[1].toInt()) })
                    }.toMap({ it.first }, { it.second.toMap() })

    private fun searchBagColorsContaining(color: Color = "shiny gold", bagRules: BagRules, result: SearchResult): SearchResult {
        val foundColors = bagRules.filter { kvp -> kvp._2.contains(color) }.keySet()
        foundColors.forEach { searchBagColorsContaining(it, bagRules, result) }

        return result + foundColors
    }

    private fun countBagsIn(color: Color, bagRules: BagRules): Int =
            bagRules.get(color).map {
                it.toList().fold(0, { count, bagIn -> count + bagIn.second + (countBagsIn(bagIn.first, bagRules) * bagIn.second) })
            }.sum()

    @Test
    fun exercise1() = Assertions.assertEquals(101, computeResult {
        searchBagColorsContaining(shinyGold, extractBagRules(it.toVavrList()), mutableSetOf()).size
    })

    @Test
    fun exercise2() = Assertions.assertEquals(108636, computeResult {
        countBagsIn(shinyGold, extractBagRules(it.toVavrList()))
    })
}