import kotlin.io.path.Path
import kotlin.io.path.readLines

fun solveDay14() {
    val inputList = Path("""inputFiles\AoCDay14.txt""").readLines()
    val (template, rules) = parseInputDay14(inputList)

    var result1 = template
    for (i in 1 .. 10) {
        result1 = applyRules(result1, rules)
    }
    println("Count of most common element, minus count of least common element: ${maxMinusMin(result1)}")  // 3555

    val polymerData = PolymerData()
    polymerData.init(template)
    for (i in 1 .. 40) {
        polymerData.applyRuleset(rules)
    }


    // 1964728807506  - is too low.
    // 4439442043739 - is the right answer.

    println("Count of most common element, minus count of least common element, after 40 iterations: ${polymerData.maxMinusMin()}")
}

/**
 * For the rewriting rules of Advent of Code 14, 2021: a class that keeps track of the digrams and single letters in each application of the ruleset.
 */
class PolymerData {
    private var digramCounts = mapOf<String, Long>()
    private var letterCounts = mapOf<Char, Long>()

    fun init(template: String) {
        letterCounts = template
            .groupingBy { it }
            .eachCount()
            .castCharMapToLongValues()

        digramCounts = template
            .windowed(2)
            .groupingBy { it }
            .eachCount()
            .castStringMapToLongValues()
    }

    fun applyRuleset(rules: List<Rule>) {
        val digramCountDelta = mutableMapOf<String, Long>()
        val letterCountDelta = mutableMapOf<Char, Long>()

        // Apply the rule-set
        for (rule in rules) {
            // Lefthand side of the current production rule.
            val lhs = "${rule.char1}${rule.char2}"

            val digramCount = digramCounts[lhs] ?: 0

            // Righthand side of the current production rule.
            // (The rule "NN->C" is interpreted as... "NN->NC,CN").
            val rhs1 = "${rule.char1}${rule.char3}"
            val rhs2 = "${rule.char3}${rule.char2}"

            digramCountDelta[rhs1] = ( digramCountDelta[rhs1] ?: 0 ) + digramCount
            digramCountDelta[rhs2] = ( digramCountDelta[rhs2] ?: 0 ) + digramCount
            digramCountDelta[lhs]   = ( digramCountDelta[lhs  ] ?: 0 ) - digramCount

            letterCountDelta[rule.char3] = (letterCountDelta[rule.char3] ?: 0) + digramCount
        }

        // Apply the deltas.
        digramCounts = addStringCounts(digramCounts, digramCountDelta)
        letterCounts = addCharCounts(letterCounts, letterCountDelta)

        //digramCounts.printStringCounts()
        letterCounts.printCharCounts()
    }

    fun letterCount(ch: Char) = letterCounts[ch] ?: 0

    fun maxMinusMin(): Long {
        val max = letterCounts.maxByOrNull { it.value }!!.value
        val min = letterCounts.minByOrNull { it.value }!!.value

        letterCounts.printCharCounts()

        return (max - min)
    }
}

/**
 * Given a map of Strings to Ints, cast it to a map of Strings to Longs.
 * @return A copy of the input mapping, but the values are stored in Longs instead of Ints.
 */
fun Map<String,Int>.castStringMapToLongValues(): Map<String, Long> {
    val result = mutableMapOf<String, Long>()
    for (entry in this) {
        result[entry.key] = entry.value.toLong()
    }
    return result
}

fun Map<Char, Int>.castCharMapToLongValues(): Map<Char, Long> {
    val result = mutableMapOf<Char, Long>()
    for (entry in this) {
        result[entry.key] = entry.value.toLong()
    }
    return result
}

/**
 * Print a mapping from Strings to Longs.
 */
fun Map<String, Long>.printStringCounts() {
    this.forEach {
        println("${it.key} -> ${it.value}")
    }
}

/**
 * Print a mapping from Chars to Longs.
 */
fun Map<Char, Long>.printCharCounts() {
    this.forEach {
        println("${it.key} -> ${it.value}")
    }
}

fun maxMinusMin(input: String): Long {
    val letterCounts = input.toCharArray().toList().groupingBy { it }.eachCount()
    //val max = letterCounts.maxByOrNull { it.value }
    // val min = letterCounts.minByOrNull { it.value }
    val max: Long = letterCounts.maxOf { it.value }.toLong()
    val min: Long = letterCounts.minOf { it.value }.toLong()
    return max - min
}

