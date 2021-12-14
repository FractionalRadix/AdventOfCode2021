import org.junit.Assert
import org.junit.Test

class Day14Tests {
    private val inputLines = listOf<String>(
        "NNCB",
        "",
        "CH -> B",
        "HH -> N",
        "CB -> H",
        "NH -> C",
        "HB -> C",
        "HC -> B",
        "HN -> C",
        "NN -> C",
        "BH -> H",
        "NC -> B",
        "NB -> B",
        "BN -> B",
        "BB -> N",
        "BC -> B",
        "CC -> N",
        "CN -> C",
    )

    @Test
    fun testParseInput() {
        val (template, rules) = parseInputDay14(inputLines)
        Assert.assertEquals("NNCB", template)
        val rule1 = rules[0]
        Assert.assertEquals(Rule('C','H','B'), rule1)
    }

    @Test
    fun testApplyRulesOnce() {
        val (template, rules) = parseInputDay14(inputLines)
        val result = applyRules(template, rules)
        Assert.assertEquals("NCNBCHB", result)
    }

    @Test
    fun testApplyRulesFourTimes() {
        var (template, rules) = parseInputDay14(inputLines)
        for (i in 1 .. 4) {
            template = applyRules(template, rules)
        }
        Assert.assertEquals("NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB", template)
    }

    @Test
    fun testMaxMinusMin() {
        var (template, rules) = parseInputDay14(inputLines)
        for (i in 1 .. 10) {
            template = applyRules(template, rules)
        }
        Assert.assertEquals(1588, maxMinusMin(template))
    }
}