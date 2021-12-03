import kotlin.io.path.Path
import kotlin.io.path.readLines

fun solveDay01() {
    val inputAsStrings: List<String> = Path("""inputFiles\AoCDay01.txt""").readLines()
    val inputAsInts = inputAsStrings.map { Integer.parseInt(it, 10) }

    val answer01 = countIncreases(inputAsInts)
    println("Depth increases: $answer01")
    val answer02 = countWindowedIncreases(inputAsInts, 3)
    println("Windowed depth increases: $answer02")
}

fun countIncreases(ints: List<Int>): Int {
    return ints
        .zipWithNext()
        .count { it.second > it.first }
}

fun countWindowedIncreases(ints: List<Int>, windowSize: Int): Int {
    return ints
        .windowed(size = windowSize)
        .map { it[0] + it[1] + it[2] }
        .zipWithNext()
        .count { it.second > it.first }
}