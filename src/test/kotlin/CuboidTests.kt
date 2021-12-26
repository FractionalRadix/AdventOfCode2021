import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CuboidTests {
    @Test
    fun testVolume() {
        assertEquals(1, Cuboid(1L..2L, 1L..2L, 1L..2L).volume())
        assertEquals(6, Cuboid(-1L..2L, 5L .. 4L, 6L..8L).volume())
    }

    @Test
    fun testFullyContains() {
        assertTrue(Cuboid(3L .. 10L,3L .. 10L,3L .. 10L).fullyContains(Cuboid(3L..5L, 3L..5L, 3L..5L)))
        assertFalse(Cuboid(10L..10L, 11L..12L, 11L..12L).fullyContains(Cuboid(9L..10L, 11L..11L, 9L..10L)))
    }

    @Test
    fun testSplitOverlapCuboids_no_overlap() {
        // Test the result for two non-overlapping cuboids (edge case):
        val cuboid1 = Cuboid(1L..3L, 2L..5L,4L..6L)
        val cuboid2 = Cuboid( 13L..15L, 12L..18L, 9L..11L)
        val actual = cuboid1.split(cuboid2)

        val expected = setOf(Pair(cuboid1, false), Pair(cuboid2, false))
        assertEquals(2, actual.size)
        val cubes = actual.toList()
        assertContains(expected, cubes[0])
        assertContains(expected, cubes[1])
    }

    /**
     * Test that two cuboid that touch on one side, are not counted as overlapping.
     */
    @Test
    fun testSplitOverlapTouchingCuboids() {
        val cuboidA = Cuboid(xRange=10L..11L, yRange=10L..11L, zRange=10L..11L)
        val cuboidB = Cuboid(xRange=10L..11L, yRange=10L..11L, zRange=11L..12L)
        val split = cuboidA.split(cuboidB)
        assertEquals(2, split.size)
        assertContains(split, Pair(cuboidA, false))
        assertContains(split, Pair(cuboidB, false))
    }

    @Test
    fun testSplitOverlapCuboids_fully_contained() {
        val cuboid1 = Cuboid(1L..9L, 2L..8L, 3L..7L)
        val cuboid2 = Cuboid(4L..6L, 4L..6L, 4L..6L)
        val actual = cuboid1.split(cuboid2)

        assertEquals(27, actual.size)
        //TODO?+ assertContains for all 27 sub-cuboids...
    }


}