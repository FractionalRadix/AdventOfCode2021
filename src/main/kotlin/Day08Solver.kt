import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Solve the Advent of Code puzzle for December 8, 2021.
 */
fun solveDay08() {
    val inputList = Path("""inputFiles\AoCDay08.txt""").readLines()
    val input = parseInputDay08(inputList)
    println("Nr of digits with a unique number of segments: ${countDigitsWithUniqueSegmentCount(input)}") // 272.
    println("Sum of the 4-element displays: ${summedSevenSegmentOutput(input)}") // 1007675.
}

fun parseInputDay08(inputList: List<String>): List<SignalLine> {
    return inputList
        .map { parseLine(it) }
}

/**
 * Parse a single line of signals for the 7-segment display.
 * @param line The input line: a number of strings separated by spaces, a vertical bar, and then 4 more strings separated by spaces.
 * @return The SignalLine representation of the input: strings before the vertical bar are input patterns, strings after the vertical bar are output patterns.
 */
fun parseLine(line: String): SignalLine {
    val splitted = line.split("|")
    val patterns = splitted[0].trim().split(' ')
    val output = splitted[1].trim().split(' ')
    return SignalLine(patterns, output)
}

/**
 * A single line of signals: the list of input patterns and the list of output patterns.
 */
class SignalLine(val inputPatterns: List<String>, val outputPatterns: List<String>) {
    fun print( ) {
        println()
        for (inputPattern in this.inputPatterns) {
            print("$inputPattern ")
        }
        print("|")
        for (outputPattern in outputPatterns) {
            print("$outputPattern ")
        }
    }
}

/**
 * In a 7-segment display, the numbers 1, 4, 7, and 8 have a unique number of segments.
 * The number 1 consists of 2 segments, the number 4 (appropriately) consists of 4 segments,
 * the number 7 consists of 3 segments, and the number 8 consists of 7 segments.
 * Given a list of strings where each letter represents an (unspecified!) segment on a 7-segment display,
 * determine the number of strings that represent these numbers.
 * @input A list of signals. Each signal consists of input patterns and output patterns; we only care about the latter.
 * @return The total number of times that a string representing 1, 4, 7, or 8 appears in the output patterns.
 */
fun countDigitsWithUniqueSegmentCount(input: List<SignalLine>): Int {
    var sum = 0
    for (signalLine in input) {
        val increase = signalLine.outputPatterns
            .map { it.length }
            .count { it == 2 || it == 3 || it == 4 || it == 7 }
        sum += increase
    }
    return sum
}

fun summedSevenSegmentOutput(inputList: List<SignalLine>): Int {
    var sum = 0
    for (signalLine in inputList) {
        val mapping = determineMapping(signalLine)

        var summedOutputValue = 0
        for (output in signalLine.outputPatterns) {

            val sortedOutput = sortString(output)
            val mappingResult = mapping[sortedOutput]
            if (mappingResult == null) {
                println("Oops, mappingResult is null?")
            } else if (mappingResult.size > 1) {
                println("Oops, mapping is ambiguous.")
            } else {
                val mappedValue = mappingResult.iterator().next()
                summedOutputValue = 10 * summedOutputValue + mappedValue
            }
        }
        sum += summedOutputValue
    }

    return sum
}

