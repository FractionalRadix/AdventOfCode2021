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
        val result = applyRewritingRules(template, rules)
        Assert.assertEquals("NCNBCHB", result)
    }

    @Test
    fun testApplyRulesFourTimes() {
        var (template, rules) = parseInputDay14(inputLines)
        for (i in 1 .. 4) {
            template = applyRewritingRules(template, rules)
        }
        Assert.assertEquals("NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB", template)
    }

    @Test
    fun testMaxMinusMin() {
        var (template, rules) = parseInputDay14(inputLines)
        for (i in 1 .. 10) {
            template = applyRewritingRules(template, rules)
        }
        Assert.assertEquals(1588, maxMinusMin(template))
    }

    @Test
    fun testApplyUsingPolymerDataClass_1iteration() {
        val (template, rules) = parseInputDay14(inputLines)
        val polymerData = PolymerData()
        polymerData.init(template)

        polymerData.applyRuleset(rules)

        // 1 iterations:
        //  NCNBCHB  N=2 C=2 B=2 H=1
        Assert.assertEquals(2, polymerData.letterCount('N'))
        Assert.assertEquals(2, polymerData.letterCount('B'))
        Assert.assertEquals(2, polymerData.letterCount('C'))
        Assert.assertEquals(1, polymerData.letterCount('H'))
    }

    @Test
    fun testApplyUsingPolymerDataClass_2iterations() {
        val (template, rules) = parseInputDay14(inputLines)
        val polymerData = PolymerData()
        polymerData.init(template)
        for (i in 1 .. 2) {
            polymerData.applyRuleset(rules)
        }

        // 2 iterations:
        // NBCCNBBBCBHCB N=2 B=6 H=1 C=4
        Assert.assertEquals(2, polymerData.letterCount('N'))
        Assert.assertEquals(6, polymerData.letterCount('B'))
        Assert.assertEquals(4, polymerData.letterCount('C'))
        Assert.assertEquals(1, polymerData.letterCount('H'))
    }

    @Test
    fun testApplyUsingPolymerDataClass_3iterations() {
        val (template, rules) = parseInputDay14(inputLines)
        val polymerData = PolymerData()
        polymerData.init(template)
        for (i in 1 .. 3) {
            polymerData.applyRuleset(rules)
        }

        // 3 iterations:
        //  NBBBCNCCNBBNBNBBCHBHHBCHB  N=5 B=11 C=5 H=4
        Assert.assertEquals(5, polymerData.letterCount('N'))
        Assert.assertEquals(11, polymerData.letterCount('B'))
        Assert.assertEquals(5, polymerData.letterCount('C'))
        Assert.assertEquals(4, polymerData.letterCount('H'))
    }

    @Test
    fun testApplyUsingPolymerDataClass_4iterations() {
        val (template, rules) = parseInputDay14(inputLines)
        val polymerData = PolymerData()
        polymerData.init(template)
        for (i in 1 .. 4) {
            polymerData.applyRuleset(rules)
        }


        // 4 iterations:
        // NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB N=11 B=25 H=5 C=10

        Assert.assertEquals(11, polymerData.letterCount('N'))
        Assert.assertEquals(23, polymerData.letterCount('B'))
        Assert.assertEquals(10, polymerData.letterCount('C'))
        Assert.assertEquals(5, polymerData.letterCount('H'))
    }

    @Test
    fun testApplyUsingPolymerDataClass_40iterations() {
        val (template, rules) = parseInputDay14(inputLines)
        val polymerData = PolymerData()
        polymerData.init(template)
        for (i in 1 .. 40) {
            polymerData.applyRuleset(rules)
        }

        val result = polymerData.maxMinusMin()
        Assert.assertEquals(2188189693529, result)
    }

}