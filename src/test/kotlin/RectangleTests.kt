import org.junit.Test
import org.w3c.dom.css.Rect
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RectangleTests {
    @Test
    fun testSurface() {
        assertEquals(6, Rectangle(1L..3L, 4L..7L).surface())
    }

    @Test
    fun testSplit() {
        val rect1 = Rectangle(2L..7L, 1L..4L)
        val rect2 = Rectangle(4L..8L, 2L..7L)
        val union = rect1.split(rect2)
        assertEquals(7, union.size)
        assertContains(union, Pair(Rectangle(2L..4L, 1L..2L), false))
        assertContains(union, Pair(Rectangle(4L..7L, 1L..2L), false))
        assertContains(union, Pair(Rectangle(2L..4L, 2L..4L), false))
        assertContains(union, Pair(Rectangle(4L..7L, 2L..4L), true))
        assertContains(union, Pair(Rectangle(7L..8L, 2L..4L), false))
        assertContains(union, Pair(Rectangle(4L..7L, 4L..7L), false))
        assertContains(union, Pair(Rectangle(7L..8L, 4L..7L), false))
    }

    @Test
    fun testFullyContains() {
        val rect1 = Rectangle(3L..10L, 3L..10L)
        val rect2 = Rectangle(3L..5L, 3L..5L)
        assertTrue(rect1.fullyContains(rect2))

        val rect3 = Rectangle( 4L .. 8L, 7L..12L)
        assertFalse(rect1.fullyContains(rect3))
    }

    @Test
    fun testInside_Ints() {
        val area = Rectangle(-2L ..-4L,4L ..6L)
        assertTrue(area.inside(-2, 4))
        assertTrue(area.inside(-4,6))
        assertTrue(area.inside(-3, 4))
        assertFalse(area.inside(-3, 7))
        assertFalse(area.inside(-5,5))

        val reverseArea = Rectangle(-5L .. -1L, 12L .. 8L)
        assertTrue(reverseArea.inside(-5,12))
        assertTrue(reverseArea.inside(-1,8))
        assertTrue(reverseArea.inside(-3, 10))
        assertFalse(reverseArea.inside(-3, 20))
        assertFalse(reverseArea.inside(1, 8))
    }

    @Test
    fun testInside_Longs() {
        val area = Rectangle(-2L ..-4L,4L ..6L)
        assertTrue(area.inside(-2L, 4L))
        assertTrue(area.inside(-4L,6L))
        assertTrue(area.inside(-3L, 4L))
        assertFalse(area.inside(-3L, 7L))
        assertFalse(area.inside(-5L,5L))

        val reverseArea = Rectangle(-5L .. -1L, 12L .. 8L)
        assertTrue(reverseArea.inside(-5L,12L))
        assertTrue(reverseArea.inside(-1L,8L))
        assertTrue(reverseArea.inside(-3L, 10L))
        assertFalse(reverseArea.inside(-3L, 20L))
        assertFalse(reverseArea.inside(1L, 8L))
    }

}