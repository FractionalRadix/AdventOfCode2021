import org.junit.Assert
import org.junit.Test

class Day03Tests {
    @Test
    fun testGammaRate() {
        val input = listOf(
            "00100", "11110",
            "10110", "10111", "10101", "01111",
            "00111", "11100", "10000", "11001",
            "00010", "01010"
        )
        Assert.assertEquals(Pair(22, 9), determineGammaAndEpsilonRate(input))
    }

    @Test
    fun determineOxygenGeneratorRating() {
        val input = listOf(
            "00100", "11110",
            "10110", "10111", "10101", "01111",
            "00111", "11100", "10000", "11001",
            "00010", "01010"
        )
        Assert.assertEquals(23, determineOxygenGeneratorRating(input))
    }

    @Test
    fun determineCO2ScrubberRating() {
        val input = listOf(
            "00100", "11110",
            "10110", "10111", "10101", "01111",
            "00111", "11100", "10000", "11001",
            "00010", "01010"
        )
        Assert.assertEquals(10, determineCO2ScrubberRating(input))
    }
}