import org.junit.Test
import java.awt.Point
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class Day13Tests {
    private val inputLines = listOf(
        "6,10",
        "0,14",
        "9,10",
        "0,3",
        "10,4",
        "4,11",
        "6,0",
        "6,12",
        "4,1",
        "0,13",
        "10,12",
        "3,4",
        "3,0",
        "8,4",
        "1,10",
        "2,14",
        "8,10",
        "9,0",
        "",
        "fold along y=7",
        "fold along x=5",
    )

    @Test
    fun testParse() {
        val (grid, folds) = parseInputDay13(inputLines)
        assertContains(grid, Point(10,4))
        assertContains(folds, Pair('x',5))
    }

    @Test
    fun testFoldOnce() {
        val (grid, _) = parseInputDay13(inputLines)
        val nextGrid = foldOnce(grid, 'y',7)
        assertEquals(17, countDots(nextGrid))
    }

    @Test
    fun testFoldMultiple() {
        val (grid, folds) = parseInputDay13(inputLines)
        val endGrid = foldAll(grid, folds)

        display(endGrid)

        assertContains(endGrid, Point(1,0))
        assertFalse(endGrid.contains(Point(1,1)))
    }
}