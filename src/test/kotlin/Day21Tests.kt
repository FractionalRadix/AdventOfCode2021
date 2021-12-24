import kotlin.test.Test
import kotlin.test.assertEquals

class Day21Tests {
    private val inputLines = listOf(
        "Player 1 starting position: 4",
        "Player 2 starting position: 8",
    )

    @Test
    fun testParseInputDay21() {
        val positions = parseInputDay21(inputLines)
        assertEquals(positions.first, 4)
        assertEquals(positions.second, 8)
    }

    @Test
    fun testPracticeGameOfDiracDice() {
        val startPositions = parseInputDay21(inputLines)
        assertEquals(739785, practiceGame(startPositions.first, startPositions.second))
    }
}