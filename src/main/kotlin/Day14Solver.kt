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

    var result2 = template
    for (i in 1 .. 40) {
        result2 = applyRules(result2, rules)
    }
    println("Count of most common element, minus count of least common element: ${maxMinusMin(result2)}")

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

fun applyRules(template: String, rules: List<Rule>): String {
    // First, for every rule, check if it matches.
    // Find all occurrences of AB in the string.

    val splitString = template.windowed(2).toMutableList()
    splitString.forEach { print(" ${it}")}
    println()

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
        rule.print()
        parsedRules.add(rule)
    }
    return Pair(template, parsedRules)
}