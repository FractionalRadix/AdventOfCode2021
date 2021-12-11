import org.junit.Before
import org.junit.Test
import java.awt.Point
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.fail

class Day11Tests {
    private lateinit var inputLines: List<String>
    private lateinit var parsedInput: MutableMap<Point, Int>

    @Before
    fun init() {
        inputLines = listOf(
            "5483143223",
            "2745854711",
            "5264556173",
            "6141336146",
            "6357385478",
            "4167524645",
            "2176841721",
            "6882881134",
            "4846848554",
            "5283751526",
        )
        parsedInput = parseInputDay11(inputLines)
    }

    @Test
    fun testParseInput() {
        parsedInput = parseInputDay11(inputLines)
        assertEquals(5, parsedInput[Point(0,0)])
        assertEquals(6, parsedInput[Point(6,3)])
        assertEquals(4, parsedInput[Point(2,1)])
    }

    @Test
    fun testNeighbours() {
        parsedInput = parseInputDay11(inputLines)
        val neighbours = getNeighbours(parsedInput, Point(5,5))
        assertEquals(8, neighbours.size)
        assertContains(neighbours, Point(4,4))
        assertContains(neighbours, Point(4,5))
        assertContains(neighbours, Point(4,6))
        assertContains(neighbours, Point(5,4))
        assertContains(neighbours, Point(5,6))
        assertContains(neighbours, Point(6,4))
        assertContains(neighbours, Point(6,5))
        assertContains(neighbours, Point(6,6))
    }

    @Test
    fun testCountFlashes() {
        assertEquals(1656, countFlashes(parsedInput))
    }

    @Test
    fun testFindSynchronizedFlash() {
        parsedInput = parseInputDay11(inputLines)
        assertEquals(195, firstSynchronizedFlash(parsedInput))
    }
}