import org.junit.Test
import kotlin.test.assertEquals

class RectangleTests {
    @Test
    fun testSurface() {
        assertEquals(8, Rectangle(1L..2L, 3L..6L).surface())
    }
}