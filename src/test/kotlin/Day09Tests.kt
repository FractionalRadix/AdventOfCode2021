import org.junit.Assert
import org.junit.Before
import org.junit.Test

class Day09Tests {
    private lateinit var inputLines: List<String>

    @Before
    fun init() {
        inputLines = listOf(
            "2199943210",
            "3987894921",
            "9856789892",
            "8767896789",
            "9899965678",
        )
    }

    @Test
    fun testRiskLevel() {
        Assert.assertEquals(15, summedRiskLevel(inputLines))
    }

    @Test
    fun testMultipliedBasins() {
        Assert.assertEquals(1134, multipliedBasins(inputLines))
    }
}