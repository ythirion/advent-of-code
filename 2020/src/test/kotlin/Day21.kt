import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day21 : Day(21) {
    private data class Dish(val ingredients: Set<String>, val allergens: Set<String>)

    private fun mapAllergensToIngredients(dishes: List<Dish>): MutableMap<String, MutableSet<String>> {
        val allergenIn = mutableMapOf<String, MutableSet<String>>()

        dishes.forEach { dish ->
            dish.allergens.forEach { allergen ->
                if (allergenIn.containsKey(allergen)) allergenIn[allergen]?.retainAll(dish.ingredients)
                else allergenIn[allergen] = dish.ingredients.toMutableSet()
            }
        }
        return allergenIn
    }

    private fun getIngredientsWithoutAllergens(dishes: List<Dish>): Set<String> {
        val allergenIn = mapAllergensToIngredients(dishes)
        return dishes.flatMap { it.ingredients }
                .toMutableSet()
                .removeAllElements(allergenIn.values.flatten())
    }

    private fun extractDishes(lines: List<String>): List<Dish> {
        val regex = "(.+) \\(contains (.+)\\)".toRegex()
        return lines.mapNotNull { line ->
            val matches = regex.findAll(line)
            Dish(matches.flatMap { m -> m.groupValues[1].splitWords() }.toSet(),
                    matches.flatMap { m -> m.groupValues[2].split(", ") }.toSet())
        }
    }

    private fun countDishesWithIngredientsWithoutAllergens(lines: List<String>): Int {
        val dishes = extractDishes(lines)
        val ingredientsWithoutAllergens = getIngredientsWithoutAllergens(dishes)

        return dishes.fold(0) { acc, dish ->
            acc + dish.ingredients.filter { ingredient -> ingredientsWithoutAllergens.contains(ingredient) }.size
        }
    }

    //Same than day16
    private fun findMapping(couldBe: MutableMap<String, MutableSet<String>>): Map<String, String> {
        val mapping = mutableMapOf<String, String>()

        while (couldBe.isNotEmpty()) {
            val solved = couldBe.entries.first { entry -> entry.value.count() == 1 }
            mapping[solved.key] = solved.value.single()
            couldBe.remove(solved.key)
            couldBe.forEach { entry -> entry.value.remove(solved.value.single()) }
        }
        return mapping
    }

    private fun getCanonicalDangerousIngredients(lines: List<String>): String {
        val dishes = extractDishes(lines)
        val allergenIn = mapAllergensToIngredients(dishes)
        val mapping = findMapping(allergenIn).toSortedMap()

        return mapping.values.joinToString(",")
    }

    @Test
    fun exercise1() = computeResult {
        Assertions.assertEquals(2517, countDishesWithIngredientsWithoutAllergens(it))
    }

    @Test
    fun exercise2() = computeResult {
        Assertions.assertEquals("rhvbn,mmcpg,kjf,fvk,lbmt,jgtb,hcbdb,zrb", getCanonicalDangerousIngredients(it))
    }
}