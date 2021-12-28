import kotlin.math.abs

data class Rectangle(val xRange: LongRange, val yRange: LongRange) {
    /**
     * Given another rectangle, determine where these two rectangles overlap - if anywhere.
     * Then cut the rectangle in pieces, by extending the lines that form the overlapping rectangle.
     * Note that this will produce at most 9 new rectangles.
     * If there is no overlap, simply return the original rectangles - although possibly represented differently.
     */
    fun split(other: Rectangle): Set<Pair<Rectangle, Boolean>> {
        val result = mutableSetOf<Pair<Rectangle, Boolean>>()

        val xRangeA = this.xRange.makeAscending()
        val yRangeA = this.yRange.makeAscending()

        val xRangeB = other.xRange.makeAscending()
        val yRangeB = other.yRange.makeAscending()

        val xOverlap = splitOnOverlap(xRangeA, xRangeB)
        val yOverlap = splitOnOverlap(yRangeA, yRangeB)

        for (xRange in xOverlap) {
            for (yRange in yOverlap) {

                val newRectangle = Rectangle(xRange.first, yRange.first)
                val insideA = this.fullyContains(newRectangle)
                val insideB = other.fullyContains(newRectangle)
                if (insideA || insideB) {
                    val overlap = insideA && insideB
                    result.add(Pair(newRectangle, overlap))
                }

            }
        }

        return result
    }

    /**
     * Determine if this rectangle is fully contains another rectangle.
     * If it is on the border, that still counts as fully inside: so the rectangle from point (3, 3) to (5, 5) is fully inside the rectangle from (3, 3) to (10, 10).
     * @param other A rectangle.
     * @return <code>true</code> if and only if the other rectangle is fully inside this rectangle.
     */
    fun fullyContains(other: Rectangle): Boolean {
        return (xRange.fullyContains(other.xRange) && yRange.fullyContains(other.yRange))
    }

    fun surface(): Long {
        val xL = abs(xRange.last - xRange.first)
        val yL = abs(yRange.last - yRange.first)
        return xL * yL
    }

    /**
     * Determine if a given point is inside the area.
     * @param x X-coordinate of the point.
     * @param y Y-coordinate of the point.
     * @return <code>true</code> if and only if (x,y) is inside the area. Being on the border counts as being inside.
     */
    fun inside(x: Long, y: Long): Boolean {
        val xInsideRange = x in xRange.makeAscending()
        val yInsideRange = y in yRange.makeAscending()
        return xInsideRange && yInsideRange
    }

    /**
     * Determine if a given point is inside the area.
     * @param x X-coordinate of the point.
     * @param y Y-coordinate of the point.
     * @return <code>true</code> if and only if (x,y) is inside the area. Being on the border counts as being inside.
     */
    fun inside(x: Int, y: Int): Boolean {
        val xInsideRange = x in xRange.makeAscending()
        val yInsideRange = y in yRange.makeAscending()
        return xInsideRange && yInsideRange
    }
}
