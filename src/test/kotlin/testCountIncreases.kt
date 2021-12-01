import org.junit.Assert
import org.junit.Test

class TestCountIncreases {
    @Test
    fun testCountIncreases() {
        val input = listOf(199, 200, 208, 210, 200, 207, 240, 269, 260, 263)
        Assert.assertEquals(7, countIncreases(input))
    }

    @Test
    fun testWindowedCountIncreases() {
        val input = listOf(199, 200, 208, 210, 200, 207, 240, 269, 260, 263)
        Assert.assertEquals(5, countWindowedIncreases(input, 3))
    }
}