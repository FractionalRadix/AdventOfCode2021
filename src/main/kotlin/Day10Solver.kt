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
    return inputList.sumOf { validity(it) }
}

fun middleScoreForIncompleteLines(inputList: List<String>): Long {
    val scores = inputList
        .filter { validity(it) == 0 }
        .map { completionScore(it) }
        .sorted()

    val middleIndex = (scores.size - 1) / 2
    return scores[middleIndex]
}

fun completionScore(line: String): Long {
    val scores = mapOf('(' to 1, '[' to 2, '{' to 3, '<' to 4)
    val openingCharacters = scores.keys

    val stack = mutableListOf<Char>()
    for (char in line) {
        if (char in openingCharacters) {
            stack.add(char)
        } else {
            // Illegal lines have already been filtered by the caller.
            stack.removeLast()
        }
    }

    return stack
        .map { scores[it] }
        .foldRight(0) { cur, acc -> (5 * acc) + cur!! }
}

fun validity(line: String) : Int {
    val stack = mutableListOf<Char>()
    for (char in line) {
        if (char == '{' || char == '[' || char == '<' || char == '(') {
            stack.add(char)
        } else {
            if (char == '}' && stack.last() != '{')
                return 1197
            if (char == ')' && stack.last() != '(')
                return 3
            if (char == ']' && stack.last() != '[')
                return 57
            if (char == '>' && stack.last() != '<')
                return 25137
            stack.removeLast()
        }
    }
    return 0
}