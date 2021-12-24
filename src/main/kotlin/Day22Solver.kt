import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.max
import kotlin.math.min

fun solveDay22() {
    val inputList = Path("""inputFiles\AoCDay22.txt""").readLines()
    val instructions = parseInputDay22(inputList)
    println("The number of cubes that are on, in the -50/50 range, is ${cubesOnAfterInstructions1(instructions)}")  // 568000.
}

data class Instruction(val on: Boolean, val xRange: IntRange, val yRange: IntRange, val zRange: IntRange)

fun parseInputDay22(inputLines: List<String>): List<Instruction> {
    val instructions = mutableListOf<Instruction>()
    for (line in inputLines) {
        val (switch,ranges) = line.split(' ')
        val on = (switch == "on")
        var (xRange, yRange, zRange) = ranges.split(',')
        xRange = xRange.drop(2)
        yRange = yRange.drop(2)
        zRange = zRange.drop(2)
        val instruction = Instruction(on, parseRange(xRange), parseRange(yRange), parseRange(zRange))
        instructions.add(instruction)
    }
    return instructions
}

/**
 * Parse a range in the form <Int>..<Int>.
 * Always returns the range in ascending order.
 */
private fun parseRange(range: String): IntRange {
    val (str0, str1) = range.split("..")
    val num0 = str0.toInt()
    val num1 = str1.toInt()
    val result = if (num0 < num1) { IntRange(num0,num1) } else { IntRange(num1,num0) }
    return result
}

/**
 * Cut an IntRange to at most -50 .. 50
 * It is assumed that the input range is ascending.
 */
private fun cutRange(range: IntRange): IntRange {
    val rStart = max(range.start, -50)
    val rEnd = min(range.last, +50)
    return IntRange(rStart, rEnd)
}

// Naive solution.
fun cubesOnAfterInstructions1(instructions: List<Instruction>): Long {
    val cubes = mutableSetOf<Point3D>()
    for (instruction in instructions) {
        val xRange = cutRange(instruction.xRange)
        val yRange = cutRange(instruction.yRange)
        val zRange = cutRange(instruction.zRange)
        if (instruction.on) {
            // Add cubes
            for (x in xRange) {
                for (y in yRange) {
                    for (z in zRange) {
                        cubes.add(Point3D(x,y,z))
                    }
                }
            }

        } else { // instruction off
            // Remove cubes
            for (x in xRange) {
                for (y in yRange) {
                    for (z in zRange) {
                        cubes.remove(Point3D(x,y,z))
                    }
                }
            }
        }
    }
    return cubes.size.toLong()
}

/**
 * Given two ranges, split them in 2 or 3 separate ranges.
 * If the ranges have no overlap, the output equals the input.
 * If the ranges do have overlap, the output will be: first range minus overlap, overlap, second range minus overlap.
 * This is returned as a list with boolean flags: if the boolean is <code>true</code>, then that range was the overlap.
 * It is assumed that both ranges are in ascending order.
 * @param r0 A range.
 * @param r1 A range.
 * @return A list of ranges with flags. The flag indicates if that range was the overlap.
 *  If there is no overlap, this will simply be the input ranges, both marked with <code>false</code>.
 *  If there was overlap, this will be the: range r0 minus the overlap (marked <code>false</code>), the overlap (marked <code>true</code>), and the range r1 minus the overlap (marked <code>false</code>).
 */
