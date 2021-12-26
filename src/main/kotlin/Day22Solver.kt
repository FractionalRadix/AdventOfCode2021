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

fun cubesOnAfterInstructions2(instructions: List<Instruction>): Long {
    var allLightedCuboids = mutableSetOf<Cuboid>()

    for (instruction in instructions) {

        // Handle an off-by-1 problem.
        // Instructions tell you which cubes are lit; they are inclusive ranges. "x=5..8" means cubes at 5,6,7, and 8.
        // But mathematics tells you that's the cuboid starting at coordinate 5... and ending at coordinate 9.
        val xRange = instruction.xRange.first.toLong() .. instruction.xRange.last.toLong() + 1
        val yRange = instruction.yRange.first.toLong() .. instruction.yRange.last.toLong() + 1
        val zRange = instruction.zRange.first.toLong() .. instruction.zRange.last.toLong() + 1
        val newCuboid = Cuboid(xRange, yRange, zRange)

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