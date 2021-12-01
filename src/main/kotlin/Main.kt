import java.lang.Integer.parseInt
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    val inputAsStrings: List<String> = Path("""inputFiles\AoCDay01.txt""").readLines()
    val inputAsInts = inputAsStrings.map { parseInt(it, 10) }

    val answer01 = countIncreases(inputAsInts)
    println(answer01)
    val answer02 = countWindowedIncreases(inputAsInts, 3)
    println(answer02)
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