import java.awt.Point
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun solveDay11() {
    val inputLines =  Path("""inputFiles\AoCDay11.txt""").readLines()
    val parsedInput = parseInputDay11(inputLines)

    println("Solution to puzzle 1: ${solvePuzzle1(parsedInput)}")
    println("Solution to puzzle 2: ${solvePuzzle2(parsedInput)}")
}

fun parseInputDay11(inputLines: List<String>): Any {
    return Object()
}

fun solvePuzzle1(arg: Any): Long {
    return 0
}

fun solvePuzzle2(arg: Any): Long {
    return 0
}

