import java.awt.Point

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
fun String.isLowerCase() : Boolean {
    return this
        .filter { ch -> ch.isLetter() }
        .all { ch -> ch.isLowerCase() }
}

/**
 * Given a mapping from (2D) points to T, find all points that are horizontally or vertically adjacent to the given point.
 * For example, if the mapping contains values for all elements in the range [0,0]-[10,10], then (5,5) would yield the points (4,5),(6,5),(5,4), and (5,6),
 * Similarly, the point (0,0) would only yield (0,1) and (1,0), as (-1,0) and (0,-1) are not in the grid.
 * Note that if a value is not in the mapping, it is not counted as a neighbour.
 * So in the above example, if <code>mapping[Point(4,5)]==null</code>, but all other points were defined, the result would be (6,5),(5,4), and (5,6).
 * The point at (4,5) would not be counted as a neighbour.
 * @param grid A mapping from points to values.
 * @param position A point on the grid.
 * @return All the direct horizontal and vertical neighbours of the given point, that do not map to <code>null</code>.
 */
fun <T> getHorizontalAndVerticalNeighbours(grid: MutableMap<Point, T>, position: Point): Set<Point> {
    val neighbours = mutableSetOf<Point>()
    val deltaList = listOf(Point(-1,0), Point(+1, 0), Point(0, -1), Point(0, +1))

    for (delta in deltaList) {
        val neighbour = Point(position.x + delta.x, position.y + delta.y)
        if (grid[neighbour] != null) {
            neighbours.add(neighbour)
        }
    }

    return neighbours
}

/**
 * Given a mapping from (2D) points to T, find all points that are diagonally adjacent to the given point.
 * For example, if the mapping contains values for all elements in the range [0,0]-[10,10], then (5,5) would yield the points (4,4), (4,6), (6,4), and (6,6).
 * Similarly, the point (0,0) would only yield (1,1), as (-1,-1), (-1,1), and (1,-1) are not in the grid.
 * Note that if a value is not in the mapping, it is not counted as a neighbour.
 * So in the above example, if <code>mapping[Point(4,6)]==null</code>, but all other points were defined, the result would be (4,4),(6,4), and (6,6).
 * The point at (4,6) would not be counted as a neighbour.
 * @param grid A mapping from points to values.
 * @param position A point on the grid.
 * @return All the direct diagonal neighbours of the given point, that do not map to <code>null</code>.
 */
fun <T> getDiagonalNeighbours(grid: MutableMap<Point, T>, position: Point): Set<Point> {
    val neighbours = mutableSetOf<Point>()
    for (dx in -1 .. +1) {
        for (dy in -1 .. +1) {
            if (dx != 0 && dy != 0) {
                val neighbour = Point(position.x + dx, position.y + dy)
                if (grid[neighbour] != null) {
                    neighbours.add(neighbour)
                }
            }
        }
    }
    return neighbours
}

/**
 * Given a mapping from (2D) points to T, find all points that are adjacent to the given point - horizontally, vertically, or diagonally.
 * For example, if the mapping contains values for all elements in the range [0,0]-[10,10], then (5,5) would yield the points (4,4), (4,5), (4,6),
 * (5,4), (5,6), (6,4), (6,5), and (6,6).
 * Similarly, the point (0,0) would only yield (0,1), (1,0), and (1,1); as (-1,-1), (-1,0), (1,-1), (-1,0) and (0,-1) are not in the grid.
 * Note that if a value is not in the mapping, it is not counted as a neighbour.
 * So in the above example, if <code>mapping[Point(4,5)]==null</code>, but all other points were defined, the result would be (4,4), (4,6),
 * (5,4), (5,6), (6,4), (6,5), and (6,6).
 * The point at (4,5) would not be counted as a neighbour.
 * @param grid A mapping from points to values.
 * @param position A point on the grid.
 * @return All the direct neighbours of the given point, that do not map to <code>null</code>.
 */
fun <T> getAllNeighbours(grid: MutableMap<Point, T>, position: Point): Set<Point> {
    val neighbours = mutableSetOf<Point>()
    for (dx in -1 .. +1) {
        for (dy in -1 .. +1) {
            if (dx != 0 || dy != 0) {
                val neighbour = Point(position.x + dx, position.y + dy)
                if (grid[neighbour] != null) {
                    neighbours.add(neighbour)
                }
            }
        }
    }
    return neighbours
}
