import kotlin.io.path.Path
import kotlin.io.path.readLines

fun solveDay06() {
    val inputList = Path("""inputFiles\AoCDay06.txt""").readLines()
    val parsedInput = parseInputDay06(inputList)

    val gen = afterMultipleDays(parsedInput, 80)
    println("Number of lanternfish after 80 days: ${gen.size}") // 391888

    val genUsingMap = afterMultipleDaysUsingMap(parsedInput, 256)
    println("Number of lanternfish after 256 days: $genUsingMap") // 1754597645339
}

fun parseInputDay06(input: List<String>): List<Int> {
    return input[0].split(',').map { it.toInt() }
}

fun list2Map(input: List<Int>): Map<Int,Long> {
    val map = mutableMapOf<Int,Long>()
    for (i in 0 .. 8) {
        map[i] = input.count { it == i }.toLong()
    }
    return map
}

fun calcNextGen(gen: Map<Int,Long>): Map<Int,Long> {
    val nextGen = mutableMapOf<Int,Long>()

    nextGen[8] = gen[0]!!   // Newly born lanternfish
    nextGen[7] = gen[8]!!
    nextGen[6] = gen[7]!! + gen[0]!! // All lanternfish that will give birth in 6 days.
    nextGen[5] = gen[6]!!
    nextGen[4] = gen[5]!!
    nextGen[3] = gen[4]!!
    nextGen[2] = gen[3]!!
    nextGen[1] = gen[2]!!
    nextGen[0] = gen[1]!!

    return nextGen
}

fun afterMultipleDaysUsingMap(parsedInput:List<Int>, nrOfDays: Int): Long {
    var nextGen = list2Map(parsedInput)
    for (i in 1 .. nrOfDays) {
        nextGen = calcNextGen(nextGen)
        //println("Day $i: size==${nextGen.values.sum()}")
    }
    return nextGen.values.sum()
}

fun afterMultipleDays(input: List<Int>, days: Int): List<Int> {
    var gen = input
    for (i in 1 .. days) {
        gen = newDay(gen)
        //println("Intermediate ($i days): ${gen.size}")
    }
    return gen
}

fun newDay(input: List<Int>): List<Int> {
    val nextGen = mutableListOf<Int>()
    var eights = 0

    for (num in input) {
        if (num == 0) {
            nextGen.add(6)
            eights++
        } else {
            nextGen.add(num - 1)
        }
    }

    for (i in 1 .. eights) {
        nextGen.add(8)
    }

    return nextGen
}