import java.awt.Point
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun solveDay20() {
    val inputList = Path("""inputFiles\AoCDay20.txt""").readLines()
    println("After applying the enhancement algorithm twice, the number of positive bits is ${solveDay20Part1(inputList)}") // 4873.
    println("After applying the enhancement algorithm fifty times, the number of positive bits is ${solveDay20Part2(inputList)}") // 16394.
}

// The catch for this problem is: the image is "infinite".
// This means that undefined pixels are either '.' or '#'.
// If the first character in the enhancement string is '.', then a field of 9 '.' (which yields 0) results in a '.'.
// If the first character in the enhancement string is '#', then a field of 9 '.' (which yields 0) results in a '#'.
// So... if the first character in the enhancement string is '#', the undefined fields will become '#' as well.
// Now this results in the undefined fields yielding 511 in the next round, so you need to look at the *last* character of the enhancement string too.
// And if that last character is '.', then the thing will flip back again!

fun solveDay20Part2(inputList: List<String>): Int {
    var (enhancer, image) = parseInputDay20(inputList)
    for (i in 1..50) {
        // If the image enhancer string has different items for the first and last element, then the value of unidentified dots flips.
        // Unidentified dots will be '#' after one application, but '.' after the next.
        val alternation = enhancer[0] == '#' && enhancer[511] == '.'
        val interpretNullsAsLightPixels = alternation && ((i % 2) == 0)
        image = enhanceImage(image, enhancer, interpretNullsAsLightPixels)
    }
    return image.values.count { it }
}

fun solveDay20Part1(inputList: List<String>): Int {
    val (enhancer, image0) = parseInputDay20(inputList)
    val image1 = enhanceImage(image0, enhancer, false)
    val alternation = enhancer[0] == '#' && enhancer[511] == '.'
    val image2 = enhanceImage(image1, enhancer, alternation)
    return image2.values.count { it }
}

fun getXRange(grid: Map<Point, Boolean>): IntRange {
    val minX = grid.keys.minOf { it.x }
    val maxX = grid.keys.maxOf { it.x }
    return IntRange(minX, maxX)
}

fun getYRange(grid: Map<Point, Boolean>): IntRange {
    val minY = grid.keys.minOf { it.y }
    val maxY = grid.keys.maxOf { it.y }
    return IntRange(minY, maxY)
}

fun enhanceImage(image: Map<Point, Boolean>, enhancer: String, interpretNullsAsLightPixels: Boolean): Map<Point, Boolean> {
    val enhancedImage = mutableMapOf<Point, Boolean>()
    val xRange = getXRange(image)
    val yRange = getYRange(image)

    val x0 = xRange.first - 1
    val x1 = xRange.last + 1
    val y0 = yRange.first - 1
    val y1 = yRange.last + 1

    for (x in x0 .. x1) {
        for (y in y0 .. y1) {
            val value = subGridValue(image, x, y, interpretNullsAsLightPixels)
            val newBit = enhancer[value] == '#'
            enhancedImage[Point(x,y)] = newBit
        }
    }

    return enhancedImage
}

fun printImage(image: Map<Point, Boolean>, interpretNullsAsLightPixels: Boolean) {
    val xRange = getXRange(image)
    val yRange = getYRange(image)

    println()
    for (y in yRange) {
        println()
        for (x in xRange) {
            val point = Point(x,y)
            if (image[point] == null) {
                if (interpretNullsAsLightPixels) {
                    print('#')
                } else {
                    print('.')
                }
            } else /* image[point] != null */ {
                if (image[point]!!) {
                    print('#')
                } else {
                    print('.')
                }
            }
        }
    }
}

fun subGridValue(image: Map<Point, Boolean>, x: Int, y: Int, interpretNullsAsLightPixels: Boolean): Int {
    var bitValue = 1
    var totalValue = 0
    for (dy in +1 downTo -1) {
        for (dx in +1 downTo -1) {
            val originalBit = image[Point(x + dx, y + dy)]
            if (originalBit == null) {
                if (interpretNullsAsLightPixels) {
                    totalValue += bitValue
                }
            } else { // originalBit != null
                if (originalBit == true) {
                    totalValue += bitValue
                }
            }
            bitValue *= 2
        }
    }
    return totalValue
}

fun parseInputDay20(inputList: List<String>): Pair<String, Map<Point, Boolean>> {
    val enhancer = inputList[0]
    val image = mutableMapOf<Point, Boolean>()
    for (row in 2 until inputList.size) {
        val line = inputList[row]
        for (col in line.indices) {
            val bool = line[col] == '#'
            image[Point(col, row - 2)] = bool
        }
    }
    return Pair(enhancer, image)
}