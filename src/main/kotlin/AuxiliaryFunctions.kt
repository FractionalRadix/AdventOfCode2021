/**
 * Power function for integers.
 * (Since the Kotlin and Java library don't seem to have one...)
 */
fun pow(base: Int, exp: Int): Int {
    var res = 1
    for (i in 0 .. exp) {
        res *= base
    }
    return res
}