import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Solve the Advent of Code puzzles for December 10, 2021.
 */
fun solveDay10() {
    val inputList = Path("""inputFiles\AoCDay10.txt""").readLines()

    println("Score for lines with syntax errors: ${scoreForIllegalLines(inputList)}") // 339477.
    println("Middle score for incomplete lines: ${middleScoreForIncompleteLines(inputList)}") // 3049320156.
}


fun scoreForIllegalLines(inputList: List<String>): Int {
    return inputList.map { validity(it) }.sum()
}

fun middleScoreForIncompleteLines(inputList: List<String>): Long {
    val scores = inputList
        .filter { validity(it) == 0 }
        .map { completionScore(it) }
        .sorted()

    //scores.map { println(it) }

    val middleIndex = (scores.size - 1) / 2
    return scores[middleIndex]
}

fun completionScore(line: String): Long {
    val stack = mutableListOf<Char>()
    for (i in 0 until line.length) {
        val ch = line[i]
        if (ch == '{' || ch == '[' || ch == '<' || ch == '(') {
            stack.add(ch)
        } else {
            // Illegal lines have already been filtered by the caller.
            stack.removeLast()
        }
    }

    var totalScore: Long = 0
    for (i in stack.size - 1 downTo 0) {
        totalScore *= 5
        when (stack[i]) {
            '(' -> totalScore += 1
            '[' -> totalScore += 2
            '{' -> totalScore += 3
            '<' -> totalScore += 4
        }
    }

    return totalScore
}

fun validity(line: String) : Int {
    val stack = mutableListOf<Char>()
    for (i in 0 until line.length) {
        val ch = line[i]
        if (ch == '{' || ch == '[' || ch == '<' || ch == '(') {
            stack.add(ch)
        } else {
            if (ch == '}' && stack.last() != '{')
                return 1197
            if (ch == ')' && stack.last() != '(')
                return 3
            if (ch == ']' && stack.last() != '[')
                return 57
            if (ch == '>' && stack.last() != '<')
                return 25137
            stack.removeLast()
        }
    }
    return 0
}