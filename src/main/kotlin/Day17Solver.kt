import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.*

fun solveDay17() {
    val inputList = Path("""inputFiles\AoCDay17.txt""").readLines() //
    var (xStart, xEnd) = getXRange(inputList[0])
    var (yStart, yEnd) = getYRange(inputList[0])

    // First, the highest point of the probe is where its velocity reaches 0 - until that it goes up, after that it goes down.
    // Hence, given an initial velocity vy0, the highest point is vy0 + (vy0 - 1) + ... + 2 + 1 , or vy0*(vy0+1)/2 .
    // Let's call this value yMax:  yMax = vy0*(vy0+1)/2

    // A number of times in this puzzle, we will need to solve 1+2+...+(n-1)+n=p,
    // We know that 1+2+...+(n-1)+n=n*(n+1)/2.
    // Hence, we need to solve n*(n+1)/2=p.
    //   n*(n+1)/2=p <=> n*(n+1)=2*p <=> n*(n+1)-2*p=0 <=> n*n+n-2*p=0
    // Now we can use the abc formula, where a=1, b=1, c=-2p


    solveProblem1(xStart, xEnd, yStart, yEnd)


    // Second, we need to find those values for vy0, for which the y-coordinate of the probe will at least once be in the range [yStart..yEnd].
    // Starting from yMax, the height decreases. Again, the formula is 1 + 2 + .... + n
    // So the question is which value n has for (yMax - yStart) <= n*(n+1)/2 <= (yMax - yEnd)
    // So we need to solve n from p=n*(n+1)/2. Where p is known; for p we will fill in (yMax-yStart) and (yMax-yEnd).
    // First, p=yMax-yStart:
    //      n*(n+1)/2 = yMax-yStart <=> n*(n+1)/2 = vy0*(vy0+1)/2 - yStart <=> n*(n+1) = vy0*(vy0+1) - 2*yStart





}

fun solveProblem1(xStart: Int, xEnd: Int, yStart: Int, yEnd: Int): Int? {
    // x=20..30, y=-10..-5
    val (vxStart1,vxStart2) = abcFormula(1,1,-2 * abs(xStart))
    val (vxEnd1,vxEnd2) = abcFormula(1,1,-2 * abs(xEnd))

    println("$vxStart1 $vxStart2 $vxEnd1 $vxEnd2")
    // 5.84428877022476 -6.84428877022476 7.262087348130012 -8.262087348130013

    // At this point we have two ranges: vxStart1-vxEnd1 and vxStart2-vxEnd2.

    var horizontalSpeeds = mutableListOf<Int>()

    var vxStart1int = round(vxStart1!!).toInt()
    var vxEnd1int = round(vxEnd1!!).toInt()
    var vxStart2int = round(vxStart2!!).toInt()
    var vxEnd2int = round(vxEnd2!!).toInt()

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
    var (namedXRange, namedYRange)=ranges.split(',')
    val (_,xRange)=namedXRange.split('=')
    val (x0str, x1str)=xRange.split("..")
    return Pair(x0str.toInt(), x1str.toInt())
}

fun getYRange(inputLine: String): Pair<Int, Int> {
    //val str= "target area: x=20..30, y=-10..-5"
    val (_,ranges) = inputLine.split(":")
    var (namedXRange, namedYRange)=ranges.split(',')
    val (_,yRange)=namedYRange.split('=')
    val (y0str, y1str)=yRange.split("..")
    return Pair(y0str.toInt(), y1str.toInt())
}