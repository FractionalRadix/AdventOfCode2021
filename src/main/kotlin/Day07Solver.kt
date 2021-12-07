import kotlin.io.path.Path
import kotlin.io.path.readLines

fun solveDay07() {
    val inputList = Path("""inputFiles\AoCDay07.txt""").readLines()
    val input = parseInputDay07(inputList)
    println("Lowest fuel consumption: ${solvePuzzle1(input)}")
    println("Lowest fuel consumption, second method: ${solvePuzzle2(input)}")
}

fun parseInputDay07(input: List<String>): List<Int> {
    return input[0].split(',').map { it.toInt() }
}

fun solvePuzzle1(input: List<Int>): Int {
    // Naive solution.
    val min = input.minOrNull()!!
    val max = input.maxOrNull()!!

    val costs = mutableListOf<Int>()
    for (i in min .. max) {
        val cost = input.map { Math.abs(it - i) }.sum()
        costs.add(cost)
    }
    return costs.minOrNull()!!
}

fun solvePuzzle2(input: List<Int>): Int {
    // Naive solution.
    val min = input.minOrNull()!!
    val max = input.maxOrNull()!!

    val costs = mutableListOf<Int>()
    for (i in min .. max) {
        val cost = input.map { fuelCost2(it, i) }.sum()
        costs.add(cost)
    }
    return costs.minOrNull()!!
}

fun fuelCost2(a: Int, b:Int): Int {
    val diff = Math.abs(a-b)
    return diff * (diff + 1) / 2
}