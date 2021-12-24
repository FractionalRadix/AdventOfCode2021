import kotlin.io.path.Path
import kotlin.io.path.readLines

fun solveDay21() {
    val inputList = Path("""inputFiles\AoCDay21.txt""").readLines()
    val startingPositions = parseInputDay21(inputList)
    println("Result of the practice game of Dirac dice is ${practiceGame(startingPositions.first, startingPositions.second)}") // 713328.

    //println(possibleOutcomes())
    //diracDieGame(4,8)
}

fun parseInputDay21(inputLines: List<String>): Pair<Int, Int> {
    val pos1 = inputLines[0].dropWhile { it != ':' }.drop(1).trim().toInt()
    val pos2 = inputLines[1].dropWhile { it != ':' }.drop(1).trim().toInt()
    return Pair(pos1, pos2)
}

data class FieldAndScore(val field: Int, val Score: Int)

fun diracDieGame(player1Start: Int, player2Start: Int): Int {
    // Naive approach: keep track of how many universes contain a given combination (field, score).
    var player1 = mutableMapOf(Pair(FieldAndScore(player1Start, 0), 1L))
    val player2 = mutableMapOf(Pair(FieldAndScore(player2Start, 0), 1L))

    for (i in 1 .. 21) {
        println("New iteration: $i")
        val nextMap = mutableMapOf<FieldAndScore, Long>()

        for (fieldAndScore in player1.keys) {
            println("Field ${fieldAndScore.field}, Score ${fieldAndScore.Score}: ${player1[fieldAndScore]} universes.")

            updateMapEntry(fieldAndScore, nextMap,3 ,1)
            updateMapEntry(fieldAndScore, nextMap,4, 3)
            updateMapEntry(fieldAndScore, nextMap,5 ,6)
            updateMapEntry(fieldAndScore, nextMap,6 ,7)
            updateMapEntry(fieldAndScore, nextMap,7 ,6)
            updateMapEntry(fieldAndScore, nextMap,8, 3)
            updateMapEntry(fieldAndScore, nextMap,9 ,1)

            println(nextMap.size)
        }
        player1 = nextMap
    }



    return 0
}

private fun updateMapEntry(
    fieldAndScore: FieldAndScore,
    nextMap: MutableMap<FieldAndScore, Long>,
    roll: Int,
    nrOfUniverses: Int
) {
    val nextField = nextField(fieldAndScore.field, roll)
    val nextScore = fieldAndScore.Score + nextField
    val currentNrOfUniverses = nextMap[FieldAndScore(nextField, nextScore)] ?: 0
    nextMap[FieldAndScore(nextField, nextScore)] = currentNrOfUniverses + nrOfUniverses
    //println("nextMap[$nextField, $nextScore]==${nextMap[FieldAndScore(nextField, nextScore)]}")
}

fun practiceGame(player1Start: Int, player2Start: Int): Int {
    var score1 = 0
    var score2 = 0
    var field1 = player1Start
    var field2 = player2Start

    val die = deterministicDiracDie.iterator()
    var nrOfRolls = 0
    var losingScore: Int? = null

    do {
        val roll1 = die.next()
        nrOfRolls += 3
        field1 = nextField(field1, roll1)
        score1 += field1
        //println("Player 1: $roll1 $field1 $score1")
        if (score1 >= 1000) {
            losingScore = score2
            break
        }

        val roll2 = die.next()
        nrOfRolls += 3
        field2 = nextField(field2, roll2)
        score2 += field2
        //println("Player 2: $roll2 $field2 $score2")
        if (score2 >= 1000) {
            losingScore = score1
            break
        }

    } while (losingScore == null)

    println("Losing score: $losingScore")
    println("Nr of die rolls: $nrOfRolls")

    return nrOfRolls * losingScore!!
}

fun nextField(field: Int, roll: Int): Int {
    var result = (field + roll) % 10
    if (result == 0) {
        result = 10
    }
    return result
}


/**
 * Draw one of the values 1, 2, and 3. Do so three times. It is allowed to draw the same number multiple times.
 * What are the possible sums of these values, and how often do they occur?
 */
fun possibleOutcomes(): Map<Int, Int> {
    val outcomes = mutableMapOf<Int, Int>()
    for (i in 1 .. 3) {
        for (j in 1 .. 3) {
            for (k in 1 .. 3) {
                val sum = i+j+k
                if (outcomes[sum] == null) {
                    outcomes[sum] = 1
                } else {
                    outcomes[sum] = outcomes[sum]!! + 1
                }
            }
        }
    }
    return outcomes
}

// I'm sure there's an O(1) solution but this is a nice opportunity to try sequences and yield returns.
/**
 * Roll a deterministic "Dirac die".
 */
val deterministicDiracDie = sequence {
    var counter = 0

    while (true) {
        val sum = 3 * counter + 6
        yield(sum)
        counter += 3
    }
}