import java.awt.Point
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.*

fun solveDay17() {
    val inputList = Path("""inputFiles\AoCDay17.txt""").readLines()
    val (xStart, xEnd) = getXRange(inputList[0])
    val (yStart, yEnd) = getYRange(inputList[0])
    println("The highest point that the probe can reach is ${highestPointForProbe(yStart, yEnd)} .") // 10296.
    val nrOfPossibleSpeeds = countPossibleSpeeds(Rectangle(xStart .. xEnd.toLong(), yStart..yEnd.toLong()))
    println("The number of possible initial speeds that land let the probe cross the target area is $nrOfPossibleSpeeds .") // 2371.
}

fun highestPointForProbe(yStart: Int, yEnd: Int): Int {
    // First, let's try some brute force.
    val speeds = mutableSetOf<Pair<Int, Int>>()
    for (verticalSpeed in 1..1000) {
        val maxHeight = verticalSpeed * (verticalSpeed + 1) / 2
        val distanceToFall = maxHeight - min(yStart, yEnd)
        val steps = determineNrOfSteps(distanceToFall).toInt() + 1
        // If we fall from maxHeight, does any of these steps get us in the y range?
        for (i in 1..steps) {
            val fallenTo = maxHeight - i * (i + 1) / 2
            if (fallenTo in yStart..yEnd) {
                //println("Starting with velocity $verticalSpeed, falling from $maxHeight to $fallenTo.")
                speeds.add(Pair(verticalSpeed, maxHeight))
            }
        }
    }
    return speeds.maxOf { it.second }
}


fun countPossibleSpeeds(target: Rectangle): Int {

    val speeds = mutableSetOf<Pair<Int, Int>>()

    val xEnd = target.xRange.last.toInt()
    val yStart = target.yRange.first.toInt()
    val yEnd = target.yRange.last.toInt()

    // 1. The fastest horizontal speed is the one that reaches the end of the trench in a single step.
    //  Note that this value may be negative if the end of the trench is behind us.
    val fastestHorizontalSpeed = xEnd
    // 2. The slowest horizontal speed is the one that barely reaches the start of the trench.
    //  If it is n and xStart is positive, it is the solution to n*(n+1)=xStart.
    //  For simplicity, we set it to 0.
    val slowestHorizontalSpeed = 0
    // 3. The fastest negative vertical speed is the one that reaches the deep end of the trench in a single step.
    //  Note that the trench is always below is, yStart and yEnd are always negative.
    val fastestNegativeVerticalSpeed = min(yStart, yEnd)
    // 4. The fastest positive vertical speed is the one where, when the probe is falling, it reaches the deep end of the trench in one step after passing y=0.
    //  (The logic for this is outlined in part 1 of the solution).
    val fastestPositiveVerticalSpeed = abs(fastestNegativeVerticalSpeed)

    // Determine the range of possible horizontal speeds.
    val initialHorizontalSpeeds = if (fastestHorizontalSpeed > 0) {
        slowestHorizontalSpeed .. fastestHorizontalSpeed
    } else {
        fastestHorizontalSpeed downTo slowestHorizontalSpeed
    }

    // A brute-force solution, since it can be solved in O(n times m).
    // A smarter solution would involve determining the number of steps first, since there is an interaction between horizontal and vertical speed via the number of steps.
    for (initialHorizontalSpeed in initialHorizontalSpeeds) {
        for (initialVerticalSpeed in fastestNegativeVerticalSpeed .. fastestPositiveVerticalSpeed) {

            // Determine the fields that the probe crosses with these starting speeds.
            if (fieldsCrossed(target, initialHorizontalSpeed, initialVerticalSpeed).isNotEmpty()) {
                speeds.add(Pair(initialHorizontalSpeed, initialVerticalSpeed))
            }

        }
    }

    return (speeds.size)
}

