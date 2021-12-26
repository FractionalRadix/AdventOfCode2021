import org.junit.Test
import org.w3c.dom.css.Rect
import kotlin.test.assertContains
import kotlin.test.assertEquals

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
}