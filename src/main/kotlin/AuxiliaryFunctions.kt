/**
 * Power function for integers.
 * (Since the Kotlin and Java library don't seem to have one...)
 */
fun pow(base: Int, exp: Int): Int {
    var res = 1
    for (i in 1 .. exp) {
        res *= base
    }
    return res
}

/**
 * Given a string, order the letters in it lexicographically.
 * For example, "hello" would yield "ehllo". "buddy" would become "bdduy".
 * @param str A string containing only characters that can be safely cast to Char.
 * @return The permutation of the input string, where the characters are in lexicographical order.
 */
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

/**
 * Given two strings, determine how many letters they have in common. Doubles count.
 * For example, "hello" and "dolly" have 'e', 'l', and 'l' in common - resulting in 3.
 * @param a A string whose contents is strictly characters that can be represented by Char.
 * @param b A string whose contents is strictly characters that can be represented by Char.
 * @return The number of letters that the strings have in common.
 */
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
