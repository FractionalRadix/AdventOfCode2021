import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.test.assertContentEquals

class Day06Tests {

    private lateinit var parsedInput: List<Int>

    @Before
    fun init() {
        val inputList = listOf("3,4,3,1,2")
        parsedInput = parseInputDay06(inputList)
    }

    @Test
    fun testParse() {
        assertContentEquals(
            listOf(3,4,3,1,2),
            parsedInput
        )
    }

    @Test
    fun testPuzzle1() {
        val afterDay1 = newDay(parsedInput)
        assertContentEquals(listOf(2,3,2,0,1), afterDay1)
        val afterDay2 = newDay(afterDay1)
        assertContentEquals(listOf(1,2,1,6,0,8), afterDay2)
    }

    @Test
    fun testPuzzle1MultipleDays() {
        Assert.assertEquals(5934, afterMultipleDays(parsedInput, 80).size)
    }

    @Test
    fun testPuzzle2() {
        Assert.assertEquals(26984457539, afterMultipleDaysUsingMap(parsedInput, 256))
    }
}