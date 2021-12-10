import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class Day10Tests {
    private lateinit var inputLines: List<String>

    @Before
    fun init() {
        inputLines = listOf(
            "[({(<(())[]>[[{[]{<()<>>",
            "[(()[<>])]({[<{<<[]>>(",
            "{([(<{}[<>[]}>{[]{[(<()>",
            "(((({<>}<{<{<>}{[]{[]{}",
            "[[<[([]))<([[{}[[()]]]",
            "[{[{({}]{}}([{[{{{}}([]",
            "{<[[]]>}<{[{[{[]{()[[[]",
            "[<(<(<(<{}))><([]([]()",
            "<{([([[(<>()){}]>(<<{{",
            "<{([{{}}[<[[[<>{}]]]>[]]",
        )
    }

    @Test
    fun testScoreForIllegalLines() {
        assertEquals(26397, scoreForIllegalLines(inputLines))
    }

    @Test
    fun testCompletionScore() {
        assertEquals(294, completionScore("<{([{{}}[<[[[<>{}]]]>[]]"))
    }

    @Test
    fun testSolvePuzzle2() {
        assertEquals(288957, middleScoreForIncompleteLines(inputLines))
    }
}