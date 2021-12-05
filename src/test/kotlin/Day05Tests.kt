import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class Day05Tests {
    private lateinit var input: List<String>

    @Before
    fun init( ) {
        input = listOf(
        "0,9 -> 5,9",
        "8,0 -> 0,8",
        "9,4 -> 3,4",
        "2,2 -> 2,1",
        "7,0 -> 7,4",
        "6,4 -> 2,0",
        "0,9 -> 2,9",
        "3,4 -> 1,4",
        "0,0 -> 8,8",
        "5,5 -> 8,2")
    }


    @Test
    fun testParser( ) {
        val parsedInput = parseInput(input)
        val thirdLineSegment = LineSegment(9,4,3,4)
        assertEquals(thirdLineSegment, parsedInput[2])
    }

    @Test
    fun testOverlaps1() {
        val parsedInput = parseInput(input)
        val field = fillField1(parsedInput)

        field.print()

        assertEquals(5, field.countOverlaps())
    }

    @Test
    fun testOverlaps2() {
        val parsedInput = parseInput(input)
        val field = fillField2(parsedInput)

        field.print()

        assertEquals(12, field.countOverlaps())

    }
}