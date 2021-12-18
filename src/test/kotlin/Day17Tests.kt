import org.junit.Test
import kotlin.test.assertEquals

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
    fun testSolveProblem1() {
        val (x0,x1) = getXRange(input)
        val (y0,y1) = getYRange(input)
        solveProblem1(x0, x1, y0, y1)
    }
}