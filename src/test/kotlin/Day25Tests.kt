import org.junit.Test
import kotlin.test.assertEquals

class Day25Tests {
    private val inputLines1 = listOf(
        "...>>>>>..."
    )

    private val inputLines3 = listOf(
        "v...>>.vv>",
        ".vv>>.vv..",
        ">>.>v>...v",
        ">>v>>.>.v.",
        "v>v.vv.v..",
        ">.>>..v...",
        ".vv..>.>v.",
        "v.v..>>v.v",
        "....v..v.>",
    )

    @Test
    fun testStep1() {
        val state0 = parseInputDay25(inputLines1)
        val state1 = horizontalStep(state0)
        println(state1)
        val keysForLine0 = state1.keys.filter { it.y == 0 }
        var str = ""
        for (key in keysForLine0) {
            str += state1[key]
        }
        println(str)
        assertEquals("...>>>>.>..", str)
    }
    @Test
    fun testStep9x10() {
        println()
        val state0 = parseInputDay25(inputLines3)
        print(state0)
        val state1 = horizontalStep(state0)
        print(state1)
        val state2 = verticalStep(state1)
        print(state2)
        println()
    }

    @Test
    fun testCountUntilStable() {
        val state0 = parseInputDay25(inputLines3)
        val count = repeatUntilStable(state0)
        assertEquals(58, count)
    }
}