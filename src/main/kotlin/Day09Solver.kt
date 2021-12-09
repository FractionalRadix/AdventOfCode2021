import java.awt.Point
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Solve the Advent of Code puzzles for December 9, 2021.
 */
fun solveDay09() {
    val inputList = Path("""inputFiles\AoCDay09.txt""").readLines()
    val depthMap = parseDepthMap(inputList)

    println("Summed risk level: ${summedRiskLevel(depthMap)}")   // 541.
    println("Product of the sizes of the three largest basins: ${multipliedBasinSizes(depthMap)}") // 847504.
}

/**
 * Given a depth map, determine the size of the three largest basins. Multiply these numbers together.
 * @param depthMap A map from points to depths. (Note that "0" is the lowest depth and "9" is the highest... don't think too long about that!)
 * @return The product of the sizes of the largest three basins in the depth map.
 */
fun multipliedBasinSizes(depthMap: Map<Point, Int>): Int {
    val lowPoints = findLowPoints(depthMap)
    val basinSizes = mutableListOf<Int>()

    // Now that we have the low points, find the basins surrounding them.
    // We do this using the recursive function "determineBasin".
    for (lowPoint in lowPoints) {
        val basin = determineBasin(depthMap, setOf(lowPoint))
        basinSizes.add(basin.size)
    }

    // Now that you know the basin sizes... sort the list of basin sizes, and take the last (and therefore biggest) elements.
    // Multiply them together. That's your answer.
    return basinSizes
        .sorted()
        .takeLast(3)
        .fold(1) { mul, item -> mul * item }
}

/**
 * Given a depth map and a set of points, determine the "basin" around them.
 * Each basin is all the points that are higher than their neighbours, excepting 9.
 * In other words, if you found yourself in a basin, there would be a monotonously decreasing path towards the lowest point.
 * @param depthMap A map from points to depths. (Note that "0" is the lowest depth and "9" is the highest... don't think too long about that!)
 * @points The set of points for which we desire to know the basin they're in.
 * @return The set of points that spans the entire basin(s) for the given set of input points.
 */
fun determineBasin(depthMap: Map<Point,Int>, points: Set<Point>): Set<Point> {
    val basin = mutableSetOf<Point>()

    for (point in points) {
        basin.add(point)
        val neighbours = higherSurroundingPoints(depthMap, point).toSet()
        val restOfBasin = determineBasin(depthMap, neighbours)
        basin.addAll(restOfBasin)
    }

    return basin
}

/**
 * Find the points that surround "point", that are higher but NOT equal to 9.
 */
fun higherSurroundingPoints(depthMap: Map<Point,Int>, p: Point): List<Point> {
    val higherNeighbours = mutableListOf<Point>()

    val current = depthMap[p]!!

    val left = depthMap[Point(p.x-1,p.y)]
    if (left != null && left != 9 && left > current)
        higherNeighbours.add(Point(p.x-1,p.y))

    val right = depthMap[Point(p.x+1,p.y)]
    if (right != null && right != 9 && right > current)
        higherNeighbours.add(Point(p.x+1, p.y))

    val above = depthMap[Point(p.x, p.y-1)]
    if (above != null && above != 9 && above > current)
        higherNeighbours.add(Point(p.x, p.y-1))

    val below = depthMap[Point(p.x, p.y+1)]
    if (below != null && below != 9 && below > current)
        higherNeighbours.add(Point(p.x, p.y+1))

    return higherNeighbours
}

/**
 * Given a list of strings that solely contain digits, determine the mapping that maps each (column,row) pair to a digit value.
 * @return A mapping of the digits in the input. Note that we use Point, not (col,row), as this data is supposed to represent a map.
 */
fun parseDepthMap(inputLines: List<String>): Map<Point,Int> {
    val map = HashMap<Point, Int>()

    for (y in inputLines.indices) {
        val inputLine = inputLines[y]
        for (x in inputLine.indices) {
            map[Point(x,y)] = inputLine[x].digitToInt()
        }
    }

    return map
}

/**
 * Find the risk level in a given depth map.
 * The risk level is defined as the sum of the lowest points, plus 1 for each lowest point.
 * @param depthMap A map from points to depths. (Note that "0" is the lowest depth and "9" is the highest... don't think too long about that!)
 * @return The sum of the depths of the lowest points, plus 1 for each point.
 */
fun summedRiskLevel(depthMap: Map<Point, Int>): Int {
    val lowPoints = findLowPoints(depthMap)
    val summedValuesOfLowPoints = lowPoints
        .mapNotNull { depthMap[it] }
        .sum()
    return lowPoints.size + summedValuesOfLowPoints
}

/**
 * Find the points for which every horizontal or vertical neighbour has a higher value.
 * @param map A mapping from coordinates to depth values.
 * @return All points in the mapping whose neighbours, if they exist, are all higher. Diagonal neighbours aren't considered.
 */
fun findLowPoints(map: Map<Point, Int>): List<Point> {
    val lowPoints = mutableListOf<Point>()

    val maxX = map.keys.maxOf { it.x }
    val maxY = map.keys.maxOf { it.y }

    for (y in 0 .. maxY) {
        for (x in 0 .. maxX) {
            val current = map[Point(x,y)]!!

            val left = map[Point(x-1,y)]
            if (left != null && current >= left)
                continue

            val right = map[Point(x+1,y)]
            if (right != null && current >= right)
                continue

            val above = map[Point(x,y-1)]
            if (above != null && current >= above)
                continue

            val below = map[Point(x,y+1)]
            if (below != null && current >= below)
                continue

            lowPoints.add(Point(x,y))
        }
    }

    return lowPoints
}