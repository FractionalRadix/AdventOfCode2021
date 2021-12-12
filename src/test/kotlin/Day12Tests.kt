import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class Day12Tests {
    private val inputLines1 = listOf(
        "start-A",
        "start-b",
        "A-c",
        "A-b",
        "b-d",
        "A-end",
        "b-end",
    )

    private val inputLines2 = listOf(
        "dc-end",
        "HN-start",
        "start-kj",
        "dc-start",
        "dc-HN",
        "LN-dc",
        "HN-end",
        "kj-sa",
        "kj-HN",
        "kj-dc",
    )

    @Test
    fun testParseInput() {
        val pairs = parseInputDay12(inputLines1)
        assertContains(pairs, Pair("start", "A"))
    }

    @Test
    fun testCountPathsWithSmallCavesAtMostOnce1() {
        val caves1 = parseInputDay12(inputLines1)
        assertEquals(10, countPathsWithSmallCavesAtMostOnce(caves1))
    }

    @Test
    fun testCountPathsWithSmallCavesAtMostOnce2() {
        val caves2 = parseInputDay12(inputLines2)
        assertEquals(19, countPathsWithSmallCavesAtMostOnce(caves2))
    }

    @Test
    fun testCountPathsWithSmallCavesAtMostTwice1() {
        val caves = parseInputDay12(inputLines1)
        assertEquals(36, countPathsWithSmallCavesAtMostTwice(caves))
    }

    @Test
    fun testCountPathsWithSmallCavesAtMostTwice2() {
        val caves = parseInputDay12(inputLines2)
        assertEquals(103, countPathsWithSmallCavesAtMostTwice(caves))
    }


    @Test
    fun testIsStringLowerCase() {
        assertEquals(true, "abc".isLowerCase())
        // assertEquals(true, "ab2c".isLowerCase())
        assertEquals(false, "abDE".isLowerCase())
    }

    @Test
    fun testFollowingCaves1() {
        val caves = parseInputDay12(inputLines1)
        val followSet1 = followingCaves(caves, Path(mutableListOf("start"))).toSet()
        assertEquals(setOf("A", "b"), followSet1)
        val followSet2 = followingCaves(caves, Path(mutableListOf("start", "A"))).toSet()
        assertEquals(setOf("c", "b", "end"), followSet2)
    }

    @Test
    fun testFollowingCaves2() {
        val caves = parseInputDay12(inputLines1)
        val testPath = Path(mutableListOf("start","A","c","A","b", "d"))
        val followSet = followingCaves(caves, testPath)
        assertEquals(0, followSet.size)
    }
}