fun determineMapping(sl: SignalLine): Map<String, Set<Int>> {
    val mapping = mutableMapOf<String,Set<Int>>()

    // Create a mapping from strings to possible values.
    // Note that the order of the letters within a string isn't relevant, so we can sort these.

    for (pattern in sl.inputPatterns) {
        val sortedPattern = sortString(pattern)
        when (sortedPattern.length) {
            2 -> mapping[sortedPattern] = setOf(1)
            3 -> mapping[sortedPattern] = setOf(7)
            4 -> mapping[sortedPattern] = setOf(4)
            5 -> mapping[sortedPattern] = setOf(2,3,5)
            6 -> mapping[sortedPattern] = setOf(0,6,9)
            7 -> mapping[sortedPattern] = setOf(8)
        }
    }

    // The question is how to identify 2,3,5 , and 0,6,9.

    // 3 is the only 5-segment number that contains the same segments as 1.
    // So if you have a 5-segment number and a 2-segment number:
    //      if the 5-segment number contains both segments in the 2-segments, it must be a 3
    //      otherwise it must be a 2 or a 5.
    val patternForOne = sl.inputPatterns.firstOrNull { it.length == 2 }
    if (patternForOne != null) {
        val fiveSegmentNumbers = sl.inputPatterns.filter { it.length == 5 }
        separatePatternFor3from2and5(fiveSegmentNumbers, patternForOne, mapping)
    }

    // Similarly, the 0 and the 9 are 6-segment numbers that contain the same segments that are also in 1.
    // So if you know the segments for 1 and NOT bot segments are in a 6-segment number, that must be the 6.
    if (patternForOne != null) {
        val sixSegmentNumbers = sl.inputPatterns.filter { it.length == 6 }
        separatePatternFor6from0and9(sixSegmentNumbers, patternForOne, mapping)
    }

    // Separate 2 and 5: 2 and 4 have 2 segments in common. 5 and 4 have 3 elements in common.
    // 3 and 4 have 3 segments in common BTW, so check that you have already covered for the 3.
    val patternForFour = sl.inputPatterns.firstOrNull { it.length == 4 }
    if (patternForFour != null) {
        val fiveSegmentNumbers = sl.inputPatterns.filter { it.length == 5 }
        distinguishPatternsFor2and5(fiveSegmentNumbers, mapping, patternForFour)
    }

    // Separate 0 and 9: look at the pattern for 4 again. The 9 contains all 4 elements of the 4, the 0 contains only 3.
    if (patternForFour != null) {
        val sixSegmentNumbers = sl.inputPatterns.filter { it.length == 6 }
        distinguishPatternsFor0and9(sixSegmentNumbers, mapping, patternForFour)
    }

    return mapping
}

private fun separatePatternFor3from2and5(
    fiveSegmentNumbers: List<String>,
    patternForOne: String,
    mapping: MutableMap<String, Set<Int>>
) {
    for (pattern in fiveSegmentNumbers) {
        if (pattern.contains(patternForOne[0]) && pattern.contains(patternForOne[1])) {
            mapping[sortString(pattern)] = setOf(3)
        } else {
            mapping[sortString(pattern)] = setOf(2, 5)
        }
    }
}

private fun separatePatternFor6from0and9(
    sixSegmentNumbers: List<String>,
    patternForOne: String,
    mapping: MutableMap<String, Set<Int>>
) {
    for (pattern in sixSegmentNumbers) {
        if (pattern.contains(patternForOne[0]) && pattern.contains(patternForOne[1])) {
            mapping[sortString(pattern)] = setOf(0, 9)
        } else {
            mapping[sortString(pattern)] = setOf(6)
        }
    }
}

private fun distinguishPatternsFor2and5(
    fiveSegmentNumbers: List<String>,
    mapping: MutableMap<String, Set<Int>>,
    patternForFour: String
) {
    for (pattern in fiveSegmentNumbers) {
        val candidateSet = mapping[sortString(pattern)]
        if (candidateSet != null) {   // ...and it SHOULD be... but I'm not using !! or .? yet, want to allow for custom error handling.
            if (candidateSet.containsAll(setOf(2, 5))) {
                val nrOfSegmentsInCommon = countLettersInCommon(pattern, patternForFour)
                when (nrOfSegmentsInCommon) {
                    2 -> mapping[sortString(pattern)] = setOf(2)
                    3 ->mapping[sortString(pattern)] = setOf(5)
                    else -> println("Pattern $pattern was assumed to represent either 2 or 5, but seems to be neither.")
                }
            }
        }
    }
}

private fun distinguishPatternsFor0and9(
    sixSegmentNumbers: List<String>,
    mapping: MutableMap<String, Set<Int>>,
    patternForFour: String
) {
    for (pattern in sixSegmentNumbers) {
        val candidateSet = mapping[sortString(pattern)]
        if (candidateSet != null) {   // ...and it SHOULD be... but I'm not using !! or .? yet, want to allow for custom error handling.
            if (candidateSet.containsAll(setOf(0, 9))) {
                val nrOfSegmentsInCommon = countLettersInCommon(pattern, patternForFour)
                when (nrOfSegmentsInCommon) {
                    4 -> mapping[sortString(pattern)] = setOf(9)
                    3 -> mapping[sortString(pattern)] = setOf(0)
                    else -> println("Pattern $pattern was assumed to represent either 0 or 9, but seems to be neither.")
                }
            }
        }
    }
}


