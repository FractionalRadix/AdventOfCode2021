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
}