fun splitOnOverlap(r0: LongRange, r1: LongRange): Set<Pair<LongRange, Boolean>> {
    val overlapFirst = max(r0.first, r1.first)
    val overlapLast = min(r0.last, r1.last)
    if (overlapFirst > overlapLast) {
        return setOf(
            Pair(r0, false),
            Pair(r1, false)
        )
    } else {
        val rStart = min(r0.first, r1.first)
        val rLast = max(r0.last, r1.last)
        return setOf(
            Pair(rStart .. overlapFirst - 1, false),
            Pair(overlapFirst .. overlapLast, true),
            Pair(overlapLast + 1 .. rLast, false)
        )
    }
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

fun IntRange.toLongRange(): LongRange= LongRange(this.first.toLong(), this.last.toLong())

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

/**
 * Determine the size of the overlap between two ranges.
 * Both ranges are in ascending order.
 */
fun overlapSize(r0: IntRange, r1: IntRange): Long {
    // Any overlap starts at the largest starting value, and ends at the smallest ending value.
    val rStart = max(r0.first, r1.first)
    val rLast = min(r0.last, r1.last)
    if (rLast < rStart)
        return 0L
    return (rLast - rStart + 1).toLong()
}

/**
 * Determine the size of the overlap of cuboids (x0,y0,z0) and (x1,y1,z1).
 */
fun overlapSize(x0: IntRange, y0: IntRange, z0: IntRange,
                x1: IntRange, y1: IntRange, z1: IntRange): Long {
    return overlapSize(x0,x1) * overlapSize(y0, y1) * overlapSize(z0,z1)
}

fun cubesOnAfterInstructions2(instructions: List<Instruction>): Long {
    var allLightedCuboids = mutableSetOf<Cuboid>()

    for (instruction in instructions) {

        val newCuboid = Cuboid(instruction.xRange.toLongRange(), instruction.yRange.toLongRange(), instruction.zRange.toLongRange())
        val nextSet = mutableSetOf<Cuboid>()

        // 1. Determine if this new cuboid overlaps with any existing cuboids.
        //    If not, and the instruction is to switch it on, add it.
        val intersectingCuboids = mutableSetOf<Cuboid>()
        for (oldCuboid in allLightedCuboids) {
            val intersections = newCuboid.split(oldCuboid).filter { cubeAndFlagPair -> cubeAndFlagPair.second }
            if (intersections.isEmpty()) {
                // Add the disjoint existing cuboid to the set.
                nextSet.add(oldCuboid)
            } else {
                // Keep track of which cuboids intersect. Don't add it yet.
                intersectingCuboids.add(oldCuboid)
            }
        }

        if (intersectingCuboids.isEmpty()) {
            if (instruction.on) {
                nextSet.add(newCuboid)
            }
        } else {

            //  Find all the cuboids that intersect with the new cuboid.
            //  Split them with the new cuboid.
            //  If the instruction is to switch ON, then add all these to "nextSet".
            //  If the instruction is to switch OFF, them add only those cuboids that are NOT in the intersection.
            println("New cuboid $newCuboid intersects with ${intersectingCuboids.size} existing cuboids:")
            intersectingCuboids.forEach { println("    $it")}

            intersectingCuboids.add(newCuboid)
            val splitCuboids = splitMultipleCuboids(intersectingCuboids)

            if (instruction.on) {
                nextSet.addAll( splitCuboids )
            } else {
                // Add only those cuboids, that are NOT in the new cuboid.
                val addUs = splitCuboids.filter { !newCuboid.fullyContains(it) }
                nextSet.addAll( addUs )
            }

        }

        allLightedCuboids = nextSet

        println("Intermediate result: ${allLightedCuboids.sumOf { it.volume() }}")
    }

    allLightedCuboids.forEach { println("  ${it.volume()}    $it")}

    return allLightedCuboids.sumOf { it.volume() }
}

/**
 * Given a set of cuboids, look for overlap.
 * Split them into separate cuboids that occupy the same space, but don't overlap.
 * @param cuboids A set of cuboids
 * @return A set of cuboids occupying the same space, but not overlapping.
 */
fun splitMultipleCuboids(cuboids: Set<Cuboid>): Set<Cuboid> {
    // If the set is empty, there is nothing to overlap.
    if (cuboids.isEmpty()) {
        return setOf()
    }
    // If the set is a singleton, there is no overlap; just a single cuboid.
    if (cuboids.size == 1) {
        val cuboid = cuboids.iterator().next()
        return setOf(cuboid)
    }

    // With the edge cases out of the way, we start the actual algorithm.

    var list = cuboids.toMutableList()
    var index = 0

    do {
        // Take the first two cuboids in our list.
        // Check if they have overlap, or if they are disjoint.
        val cuboidA = list[index]
        val cuboidB = list[index + 1]
        val split = cuboidA.split(cuboidB)
        val disjoint = split.none { it.second }

        if (disjoint) {
            // If our two cuboids are disjoint, we can just move on to the next round.
            index++
        } else {
            // If our two cuboids have overlap, we remove them from our list, and add their split cuboids instead.
            list.remove(cuboidA)
            list.remove(cuboidB)
            val newList = split.map { it.first }.toMutableList()
            newList.addAll(list)
            list = newList
            // We then go back to the start, to do it again with the new set of cuboids.
            index = 0
        }
    } while (index < list.size - 1)

    return list.toSet()
}