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

fun display(grid: Set<Point>) {
    val minX = grid.minOf { it.x }
    val maxX = grid.maxOf { it.x }
    val minY = grid.minOf { it.y }
    val maxY = grid.maxOf { it.y }

    println()
    for (y in minY .. maxY) {
        println()
        for (x in minX .. maxX) {
            if (grid.contains(Point(x,y))) {
                print('#')
            } else {
                print(' ')
            }
        }
    }
}

fun foldAll(grid: Set<Point>, instructions: List<Pair<Char,Int>>): Set<Point> {
    var nextGrid = grid
    for (instruction in instructions) {
        nextGrid = foldOnce(nextGrid, instruction.first, instruction.second)
    }
    return nextGrid
}

fun foldOnce(grid: Set<Point>, axis: Char, threshold: Int): Set<Point> {
    val nextGrid = mutableSetOf<Point>()

    for (point in grid) {
        if (axis=='x') {
            if (point.x < threshold) {
                nextGrid.add(point)
            } else {
                nextGrid.add(Point(2 * threshold - point.x, point.y))
            }
        } else if (axis == 'y') {
            if (point.y < threshold) {
                nextGrid.add(point)
            } else {
                val nextPoint = Point(point.x, 2 * threshold - point.y)
                nextGrid.add(nextPoint)
            }
        }
    }

    return nextGrid
}

fun countDots(grid: Set<Point>) = grid.size

fun parseInputDay13(inputLines: List<String>): Pair<Set<Point>, List<Pair<Char, Int>>> {
    val grid = mutableSetOf<Point>()
    val folds = mutableListOf<Pair<Char, Int>>( )
    for (line in inputLines) {
        if (line.isNotEmpty()) {
            if (line.startsWith("fold")) {
                //val modifiedLine = line.drop(11)
                //val (axis, value) = modifiedLine.split('=')
                val (axis, value) = line
                    .drop(11)
                    .split('=')
                folds.add(Pair(axis[0], value.toInt()))
            } else {
                val (x, y) = line.split(",")
                grid.add(Point(x.toInt(), y.toInt()))
            }
        }
    }

    return Pair(grid, folds)
}