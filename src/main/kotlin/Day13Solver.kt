import java.awt.Point
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun solveDay13() {
    val inputList = Path("""inputFiles\AoCDay13.txt""").readLines()
    val (grid, folds) = parseInputDay13(inputList)

    println("Number of dots after folding the paper once: ${countDots(foldOnce(grid, folds[0].first, folds[0].second))}") // 942

    val fullyFolded = foldAll(grid, folds)
    display(fullyFolded)    // JZGUAPRB

}

fun display(grid: Map<Point,Char>) {
    val minX = grid.keys.minOf { it.x }
    val maxX = grid.keys.maxOf { it.x }
    val minY = grid.keys.minOf { it.y }
    val maxY = grid.keys.maxOf { it.y }

    println()
    for (y in minY .. maxY) {
        println()
        for (x in minX .. maxX) {
            val char = grid[Point(x,y)]
            if (char == null) {
                print(' ')
            } else {
                print('#')
            }
        }
    }
}

fun foldAll(grid: Map<Point,Char>, instructions: List<Pair<Char,Int>>): Map<Point,Char> {
    var nextGrid = grid
    for (instruction in instructions) {
        nextGrid = foldOnce(nextGrid, instruction.first, instruction.second)
    }
    return nextGrid
}

fun foldOnce(grid: Map<Point,Char>, axis: Char, threshold: Int): Map<Point, Char> {
    val nextGrid = mutableMapOf<Point, Char>()

    for (point in grid.keys) {
        if (axis=='x') {
            if (point.x < threshold) {
                nextGrid[point] = grid[point]!!
            } else {
                val nextPoint = Point(2 * threshold - point.x, point.y)
                nextGrid[nextPoint] = '#'
            }
        } else if (axis == 'y') {
            if (point.y < threshold) {
                nextGrid[point] = grid[point]!!
            } else {
                val nextPoint = Point(point.x, 2 * threshold - point.y)
                nextGrid[nextPoint] = '#'
            }
        }
    }

    return nextGrid
}

fun countDots(grid: Map<Point, Char>) = grid.values.count { it == '#' }

fun parseInputDay13(inputLines: List<String>): Pair<Map<Point,Char>, List<Pair<Char, Int>>> {
    val grid = mutableMapOf<Point, Char>()
    val folds = mutableListOf<Pair<Char, Int>>()
    for (line in inputLines) {
        if (line.isNotEmpty()) {
            if (line.startsWith("fold")) {
                val modifiedLine = line.drop(11)
                print(modifiedLine)
                val (axis, value) = modifiedLine.split('=')
                folds.add(Pair(axis[0], value.toInt()))
            } else {
                val (x, y) = line.split(",")
                val coordinates = Point(x.toInt(), y.toInt())
                grid[coordinates] = '#'
            }
        }
    }

    return Pair(grid, folds)
}