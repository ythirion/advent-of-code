import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day4 : Day(4) {
    private fun prepareBatch(input: String) = input.splitAtEmptyLine()
    private val regexes = listOf(
            Regex(".*(byr:(19[23456789]\\d|20[0][12])).*"),
            Regex(".*(iyr:(20[1]\\d|2020)).*"),
            Regex(".*(eyr:(20[2]\\d|2030)).*"),
            Regex(".*(hgt:((1[56789]\\d|19[0123])cm|(59|6\\d|7[0123456])in)).*"),
            Regex(".*(hcl:#([\\d|a-f]{6})).*"),
            Regex(".*(ecl:(amb|blu|brn|gry|grn|hzl|oth)).*"),
            Regex(".*(pid:\\d{9}).*")
    )

    private fun simpleCount(batch: List<String>): Int = batch.count { it.containsAll("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid") }
    private fun enforcedRules(batch: List<String>): Int = batch.count { regexes.map { r -> it.contains(r) }.count { r -> r } == regexes.size }

    @Test
    fun exercise1() = Assertions.assertEquals(226, computeStringResult { simpleCount(prepareBatch(it)) })

    @Test
    fun exercise2() = Assertions.assertEquals(160, computeStringResult { enforcedRules(prepareBatch(it)) })
}