import kotlin.io.path.Path
import kotlin.io.path.readLines

fun solveDay03() {
    val inputList = Path("""inputFiles\AoCDay03.txt""").readLines()
    val ratePair = determineGammaAndEpsilonRate(inputList)
    println("Gamma rate times epsilon rate: ${ratePair.first * ratePair.second}")
    val oxygen = determineOxygenGeneratorRating(inputList)
    val co2 = determineCO2ScrubberRating(inputList)
    println("Oxygen generator rating times CO2 scrubber rating: ${oxygen * co2}")
}

fun determineGammaAndEpsilonRate(input: List<String>): Pair<Int,Int> {

    val bitsPerInput = input[0].length
    val counter = Array(bitsPerInput, {0})
    for (str in input) {
        for (i in 0 until bitsPerInput) {
            if (str[i] == '1') {
                counter[i]++
            }
        }
    }

    var multiplier = 1
    var gammaRate = 0
    for (i in bitsPerInput - 1 downTo 0) {
        val gamma = counter[i]
        val epsilon = input.size - counter[i]
        if (gamma > epsilon) {
            gammaRate += multiplier
        }
        multiplier *= 2
    }

    val mask = pow(2, bitsPerInput - 1) - 1
    val epsilonRate = gammaRate xor mask

    return Pair(gammaRate, epsilonRate)
}

fun determineOxygenGeneratorRating(input: List<String>) : Int {
    val bitsPerInput = input[0].length

    // Count the number of 1 in the first position.
    var idx = 0
    var filteredInput = input

    while (filteredInput.size > 1 && idx < bitsPerInput) {

        val twoLists = filteredInput.groupBy { it[idx] }
        filteredInput = if (twoLists['0']!!.size > twoLists['1']!!.size) {
            twoLists['0']!!
        } else {
            twoLists['1']!!
        }

        if (filteredInput.size == 1) {

            return Integer.parseInt(filteredInput[0], 2)
        }

        idx++
    }

    return -1
}

fun determineCO2ScrubberRating(input: List<String>) : Int {
    val bitsPerInput = input[0].length

    // Count the number of 1 in the first position.
    var idx = 0
    var filteredInput = input

    while (filteredInput.size > 1 && idx < bitsPerInput) {

        val twoLists = filteredInput.groupBy { it[idx] }
        filteredInput = if (twoLists['0']!!.size > twoLists['1']!!.size) {
            twoLists['1']!!
        } else {
            twoLists['0']!!
        }

        if (filteredInput.size == 1) {

            return Integer.parseInt(filteredInput[0], 2)
        }

        idx++
    }

    return -1
}