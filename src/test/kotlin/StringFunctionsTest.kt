import org.junit.Test
import kotlin.test.assertEquals

class StringFunctionsTest {
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
}