fun fieldsCrossed(area: Rectangle, initialHorizontalSpeed: Int, initialVerticalSpeed: Int): Set<Point> {
    val positions = mutableSetOf<Point>()
    var horizontalSpeed = initialHorizontalSpeed
    var verticalSpeed = initialVerticalSpeed
    var probeX = 0
    var probeY = 0

    // Since the y coordinate is always negative, the deepest point is the minimum of the range.
    val deepestPoint = min(area.yRange.first, area.yRange.last)

    while (probeY >= deepestPoint) {
        probeX += horizontalSpeed
        probeY += verticalSpeed
        if (area.inside(probeX, probeY)) {
            positions.add(Point(probeX, probeY))
        }
        if (horizontalSpeed > 0) {
            horizontalSpeed--
        }
        verticalSpeed--
    }

    return positions
}

/**
 * Determine the possible initial horizontal speeds for the probe, as well as the number of steps needed to be in the target area using this speed.
 * Note that these are NOT the same numbers! You don't have to END in the target area, only to CROSS it.
 * @param xStart Start of the target area range.
 * @param xEnd End of the target area range.
 * @return All initial speed values for which the probe visits the target area, together with the number of steps needed to reach the area.
 *  This is a list of pairs (speed, steps).
 */
fun initialHorizontalSpeedsWithSteps(xStart: Int, xEnd: Int): List<Pair<Int,Int>> {
    val result = mutableListOf<Pair<Int,Int>>()

    // The target area can be in front of us (requiring positive speed values), or behind us (requiring negative speed values).
    // However, for the *amount* of possible speeds, it doesn't matter! We can simply use the absolute value.
    val x0 = if (xStart > 0) xStart else abs(xStart)
    val x1 = if (xEnd > 0) xEnd else abs(xEnd)

    // The highest possible speed equals xEnd: reaches the end of the ocean trench in 1 step.
    // Any speed higher than that is too high.
    for (initialSpeed in 1 .. xEnd) {
        var distanceTravelled = 0
        var steps = 0
        for (speed in initialSpeed downTo 0) {
            distanceTravelled += speed
            steps++
            if (distanceTravelled in x0 .. x1) {
                result.add(Pair(initialSpeed, steps))
            }
        }

    }

    return result
}

/**
 * Suppose you would add 1, 2, 3 ... n.
 * This would result in a sum, that is provably equal to n*(n+1).
 * Now suppose that you know this sum, but not the number of steps taken to reach it.
 * In other words, given a value "sum", you are asked to solve n in "n*(n+1)/2 = sum"
 * @param sum A non-negative integer, representing the sum of 1 ... n for some unknown n.
 * @return The number of consecutive integers you'd have to add, to reach the given sum.
 */
fun determineNrOfSteps(sum: Int): Double {
    // Observe that:
    // n*(n+1)/2 = sum   <=>   n*(n+1) = 2*sum   <=>   n*n + n - 2*sum = 0
    // Therefore you can just apply the abc formula:
    val result = abcFormula(1, 1, -2*sum)
    // If the value of "sum" was positive there will be two solutions, but only one of them a positive number (0 included).
    if (result.first != null && result.second != null) {
        return max(result.first!!, result.second!!)
    }
    return 0.0
}

fun getXRange(inputLine: String): Pair<Int, Int> {
    //val str= "target area: x=20..30, y=-10..-5"
    val (_,ranges) = inputLine.split(":")
    val (namedXRange, _)=ranges.split(',')
    val (_,xRange)=namedXRange.split('=')
    val (x0str, x1str)=xRange.split("..")
    return Pair(x0str.toInt(), x1str.toInt())
}

fun getYRange(inputLine: String): Pair<Int, Int> {
    //val str= "target area: x=20..30, y=-10..-5"
    val (_,ranges) = inputLine.split(":")
    val (_, namedYRange)=ranges.split(',')
    val (_,yRange)=namedYRange.split('=')
    val (y0str, y1str)=yRange.split("..")
    return Pair(y0str.toInt(), y1str.toInt())
}