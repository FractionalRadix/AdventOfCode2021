import kotlin.io.path.Path
import kotlin.io.path.readLines

fun solveDay08() {
    val inputList = Path("""inputFiles\AoCDay08.txt""").readLines()
    val input = parseInputDay08(inputList)
    println("Solution to puzzle 1: ${solvePuzzle1(input)}")
    println("Solution to puzzle 2: ${solvePuzzle2(input)}")
}

fun parseInputDay08(inputList: List<String>): List<SignalLine> {
    return inputList
        .map { parseLine(it) }

}

fun parseLine(line: String): SignalLine {
    val splitted = line.split("|")
    val patterns = splitted[0].split(' ')
    val output = splitted[1].split(' ')
    val result =  SignalLine(patterns, output)
    //result.print()
    return result
}

class SignalLine(val patterns: List<String>, val output: List<String>) {
    fun print( ) {
        println()
        for (p in this.patterns) {
            print("${p} ")
        }
        print("|")
        for (o in output) {
            print("${o} ")
        }
    }
}


fun solvePuzzle1(input: List<SignalLine>): Int {
    var sum = 0
    for (sl in input) {
        val increase = sl.output
            .map { it.length }
            .count { it == 2 || it == 3 || it == 4 || it == 7 }
        sum += increase
    }
    return sum
}

fun solvePuzzle2(inputList: List<SignalLine>): Int {
    var sum = 0
    for (sl in inputList) {
        val mapping = determineMapping(sl)

        var summedOutputValue = 0
        var digit = 1000
        for (output in sl.output) {
            print(output)

            val sortedOutput = sortString(output)
            val mappingResult = mapping[sortedOutput]
            if (mappingResult == null) {
                println("Oops, mappingResult is null?")
            } else if (mappingResult.size > 1) {
                println("Oops, mapping is not clear")
            } else {
                val mappedValue = mappingResult.iterator().next()
                println("($mappedValue)")
                summedOutputValue += (digit * mappedValue)
                digit /= 10
                print("<$digit>")
            }
        }
        println(summedOutputValue)
        sum += summedOutputValue
    }

    return sum
}

