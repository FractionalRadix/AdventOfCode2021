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

    var result = ""
    for (ch in list) {
        val repetitions = str.count { it == ch }
        result += "$ch".repeat(repetitions)
    }

    return result
}

/**
 * Given two strings, determine how many letters they have in common. Doubles count.
 * For example, "hello" and "dolly" have 'e', 'l', and 'l' in common - resulting in 3.
 * @param a A string whose contents is strictly characters that can be represented by Char.
 * @param b A string whose contents is strictly characters that can be represented by Char.
 * @return The number of letters that the strings have in common.
 */
fun countLettersInCommon(a: String, b:String): Int {
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

/**
 * Determine if all letters in a String are lowercase.
 * For example, "hello" and "3 days" will both return <code>true</code>.
 * The second string contains some symbols that aren't letters, but not a single capital letter - so the string is in lowercase.
 * But "Hello" will return <code>false</code>, because it contains a capital letter.
 * @return <code>true<code> if and only if all letters in a given string are lowercase.
 */
fun String.isLowerCase(): Boolean {
    return this
        .filter { ch -> ch.isLetter() }
        .all { ch -> ch.isLowerCase() }
}