class Rule(val char1: Char, val char2: Char, val char3: Char) {
    fun print() {
        println("$char1$char2 -> $char3")
    }

    override fun equals(other: Any?): Boolean {
        if (other?.javaClass != javaClass)
            return false
        other as Rule
        return (other.char1 == char1) && (other.char2 == char2) && (other.char3 == char3)
    }

    override fun hashCode(): Int {
        /* Generated code. The checker says that "Variable is never modified so it can be declared using 'val'", at position 29, and refuses to commit...
        var result = char1.hashCode()
        result = 31 * result + char2.hashCode()
        result = 31 * result + char3.hashCode()
        return result
         */
        val result1 = char1.hashCode()
        val result2 = 31 * result1 + char2.hashCode()
        val result3 = 31 * result2 + char3.hashCode()
        return result3
    }
}

/**
 * Given a mapping from Strings to Longs, and a list of rewriting rules for these Strings,
 * determine the <em>change</em> of occurrences of these strings, if these rewriting rule are
 * applied simultaneously to the same input.
 */
fun determineChangeInStringCounts(stringCounts: Map<String,Long>, rules: List<Rule>): MutableMap<String, Long> {
    val delta = mutableMapOf<String, Long>()

    for (rule in rules) {
        val input = "${rule.char1}${rule.char2}"

        val count = stringCounts[input] ?: 0

        val output1 = "${rule.char1}${rule.char3}"
        val output2 = "${rule.char3}${rule.char2}"

        delta[output1] = ( delta[output1] ?: 0 ) + count
        delta[output2] = ( delta[output2] ?: 0 ) + count
        delta[input]   = ( delta[input  ] ?: 0 ) - count
    }

    return delta
}

/**
 * Given two mappings that map Strings to counts, add these maps together.
 * For example, if the maps are { "A"->3, "B"->2 } and { "A"->4,"C"->5 }, the result would be { "A"->7, "B"->2, "C"->5 }.
 * We allow for the possibility that a count value is negative. This is because one or both of the maps might represent a delta, rather than a count.
 * @param map1 A mapping from Strings to Longs. The Long represents a number of occurrences of the String.
 * @param map2 A mapping from Strings to Longs. The Long represents a number of occurrences of the String.
 * @return A mapping from Strings to Longs. The Long represents the summed number of occurrences of the String over the input maps.
 */
fun addStringCounts(map1: Map<String, Long>, map2: Map<String, Long>): Map<String, Long> {
    val sum = mutableMapOf<String, Long>()
    val keys = map1.keys.union(map2.keys)
    for (key in keys) {
        sum[key] = (map1[key] ?: 0) + (map2[key] ?: 0)
    }
    return sum
}

fun addCharCounts(map1: Map<Char, Long>, map2: Map<Char, Long>): Map<Char, Long> {
    val sum = mutableMapOf<Char ,Long>()
    val keys = map1.keys.union(map2.keys)
    for (key in keys) {
        sum[key] = (map1[key] ?: 0) + (map2[key] ?: 0)
    }
    return sum
}

fun applyRules(template: String, rules: List<Rule>): String {
    // First, for every rule, check if it matches.
    // Find all occurrences of AB in the string.

    val splitString = template.windowed(2).toMutableList()
    //splitString.forEach { print(" ${it}")}
    //println()

    for (rule in rules) {
        val input = "${rule.char1}${rule.char2}"
        val output = "${rule.char1}${rule.char3}${rule.char2}"
        for (i in splitString.indices) {
            if (input == splitString[i]) {
                splitString[i] = output
            }
        }
    }

    var result = ""
    for (str in splitString) {
        result += str.dropLast(1)
    }
    result += splitString.last().last()

    return result
}

fun parseInputDay14(inputList: List<String>): Pair<String, List<Rule>> {
    val template = inputList[0]
    val rules = inputList.drop(2)
    val parsedRules = mutableListOf<Rule>()
    rules.forEach {
        val ch1 = it[0]
        val ch2 = it[1]
        val ch3 = it[6]
        val rule = Rule(ch1, ch2, ch3)
        //rule.print()
        parsedRules.add(rule)
    }
    return Pair(template, parsedRules)
}