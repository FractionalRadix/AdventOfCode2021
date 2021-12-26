import kotlin.math.abs

data class Cuboid(val xRange: LongRange, val yRange: LongRange, val zRange: LongRange) {
    /**
     * Given another cuboid, determine where these two cuboids overlap - if anywhere.
     * Then cut the cuboid in pieces, by extending the lines that form the overlapping cuboid.
     * Note that this will produce at most 27 new cuboids.
     * If there is no overlap, simply return this cuboid and the input cuboid - although possibly represented differently.
     */
    fun split(other: Cuboid): Set<Pair<Cuboid, Boolean>> {
        val result = mutableSetOf<Pair<Cuboid, Boolean>>()

        val xRangeA = this.xRange.makeAscending()
        val yRangeA = this.yRange.makeAscending()
        val zRangeA = this.zRange.makeAscending()

        val xRangeB = other.xRange.makeAscending()
        val yRangeB = other.yRange.makeAscending()
        val zRangeB = other.zRange.makeAscending()

        val xOverlap = splitOnOverlap(xRangeA, xRangeB)
        val yOverlap = splitOnOverlap(yRangeA, yRangeB)
        val zOverlap = splitOnOverlap(zRangeA, zRangeB)

        for (xRange in xOverlap) {
            for (yRange in yOverlap) {
                for (zRange in zOverlap) {

                    val newCuboid = Cuboid(xRange.first, yRange.first, zRange.first)
                    val insideA = this.fullyContains(newCuboid)
                    val insideB = other.fullyContains(newCuboid)
                    if (insideA || insideB) {
                        val overlap = insideA && insideB
                        result.add(Pair(newCuboid, overlap))
                    }
                }
            }
        }

        return result
    }

    /**
     * Determine if this cuboid is fully contains another cuboid.
     * If it is on the border, that still counts as fully inside: so (3,3,5,5,8,8) is fully inside (3,3,10,10,20,20).
     * @param other A cuboid.
     * @return <code>true</code> if and only if the other cuboid is fully inside this cuboid.
     */
    fun fullyContains(other: Cuboid): Boolean {
        return (
                xRange.fullyContains(other.xRange) &&
                yRange.fullyContains(other.yRange) &&
                zRange.fullyContains(other.zRange)
        )
    }

    fun volume(): Long {
        val xLength = abs(xRange.last - xRange.first)
        val yLength = abs(yRange.last - yRange.first)
        val zLength = abs(zRange.last - zRange.first)
        return xLength * yLength * zLength
    }
}
