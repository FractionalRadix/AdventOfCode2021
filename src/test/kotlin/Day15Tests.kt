import org.junit.Test
import java.awt.Point
import kotlin.test.assertEquals

class Day15Tests {
    private val inputLines = listOf(
        "1163751742",
        "1381373672",
        "2136511328",
        "3694931569",
        "7463417111",
        "1319128137",
        "1359912421",
        "3125421639",
        "1293138521",
        "2311944581",
    )

    @Test
    fun testDijkstraOnLargeMap() {
        val map = parseInputDay15(inputLines)
        val largeMap = enlargeMap(map)
        assertEquals(315, dijkstra(largeMap, Point(0,0)))
    }

    @Test
    fun testDijsktra() {
        val map = parseInputDay15(inputLines)
        assertEquals(40, dijkstra(map, Point(0,0)))
    }


    @Test
    fun testParseInputDay15() {
        val map = parseInputDay15(inputLines)
        assertEquals(1, map[Point(0,0)])
        assertEquals(3, map[Point(1,1)])
        assertEquals(7, map[Point(4,0)])
        assertEquals(3, map[Point(3,0)])
    }
}