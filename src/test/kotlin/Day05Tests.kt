import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class Day05Tests {
    private lateinit var lineSegments: List<LineSegment>

    @Before
    fun init( ) {
        val input = listOf(
        "0,9 -> 5,9",
        "8,0 -> 0,8",
        "9,4 -> 3,4",
        "2,2 -> 2,1",
        "7,0 -> 7,4",
        "6,4 -> 2,0",
        "0,9 -> 2,9",
        "3,4 -> 1,4",
        "0,0 -> 8,8",
        "5,5 -> 8,2"
        )

        lineSegments = parseInputDay05(input)
    }

    @Test
    fun testParser( ) {
        val thirdLineSegment = LineSegment(9,4,3,4)
        assertEquals(thirdLineSegment, lineSegments[2])
    }

    @Test
    fun testOverlaps1() {
        val field = fillField1(lineSegments)
        assertEquals(5, field.countOverlaps())
    }

    @Test
    fun testOverlaps2() {
        val field = fillField2(lineSegments)
        assertEquals(12, field.countOverlaps())
    }
}