fun determineMapping(sl: SignalLine): Map<String, Set<Int>> {
    println()
    val mapping = mutableMapOf<String,Set<Int>>()

        // Create a mapping from strings to possible values.
        // Note that the order of the letters within a string isn't relevant, so we can sort these.

        for (pattern in sl.patterns) {
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
        val patternForOne = sl.patterns.firstOrNull { it.length == 2 }
        if (patternForOne != null) {
            val fiveSegmentNumbers = sl.patterns.filter { it.length == 5 }
            for (pattern in fiveSegmentNumbers) {
                if (pattern.contains(patternForOne[0]) && pattern.contains(patternForOne[1])) {
                    mapping[sortString(pattern)] = setOf(3)
                } else {
                    mapping[sortString(pattern)] = setOf(2,5)
                }
            }
        }

        // Similarly, the 0 and the 9 are 6-segment numbers that contain the same segments that are also in 1.
        // So if you know the segments for 1 and NOT bot segments are in a 6-segment number, that must be the 6.
        if (patternForOne != null) {
            val sixSegmentNumbers = sl.patterns.filter { it.length == 6 }
            for (pattern in sixSegmentNumbers) {
                if (pattern.contains(patternForOne[0]) && pattern.contains(patternForOne[1])) {
                    mapping[sortString(pattern)] = setOf(0, 9)
                } else {
                    mapping[sortString(pattern)] = setOf(6)
                }
            }
        }

        // Separate 2 and 5: 2 and 4 have 2 segments in common. 5 and 4 have 3 elements in common.
        // 3 and 4 have 3 segments in common BTW, so check that you have already covered for the 3.
        val patternForFour = sl.patterns.firstOrNull { it.length == 4 }
        if (patternForFour != null) {
            val fiveSegmentNumbers = sl.patterns.filter { it.length == 5 }
            for (pattern in fiveSegmentNumbers) {
                val nullableCandidateSet = mapping[sortString(pattern)]
                if (nullableCandidateSet != null) {   // ...and it SHOULD be... but I'm not using !! or .? yet, want to allow for custom error handling.
                    val candidates = nullableCandidateSet!!
                    if (candidates.containsAll(setOf(2,5))) {
                        val nrOfSegmentsInCommon = howManyLettersInCommon(pattern, patternForFour)
                        if (nrOfSegmentsInCommon == 2) {
                            mapping[sortString(pattern)] = setOf(2)
                        } else if (nrOfSegmentsInCommon == 3) {
                            mapping[sortString(pattern)] = setOf(5)
                        } else {
                            //TODO!+ Error message...
                        }
                    }

                }
            }
        }

        // Separate 0 and 9: look at the pattern for 4 again. The 9 contains all 4 elements of the 4, the 0 contains only  3
        if (patternForFour != null) {
            val sixSegmentNumbers = sl.patterns.filter { it.length == 6 }
            for (pattern in sixSegmentNumbers) {
                val nullableCandidateSet = mapping[sortString(pattern)]
                if (nullableCandidateSet != null) {   // ...and it SHOULD be... but I'm not using !! or .? yet, want to allow for custom error handling.
                    val candidates = nullableCandidateSet!!
                    if (candidates.containsAll(setOf(0, 9))) {
                        val nrOfSegmentsInCommon = howManyLettersInCommon(pattern, patternForFour)
                        if (nrOfSegmentsInCommon == 4) {
                            mapping[sortString(pattern)] = setOf(9)
                        } else if (nrOfSegmentsInCommon == 3){
                            mapping[sortString(pattern)] = setOf(0)
                        } else {
                            //TODO!+ Error message
                        }
                    }
                }
            }
        }

        for (pattern in sl.patterns) {
            print("[$pattern] ")
            printSet(mapping[sortString(pattern)])
        }

        println()

    return mapping
}

fun howManyLettersInCommon(a: String, b:String): Int {
    val charListA = mutableListOf<Char>()
    for (char in a) {
        charListA.add(char)
    }

    val charListB = mutableListOf<Char>()
    for (char in b) {
        charListB.add(char)
    }

    return charListA.count { charListB.contains(it) }
}

fun printSet(charSet: Set<Int>?) {
    if (charSet == null)
        print(" null ")
    else
        print( charSet.joinToString(prefix = "{", separator=",", postfix="}" ) )
}

fun sortString(str: String): String {
    val set = mutableSetOf<Char>()
    for (char in str) {
        set.add(char)
    }
    val list = set.sorted()
    var res = ""
    for (char in list) {
        res += char
    }
    return res
}

fun charSetToString(charSet: Set<Char>): String {
    var res = ""
    for (char in charSet.iterator()) {
        res += char
    }
    return res
}
fun solvePuzzle2_original(inputList: List<SignalLine>): Int {

    /*
    2 segmenten: 1
    3 segmenten: 7
    4 segmenten: 4
    5 segmenten: 2, 3, of 5
    6 segmenten: 0, 6, of 9
    7 segmenten: 8
    */

    println()
    var sum = 0
    for (sl in inputList) {
        val lettersToSegments = mutableMapOf(
            'a' to setOf('A', 'B', 'C', 'D', 'E', 'F', 'G'),
            'b' to setOf('A', 'B', 'C', 'D', 'E', 'F', 'G'),
            'c' to setOf('A', 'B', 'C', 'D', 'E', 'F', 'G'),
            'd' to setOf('A', 'B', 'C', 'D', 'E', 'F', 'G'),
            'e' to setOf('A', 'B', 'C', 'D', 'E', 'F', 'G'),
            'f' to setOf('A', 'B', 'C', 'D', 'E', 'F', 'G'),
            'g' to setOf('A', 'B', 'C', 'D', 'E', 'F', 'G'),
        )
        //TODO!+
        for (pat in sl.patterns) {
            if (pat.length == 2) {
                // The two elements in this string map to the segments C and F.
                lettersToSegments[pat[0]] = lettersToSegments[pat[0]]!!.intersect(setOf('C', 'F'))
                lettersToSegments[pat[1]] = lettersToSegments[pat[1]]!!.intersect(setOf('C', 'F'))
            }
            if (pat.length == 3) {
                // The three elements in this string map to the segments A, C, and F.
                lettersToSegments[pat[0]] = lettersToSegments[pat[0]]!!.intersect(setOf('A', 'C', 'F'))
                lettersToSegments[pat[1]] = lettersToSegments[pat[1]]!!.intersect(setOf('A', 'C', 'F'))
                lettersToSegments[pat[2]] = lettersToSegments[pat[2]]!!.intersect(setOf('A', 'C', 'F'))
            }
            if (pat.length == 4) {
                // The four elements in this string map to the segments B, C, D, and F.
                lettersToSegments[pat[0]] = lettersToSegments[pat[0]]!!.intersect(setOf('B', 'C', 'D', 'F'))
                lettersToSegments[pat[1]] = lettersToSegments[pat[1]]!!.intersect(setOf('B', 'C', 'D', 'F'))
                lettersToSegments[pat[2]] = lettersToSegments[pat[2]]!!.intersect(setOf('B', 'C', 'D', 'F'))
                lettersToSegments[pat[3]] = lettersToSegments[pat[3]]!!.intersect(setOf('B', 'C', 'D', 'F'))
            }
            // Note that if pat.length==7, ALL segments are lit, so no extra information is obtained.
            //TODO!+
        }

        ruleOfTwoAndThree(sl, lettersToSegments)

        println(lettersToSegments)

        for (pat in sl.patterns) {
            when (pat.length) {
                2 -> print("1 ")
                3 -> print("7 ")
                4 -> print("4 ")
                7 -> print("8 ")
                else -> print("$pat ")
            }

        }
    }
    return sum
}

//TODO?- This rule is superfluous! The only elements that don't have segment A, are the "1" and the "4". And we already got those.
/**
 * If the signal contains an element with two segments, it is a 1. Segments C and F light up.
 * If the signal contains an element with three segments, it is a 7. Segments A, C and F light up.
 * Therefore, if it contains both, then the letter that is in the three-segment element, but NOT in the two-segment element, maps to segment A.
 */
fun ruleOfTwoAndThree(sl: SignalLine, lettersToSegments: MutableMap<Char, Set<Char>>) {
    val mapping = lettersToSegments
    // Check if the patterns contain a "1" (segments C and F lighting up).
    val twoSegments = sl.patterns.firstOrNull { it.length == 2 }
    // Check if the patterns contain a "7" (segments A, C, and F lighting up).
    val threeSegments = sl.patterns.firstOrNull { it.length == 3 }
    // If yes, find the letter in which these two patterns differ. That letter maps to segment "A".
    if (twoSegments != null && threeSegments != null) {
        val threeSegmentsSet= mutableSetOf(threeSegments[0], threeSegments[1], threeSegments[2])
        threeSegmentsSet.remove(twoSegments[0])
        threeSegmentsSet.remove(twoSegments[1])
        val res = threeSegmentsSet.toList()[0]
        lettersToSegments[res] = setOf('A')
    }
}

/**
 * Check if the input pattern contains an element of only two letters.
 * These two letters are the segments that form the "1".
 * Every letter contains at least one of these two. The 0, 1, 3, 4, 7, 8, and 9 contain both.
 *
 */
fun ruleOfSymbolsWithoutAOne(sl: SignalLine, lettersToSegments: MutableMap<Char, Set<Char>>) {

}
