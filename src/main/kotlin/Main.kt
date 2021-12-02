import java.lang.Integer.parseInt
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    solveDay02()
}

fun solveDay02() {
    val inputList = Path("""inputFiles\AoCDay02.txt""").readLines()
    val coordinates = determinePosition(inputList)
    println(coordinates.first * coordinates.second)
    val coordinatesWithAim = determinePositionWithAim(inputList)
    println(coordinatesWithAim.first * coordinatesWithAim.second)
}

/**
 * Determine the position of the submarine.
 * @input The list of commands that alter the submarine's position.
 * @return The pair (horizontal position, depth).
 */
fun determinePosition(input: List<String>): Pair<Int,Int> {
    var horizontalPosition = 0
    var depth = 0

    for (entry in input) {
        val pair = entry.split(' ')
        val command = pair[0]
        val quantity = parseInt(pair[1])
        when (command) {
            "up" -> depth -= quantity
            "down" -> depth += quantity
            "forward" -> horizontalPosition += quantity
        }
    }



    return Pair(horizontalPosition, depth)
}

/**
 * Determine the position of the submarine.
 * Alternative implementation using "groupBy".
 * @input The list of commands that alter the submarine's position.
 * @return The pair (horizontal position, depth).
 */
fun alternativeDeterminePosition(input: List<String>): Pair<Int, Int> {
    val maps = input
        // Split the string in a command and a value.
        .map { string -> string.split(' ')}
        .map { list -> Pair(list[0], parseInt(list[1])) }
        // Group by command.
        .groupBy { commandAndValue -> commandAndValue.first }
        // Transform from List<Key, List<Key,Value>> into List<Key,Value>>
        // Summing the elements of these lists, while we're at it.
        .map {
            Pair(it.key, it.value.sumOf { commandAndValue -> commandAndValue.second })
        }
        // Turn our list of Pair<String, Int> into a map.
        .toMap()
    // ...a map of <String,Int>, rather than a map of <String,List<Int>>

    val depth = maps["down"]!! - maps["up"]!!
    val horizontalPosition = maps["forward"]!!

    return Pair(horizontalPosition, depth)
}

fun determinePositionWithAim(input: List<String>): Pair<Int, Int> {
    var horizontalPosition = 0
    var depth = 0
    var aim = 0

    for (entry in input) {
        val pair = entry.split(' ')
        val command = pair[0]
        val quantity = parseInt(pair[1])
        when (command) {
            "up" -> aim -= quantity
            "down" -> aim += quantity
            "forward" -> {
                horizontalPosition += quantity
                depth += aim * quantity
            }
        }
    }

    return Pair(horizontalPosition, depth)
}

fun solveDay01() {
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