import org.junit.Assert
import org.junit.Test

class Tests {
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

    @Test
    fun testDeterminePosition() {
        val input = listOf("forward 5", "down 5", "forward 8", "up 3", "down 8", "forward 2")
        Assert.assertEquals(determinePosition(input), Pair(15,10))
    }

    @Test
    fun testDeterminePositionWithAim() {
        val input = listOf("forward 5", "down 5", "forward 8", "up 3", "down 8", "forward 2")
        Assert.assertEquals(determinePositionWithAim(input), Pair(15,60))
    }
}