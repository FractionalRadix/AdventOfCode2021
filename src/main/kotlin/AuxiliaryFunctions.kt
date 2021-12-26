import kotlin.math.max
import kotlin.math.min

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
 * Given two ranges, split them in 2 or 3 separate ranges.
 * If the ranges have no overlap, the output equals the input.
 * If the ranges do have overlap, the output will be: first range minus overlap, overlap, second range minus overlap.
 * This is returned as a list with boolean flags: if the boolean is <code>true</code>, then that range was the overlap.
 * It is assumed that both ranges are in ascending order.
 * The result will be inclusive: if the input is [3..7] and [5..9], the result will be [3..5], [5..7], [7..9].
 * Empty ranges will not be included; if the input is [1..5] and [5..6], the output will be [1..5] and [5..6]. But also if the input is [1,1] and [1,2], the output will be [1,2].
 * @param r0 A range in ascending order.
 * @param r1 A range in ascending order.
 * @return A list of ranges with flags. The flag indicates if that range was the overlap.
 *  If there is no overlap, this will simply be the input ranges, both marked with <code>false</code>.
 *  If there was overlap, this will be the: range r0 minus the overlap (marked <code>false</code>), the overlap (marked <code>true</code>), and the range r1 minus the overlap (marked <code>false</code>).
 */
fun splitOnOverlap(r0: LongRange, r1: LongRange): Set<Pair<LongRange, Boolean>> {
    val overlapFirst = max(r0.first, r1.first)
    val overlapLast = min(r0.last, r1.last)

    var result = mutableSetOf<Pair<LongRange, Boolean>>()
    if (overlapFirst > overlapLast) {
        //TODO?~ If we remove 0-size ranges when there is overlap, shouldn't we do the same here?
        result = mutableSetOf(
            Pair(r0, false),
            Pair(r1, false)
        )
    } else {
        val rStart = min(r0.first, r1.first)
        val rLast = max(r0.last, r1.last)
        result = mutableSetOf(
            Pair(rStart..overlapFirst, false),
            Pair(overlapFirst..overlapLast, true),
            Pair(overlapLast..rLast, false)
        )
    }
    // Remove ranges of size 0, e.g. [3..3] or [-1..-1].
    result.removeIf { it.first.first == it.first.last }

    return result
}

/**
 * Given a range, return the ascending version of the same range.
 * For example, "7..4" will return "4..7".
 * When given an already ascending version of the range, simply return the input.
 * For example, "2..3" will return "2..3".
 * @return The ascending version of the range.
 */
fun LongRange.makeAscending(): LongRange {
    return if (this.first < this.last)
        this
    else
        this.last..this.first
}

/**
 * Given an IntRange, return the same range, but now using Long for the boundaries.
 * @return The input range, but represented using Long values instead of Int values.
 */
fun IntRange.toLongRange(): LongRange = LongRange(this.first.toLong(), this.last.toLong())

/**
 * Determine if this range fully contains another range.
 * This includes the case where the range is on the border, or if the ranges are equal.
 * So, (3,5) is fully inside (3,8); as are (3,8) and (5,8).
 * But (2,4) is NOT fully inside (3,8), the point 2 falls outside it.
 * @param other A range.
 * @return <code>true</code> if and only if the given range is fully within this range.
 */
fun LongRange.fullyContains(other: LongRange): Boolean {
    val myRange = this.makeAscending()
    val otherRange = other.makeAscending()
    return ( (otherRange.first >= myRange.first) && (otherRange.last <= myRange.last) )
}
