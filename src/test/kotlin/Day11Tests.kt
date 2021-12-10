import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class Day11Tests {
    private lateinit var inputLines: List<String>
    private lateinit var parsedInput: Any

    @Before
    fun init() {
        inputLines = listOf()
        parsedInput = parseInputDay11(inputLines)
    }

    @Test
    fun testParseInput() {
        parsedInput = parseInputDay11(inputLines)
        fail("Not yet implemented")
    }

    @Test
    fun testPuzzle1() {
        fail("Not yet implemented")
        assertEquals(0, solvePuzzle1(parsedInput))
    }

    @Test
    fun testPuzzle2() {
        fail("Not yet implemented")
        assertEquals(0, solvePuzzle2(parsedInput))
    }
}