import kotlin.io.path.Path
import kotlin.io.path.readLines

fun solveDay09() {
    val inputList = Path("""inputFiles\AoCDay09.txt""").readLines()

    println("The summed risk level is ${summedRiskLevel(inputList)}")   // 1581 is too high. It's 541.
    //TODO!+
    multipliedBasins(inputList)
}


fun multipliedBasins(inputLines: List<String>): Long {
    // First, find the low points again..
    val depthMap = parseDepthMap(inputLines)
    val lowPoints = findLowPoints(depthMap)

    val basinSizes = mutableListOf<Int>()

    lowPoints.forEach { println("(${it.first},${it.second})==${depthMap[Pair(it.first,it.second)]}")}

    //TODO!+ Now that we have the low points, find the basins surrounding them.
    // For every point, find the surrounding points - if they're higher but NOT 9, they are part of the basin.
    // Then do the same for all points around those...
    for (lowPoint in lowPoints) {
        val basin = determineBasin(depthMap, setOf(Pair(lowPoint.first, lowPoint.second)))
        println("Basin for (${lowPoint.first}, ${lowPoint.second}):")
        for (point in basin) {
            print("(${point.first},${point.second}) ")
        }
        basinSizes.add(basin.size)
    }

    val m1 = basinSizes.maxOrNull()
    basinSizes.remove(m1)
    val m2 = basinSizes.maxOrNull()
    basinSizes.remove(m2)
    val m3 = basinSizes.maxOrNull()

    val biggestThree = m1!!.toLong() * m2!!.toLong() * m3!!.toLong()
    println("$m1 * $m2 * $m3 == ${biggestThree}")

    return biggestThree
}

fun determineBasin(depthMap: Map<Pair<Int,Int>,Int>, points: Set<Pair<Int,Int>>): Set<Pair<Int,Int>> {
    var basin = mutableSetOf<Pair<Int,Int>>()

    for (point in points) {
        basin.add(point)
        val neighbours = higherSurroundingPoints(depthMap, point.first, point.second).toSet()
        val restOfBasin = determineBasin(depthMap, neighbours)
        basin.addAll(restOfBasin)
    }

    return basin
}


/**
 * Find the points that surround "point", that are higher but NOT equal to 9.
 */
fun higherSurroundingPoints(depthMap: Map<Pair<Int,Int>,Int>, x: Int, y:Int): List<Pair<Int,Int>> {
    val higherNeighbours = mutableListOf<Pair<Int,Int>>()

    val current = depthMap[Pair(x,y)]!!

    val left = depthMap[Pair(x-1,y)]
    if (left != null && left != 9 && left > current)
        higherNeighbours.add(Pair(x-1,y))

    val right = depthMap[Pair(x+1,y)]
    if (right != null && right != 9 && right > current)
        higherNeighbours.add(Pair(x+1, y))

    val above = depthMap[Pair(x, y-1)]
    if (above != null && above != 9 && above > current)
        higherNeighbours.add(Pair(x, y-1))

    val below = depthMap[Pair(x, y+1)]
    if (below != null && below != 9 && below > current)
        higherNeighbours.add(Pair(x, y+1))

    return higherNeighbours
}


fun parseDepthMap(inputLines: List<String>): Map<Pair<Int,Int>,Int> {
    val map = HashMap<Pair<Int,Int>, Int>()

    for (y in inputLines.indices) {
        val inputLine = inputLines[y]
        for (x in inputLine.indices) {
            map[Pair(x,y)] = inputLine[x].digitToInt()
        }
    }

    return map
}

fun summedRiskLevel(inputLines: List<String>): Int {
    val map = parseDepthMap(inputLines)

    val maxX = map.keys.map { it.first }.maxOrNull()!!
    val maxY = map.keys.map { it.second }.maxOrNull()!!

    var riskLevel = 0

    for (y in 0 .. maxY) {
        //println()
        for (x in 0 .. maxX) {
            val current = map[Pair(x,y)]!!
            //print(current)

            if (x > 0) {
                val left = map[Pair(x-1,y)]!!
                if (current >= left)
                    continue
            }

            if (x < maxX) {
                val right = map[Pair(x+1,y)]!!
                if (current >= right)
                    continue
            }

            if (y > 0) {
                var above = map[Pair(x,y-1)]!!
                if (current >= above)
                    continue
            }

            if (y < maxY) {
                val below = map[Pair(x,y+1)]!!
                if (current >= below)
                    continue
            }

            //println("Local minimum: ($x,$y)==$current")

            riskLevel += (1 + current)
        }
    }

    return riskLevel
}

fun findLowPoints(map: Map<Pair<Int,Int>, Int>): List<Pair<Int,Int>> {
    var lowPoints = mutableListOf<Pair<Int,Int>>()

    val maxX = map.keys.map { it.first }.maxOrNull()!!
    val maxY = map.keys.map { it.second }.maxOrNull()!!

    for (y in 0 .. maxY) {
        //println()
        for (x in 0 .. maxX) {
            val current = map[Pair(x,y)]!!
            //print(current)

            if (x > 0) {
                val left = map[Pair(x-1,y)]!!
                if (current >= left)
                    continue
            }

            if (x < maxX) {
                val right = map[Pair(x+1,y)]!!
                if (current >= right)
                    continue
            }

            if (y > 0) {
                var above = map[Pair(x,y-1)]!!
                if (current >= above)
                    continue
            }

            if (y < maxY) {
                val below = map[Pair(x,y+1)]!!
                if (current >= below)
                    continue
            }

            //println("Local minimum: ($x,$y)==$current")
            lowPoints.add(Pair(x,y))
        }
    }

    return lowPoints
}