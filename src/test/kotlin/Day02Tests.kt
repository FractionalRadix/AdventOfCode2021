import org.junit.Assert
import org.junit.Test

class Day02Tests {
    @Test
    fun testDeterminePosition() {
        val input = listOf("forward 5", "down 5", "forward 8", "up 3", "down 8", "forward 2")
        Assert.assertEquals(Pair(15,10), determinePosition(input))
    }

    @Test
    fun testDeterminePositionWithAim() {
        val input = listOf("forward 5", "down 5", "forward 8", "up 3", "down 8", "forward 2")
        Assert.assertEquals(Pair(15,60), determinePositionWithAim(input))
    }
}