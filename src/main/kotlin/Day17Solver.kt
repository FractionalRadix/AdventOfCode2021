import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.*

fun solveDay17() {
    val inputList = Path("""inputFiles\AoCDay17.txt""").readLines()
    var (xStart, xEnd) = getXRange(inputList[0])
    var (yStart, yEnd) = getYRange(inputList[0])

    println("The highest point that the probe can reach is ${highestPointForProbe(yStart, yEnd)}") // 10296

}

/**
 * Describe a rectangular area.
 */
class Area(val xStart: Int, val xEnd: Int, val yStart: Int, val yEnd: Int) {
    /**
     * Determine if a given point is inside the area.
     * @param x X-coordinate of the point.
     * @param y Y-coordinate of the point.
     * @return <code>true</code> if and only if (x,y) is inside the area. Being on the border counts as being inside.
     */
    fun inside(x: Int, y: Int): Boolean {
        val xInsideRange = if (xStart <= xEnd) { x in xStart .. xEnd } else { x in xEnd .. xStart }
        val yInsideRange = if (yStart <= yEnd) { y in yStart .. yEnd } else { y in yEnd .. yStart }
        return xInsideRange && yInsideRange
    }
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
                println("Starting with velocity $verticalSpeed, falling from $maxHeight to $fallenTo.")
                speeds.add(Pair(verticalSpeed, maxHeight))
            }
        }
    }
    return speeds.maxOf { it.second }
}


fun countPossibleSpeeds(target: Area): Int {

    val speeds = mutableSetOf<Pair<Int, Int>>()

    // First, determine all possible horizontal speeds, along with the number of steps.
    val initialHorizontalSpeedsWithSteps = initialHorizontalSpeedsWithSteps(target.xStart, target.xEnd)

    val allowedNumberOfSteps = initialHorizontalSpeedsWithSteps.map { it.second }.toSet()
    val maxSteps = allowedNumberOfSteps.maxOrNull()!!

    // The lowest possible vertical speed is a negative: the yEnd value, which reaches the bottom of the trench in a single step.
    // For the highest possible vertical speed, observe that for any positive value, the probe will eventually visit *exactly* height 0 again!
    // For example, if the initial vertical speed is 5, the probe will move 5+4+3+2+1 steps to reach height 15... and then fall with 1+2+3+4+5.
    // The speed after this falling is -(initialSpeed+1).
    // So if the initial speed is the absolute value of yEnd minus 1, then the probe will fall from 0 to yEnd.
    // This means the highest vertical speed is the absolute value of the deepest point of the trench.. minus 1.

    val lowestInitialVerticalSpeed = min(target.yStart, target.yEnd)
    val highestInitialVerticalSpeed = abs(target.yEnd)-1

    for (initialVerticalSpeed in lowestInitialVerticalSpeed .. highestInitialVerticalSpeed) {
        for (initialHorizontalSpeed in initialHorizontalSpeedsWithSteps.map { it.first } ) {
            var xPos = 0
            var yPos = 0
            var xVelocity = initialHorizontalSpeed
            var yVelocity = initialVerticalSpeed
            // For the stop condition, we check if the probe has passed yEnd while falling.
            while (yPos >= target.yEnd || yVelocity >= 0) {
                xPos += xVelocity
                yPos += yVelocity
                if (target.inside(xPos, yPos)) {
                    speeds.add(Pair(xVelocity, yVelocity))
                    break;
                }
                xVelocity--
                yVelocity--
            }
        }


    }

    println()
    //initialVerticalSpeeds.forEach { print(" $it")}
    val printableSpeeds = speeds.chunked(9)

    printableSpeeds.forEach {
        println()
        it.forEach { point -> print( " (${point.first},${point.second})") }
    }

    return speeds.size
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

//TODO!~ Determine what to do about rounding..
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

fun solveProblem1(xStart: Int, xEnd: Int, yStart: Int, yEnd: Int): Int? {
    // x=20..30, y=-10..-5
    val (vxStart1,vxStart2) = abcFormula(1,1,-2 * abs(xStart))
    val (vxEnd1,vxEnd2) = abcFormula(1,1,-2 * abs(xEnd))

    println("$vxStart1 $vxStart2 $vxEnd1 $vxEnd2")
    // 5.84428877022476 -6.84428877022476 7.262087348130012 -8.262087348130013

    // At this point we have two ranges: vxStart1-vxEnd1 and vxStart2-vxEnd2.

    val horizontalSpeeds = mutableListOf<Int>()

    val vxStart1int = round(vxStart1!!).toInt()
    val vxEnd1int = round(vxEnd1!!).toInt()
    val vxStart2int = round(vxStart2!!).toInt()
    val vxEnd2int = round(vxEnd2!!).toInt()

    var lowestStartingSpeed = min(vxStart1int, vxEnd1int)
    var highestStartingSpeed = max(vxStart1int, vxEnd1int)
    for (vx in lowestStartingSpeed .. highestStartingSpeed) {
        println("Initial x speed: $vx")
        var distance = abs(vx)*(abs(vx)+1)/2
        if (vx<0) { distance = -distance }
        println("Horizontal distance travelled: $distance")
        val withinRange = distance in xStart .. xEnd
        println("Within range? $withinRange")
        if (withinRange) {
            horizontalSpeeds.add(vx)
        }
    }

    println()

    lowestStartingSpeed = min(vxStart2int, vxEnd2int)
    highestStartingSpeed = max(vxStart2int, vxEnd2int)
    for (vx in lowestStartingSpeed .. highestStartingSpeed) {
        println("Initial x speed: $vx")
        var distance = abs(vx)*(abs(vx)+1)/2
        if (vx<0) { distance = -distance }
        println("Horizontal distance travelled: $distance")
        val withinRange = distance in xStart .. xEnd
        println("Within range? $withinRange")
        if (withinRange) {
            horizontalSpeeds.add(vx)
        }
    }

    // At this point, "horizontalSpeeds" contains the possible values for the horizontal speeds.
    // Note that this is the same as the possible values for the TOTAL number of steps!
    //


    var highestPoint: Int? = null
    for (n in horizontalSpeeds) {
        // Assume the number of steps is n.
        // Then the probe goes up for m1 steps and down for m2 steps, where m1+m2=n
        for (m1 in 0..n) {
            val maxHeight = m1*(m1+1)/2
            val m2 = n - m1
            val fallingDistance = m2*(m2+1)/2
            val endPosition = maxHeight - fallingDistance
            if (endPosition in yStart .. yEnd) {
                println("nr of steps: $n max height: $maxHeight reached in $m1 steps. End position: $endPosition")
                highestPoint = if (highestPoint == null) {
                    maxHeight
                } else {
                    max(highestPoint, maxHeight)
                }
            }
        }
    }
    return highestPoint
}

fun abcFormula(a: Int, b: Int, c:Int): Pair<Double?,Double?> {
    val discriminant = b*b - 4*a*c
    if (discriminant < 0) {
        return Pair(null,null)
    } else {
        val x1 = (-b + sqrt(discriminant.toDouble())) / 2*a
        val x2 = (-b - sqrt(discriminant.toDouble())) / 2*a
        return Pair(x1,x2)
    }
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