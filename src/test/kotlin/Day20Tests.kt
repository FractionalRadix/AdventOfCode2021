import org.junit.Test
import java.awt.Point
import kotlin.test.assertEquals

class Day20Tests {
    private val inputLines = listOf(
    "..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##"+
    "#..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###"+
    ".######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#."+
    ".#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#....."+
    ".#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.."+
    "...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#....."+
    "..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#",
    "",
    "#..#.",
    "#....",
    "##..#",
    "..#..",
    "..###",
    )

    @Test
    fun testParse() {
        val (enhancer, image) = parseInputDay20(inputLines)
        assertEquals(512, enhancer.length)
        assertEquals(true, image[Point(0,0)])
        assertEquals(false, image[Point(1, 3)])
    }

    @Test
    fun testSubGridValue() {
        val (_, image) = parseInputDay20(inputLines)
        assertEquals(34, subGridValue(image,2,2, false))
    }

    @Test
    fun testSubGridValue_emptyMap_nullsRepresentON() {
        val emptyMap = mapOf<Point,Boolean>()
        assertEquals(511, subGridValue(emptyMap, 0,0, true))
    }

    @Test
    fun testSubGridValue_emptyMap_nullsRepresentOFF() {
        val emptyMap = mapOf<Point,Boolean>()
        assertEquals(0, subGridValue(emptyMap, 0,0, false))
    }

    @Test
    fun testSolveDay20Part1() {
        assertEquals(35, solveDay20Part1(inputLines))
    }

    @Test
    fun testSolveDay20Part2() {
        assertEquals(3351, solveDay20Part2(inputLines))
    }
}