import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.awt.Point

class Day09Tests {
    private lateinit var depthMap: Map<Point, Int>

    @Before
    fun init() {
        val inputLines = listOf(
            "2199943210",
            "3987894921",
            "9856789892",
            "8767896789",
            "9899965678",
        )
        depthMap = parseDepthMap(inputLines)
    }

    @Test
    fun testRiskLevel() {
        Assert.assertEquals(15, summedRiskLevel(depthMap))
    }

    @Test
    fun testMultipliedBasinSizes() {
        Assert.assertEquals(1134, multipliedBasinSizes(depthMap))
    }
}