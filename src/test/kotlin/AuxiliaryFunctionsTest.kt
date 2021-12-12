import org.junit.Test
import java.awt.Point
import kotlin.test.assertEquals

class AuxiliaryFunctionsTest {
    @Test
    fun testPow() {
        assertEquals(128, pow(2,7))
        assertEquals(9, pow(3,2))
        assertEquals(0, pow(0,1000))
    }

    @Test
    fun testIsStringLowerCase() {
        assertEquals(true, "abc".isLowerCase())
        assertEquals(true, "ab2c".isLowerCase())
        assertEquals(false, "abDE".isLowerCase())
    }

    @Test
    fun testSortString() {
        assertEquals("bdduy", sortString("buddy"))
        assertEquals("dlloy", sortString("dolly"))
    }

    @Test
    fun testCountLettersInCommon() {
        assertEquals(3, countLettersInCommon("hello", "dolly"))
    }

    @Test
    fun testHorizontalAndVerticalNeighbours() {
        val testMap = mutableMapOf<Point, Int>()
        val intRange = 0..9
        intRange.forEach { x -> intRange.forEach { y -> testMap[Point(x, y)] = 42 } }
        val actual = getHorizontalAndVerticalNeighbours(testMap, Point(5,5))
        val expected = setOf(
            Point(4,5),
            Point(5,4),
            Point(5,6),
            Point(6,5)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun testHorizontalAndVerticalNeighbours_corner() {
        val testMap = mutableMapOf<Point, Int>()
        val intRange = 0..9
        intRange.forEach { x -> intRange.forEach { y -> testMap[Point(x, y)] = 42 } }
        val actual = getHorizontalAndVerticalNeighbours(testMap, Point(0,0))
        val expected = setOf(
            Point(0,1),
            Point(1,0)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun testHorizontalAndVerticalNeighbours_edge() {
        val testMap = mutableMapOf<Point, Int>()
        val intRange = 0..9
        intRange.forEach { x -> intRange.forEach { y -> testMap[Point(x, y)] = 42 } }
        val actual = getHorizontalAndVerticalNeighbours(testMap, Point(5,0))
        val expected = setOf(
            Point(4,0),
            Point(5,1),
            Point(6,0)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun testDiagonalNeighbours() {
        val testMap = mutableMapOf<Point, Int>()
        val intRange = 0..9
        intRange.forEach { x -> intRange.forEach { y -> testMap[Point(x, y)] = 42 } }
        val actual = getDiagonalNeighbours(testMap, Point(5,5))
        val expected = setOf(Point(4,4), Point(4,6), Point(6,4), Point(6,6))
        assertEquals(expected, actual)
    }

    @Test
    fun testDiagonalNeighbours_corner() {
        val testMap = mutableMapOf<Point, Int>()
        val intRange = 0..9
        intRange.forEach { x -> intRange.forEach { y -> testMap[Point(x, y)] = 42 } }
        val actual = getDiagonalNeighbours(testMap, Point(0,0))
        val expected = setOf(Point(1,1))
        assertEquals(expected, actual)
    }

    @Test
    fun testDiagonalNeighbours_edge() {
        val testMap = mutableMapOf<Point, Int>()
        val intRange = 0..9
        intRange.forEach { x -> intRange.forEach { y -> testMap[Point(x, y)] = 42 } }
        val actual = getDiagonalNeighbours(testMap, Point(5,0))
        val expected = setOf(Point(4,1), Point(6,1))
        assertEquals(expected, actual)
    }

    @Test
    fun testAllNeighbours() {
        val testMap = mutableMapOf<Point, Int>()
        val intRange = 0..9
        intRange.forEach { x -> intRange.forEach { y -> testMap[Point(x, y)] = 42 } }
        val actual = getAllNeighbours(testMap, Point(5,5))
        val expected = setOf(
            Point(4,4), Point(4,5), Point(4,6),
            Point(5,4), Point(5,6),
            Point(6,4), Point(6,5), Point(6,6),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun testAllNeighbours_corner() {
        val testMap = mutableMapOf<Point, Int>()
        val intRange = 0..9
        intRange.forEach { x -> intRange.forEach { y -> testMap[Point(x, y)] = 42 } }
        val actual = getAllNeighbours(testMap, Point(0,0))
        val expected = setOf(Point(0,1), Point(1,0), Point(1,1))
        assertEquals(expected, actual)
    }

    @Test
    fun testAllNeighbours_edge() {
        val testMap = mutableMapOf<Point, Int>()
        val intRange = 0..9
        intRange.forEach { x -> intRange.forEach { y -> testMap[Point(x, y)] = 42 } }
        val actual = getAllNeighbours(testMap, Point(5,0))
        val expected = setOf(
            Point(4,0), Point(4,1),
            Point(5,1),
            Point(6,0), Point(6,1),
        )
        assertEquals(expected, actual)
    }


}