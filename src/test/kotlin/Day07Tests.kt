import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.test.assertContentEquals

class Day07Tests {

    private lateinit var parsedInput: List<Int>

    @Before
    fun init() {
        val inputList = listOf<String>(
            "16,1,2,0,4,2,7,1,2,14"
        )
        parsedInput = parseInputDay07(inputList)
    }

    @Test
    fun testParse() {
        assertContentEquals(
            listOf(16,1,2,0,4,2,7,1,2,14),
            parsedInput
        )
    }

    @Test
    fun testDetermineFuelConsumption1() {
        Assert.assertEquals(37, determineFuelConsumption1(parsedInput))
    }

    @Test
    fun testDetermineFuelConsumption2() {
        Assert.assertEquals(168, determineFuelConsumption2(parsedInput))
    }
}