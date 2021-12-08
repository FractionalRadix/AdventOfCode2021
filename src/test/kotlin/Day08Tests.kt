import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.test.assertContentEquals

class Day08Tests {
    private lateinit var parsedInput: List<SignalLine>

    @Before
    fun init() {
        val inputList=listOf(
                "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb |fdgacbe cefdb cefbgd gcbe",
                "edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec |fcgedb cgb dgebacf gc",
                "fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef |cg cg fdcagb cbg",
                "fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega |efabcd cedba gadfec cb",
                "aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga |gecf egdcabf bgf bfgea",
                "fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf |gebdcfa ecba ca fadegcb",
                "dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf |cefg dcbef fcge gbcadfe",
                "bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd |ed bcgafe cdgba cbgef",
                "egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg |gbdfcae bgc cg cgb",
                "gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc |fgae cfgab fg bagce")
        parsedInput = parseInputDay08(inputList)
    }

    @Test
    fun testParse() {
    //    assertContentEquals(
    //        listOf(),
    //        parsedInput
    //    )
    }

    @Test
    fun testPuzzle1() {
        Assert.assertEquals(26, solvePuzzle1(parsedInput))
    }

    @Test
    fun testPuzzle2() {
        Assert.assertEquals(61229, solvePuzzle2(parsedInput))
    }
}