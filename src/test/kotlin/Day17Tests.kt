import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Day17Tests {
    private val input="target area: x=20..30, y=-10..-5"

    @Test
    fun testParseXRange() {
        val (x0,x1) = getXRange(input)
        assertEquals(20, x0)
        assertEquals(30, x1)
    }

    @Test
    fun testParseYRange() {
        val (y0,y1) = getYRange(input)
        assertEquals(-10, y0)
        assertEquals(-5, y1)
    }

    @Test
    fun testDetermineNrOfSteps() {
        assertEquals(6.0, determineNrOfSteps(21))
    }

    @Test
    fun testHighestPointForProbe() {
        val (y0,y1) = getYRange(input)
        assertEquals(45, highestPointForProbe(y0, y1))
    }

    @Test
    fun testAreaInside() {
        val area = Area(-2,-4,4,6)
        assertTrue(area.inside(-2, 4))
        assertTrue(area.inside(-4,6))
        assertTrue(area.inside(-3, 4))
        assertFalse(area.inside(-3, 7))
        assertFalse(area.inside(-5,5))

        val reverseArea = Area(-5,-1, 12, 8)
        assertTrue(reverseArea.inside(-5,12))
        assertTrue(reverseArea.inside(-1,8))
        assertTrue(reverseArea.inside(-3, 10))
        assertFalse(reverseArea.inside(-3, 20))
        assertFalse(reverseArea.inside(1, 8))
    }

    @Test
    fun testCountPossibleSpeeds() {
        val (x0,x1) = getXRange(input)
        val (y0,y1) = getYRange(input)
        assertEquals(112, countPossibleSpeeds(Area(x0, x1, y0, y1)))
    }

    @Test
    fun testPossibleHorizontalSpeeds() {
        val (x0,x1) = getXRange(input)
        val expected = listOf(6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30)
        val horizontalSpeedsWithSteps = initialHorizontalSpeedsWithSteps(x0, x1)
        val horizontalSpeeds = horizontalSpeedsWithSteps.map { it.first }.toSet()
        assertContentEquals(expected, horizontalSpeeds)
    }

}