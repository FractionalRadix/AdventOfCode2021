import java.awt.Point
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun solveDay25() {
    val inputList = Path("""inputFiles\AoCDay25.txt""").readLines()
    val map = parseInputDay25(inputList)
    val nrOfSteps = repeatUntilStable(map)
    println("Equilibrium in sea cucumber movement is reached in $nrOfSteps steps.") // 456.
}

fun parseInputDay25(inputList: List<String>): Map<Point, Char> {
    val result = mutableMapOf<Point, Char>()
    for (y in inputList.indices) {
        val line = inputList[y]
        for (x in line.indices) {
            result[Point(x,y)] = line[x]
        }
    }
    return result
}

fun horizontalStep(state: Map<Point, Char>): Map<Point, Char> {
    val nextState = mutableMapOf<Point, Char>()
    val maxX = state.keys.maxOf { it.x }
    val minX = state.keys.minOf { it.x }
    val width = maxX - minX + 1

    // Move all sea cucumbers that are headed eastwards.
    val xRange = state.keys.map { it.x }.toSortedSet()
    val yRange = state.keys.map { it.y }.toSortedSet()
    for (y in yRange) {
        for (x in xRange) {
            val targetPositionIsFree = nextState[Point(x, y)] == null
            if (targetPositionIsFree) {

                val seaCucumberGoingEastwards = (state[Point(x, y)] == '>')

                val nextX = (x + 1) % width
                val nextPositionIsFree = (state[Point(nextX, y)] == '.')

                if (seaCucumberGoingEastwards && nextPositionIsFree) {
                    nextState[Point(x, y)] = '.'
                    nextState[Point(nextX, y)] = '>'
                } else {
                    nextState[Point(x, y)] = state[Point(x, y)]!!
                }
            }
        }
    }

    return nextState
}

fun verticalStep(state: Map<Point, Char>): Map<Point, Char> {
    val nextState = mutableMapOf<Point, Char>()
    val maxY = state.keys.maxOf { it.y }
    val minY = state.keys.minOf { it.y }
    val height = maxY - minY + 1

    // Move all sea cucumbers that are headed downwards.
    val xRange = state.keys.map { it.x }.toSortedSet()
    val yRange = state.keys.map { it.y }.toSortedSet()
    for (x in xRange) {
        for (y in yRange) {

            val targetPositionIsFree = nextState[Point(x, y)] == null
            if (targetPositionIsFree) {

                val currentFieldContents = state[Point(x, y)]
                val seaCucumberGoingDownwards = (currentFieldContents == 'v' || currentFieldContents == 'V')

                val nextY = (y + 1) % height
                val nextPositionFree = (state[Point(x, nextY)] == '.')

                if (seaCucumberGoingDownwards && nextPositionFree) {
                    nextState[Point(x, y)] = '.'
                    nextState[Point(x, nextY)] = 'v'
                } else {
                    nextState[Point(x, y)] = state[Point(x, y)]!!
                }
            }
        }
    }

    return nextState
}

fun print(grid: Map<Point, Char>) {
    val maxX = grid.keys.maxOf { it.x }
    val maxY = grid.keys.maxOf { it.y }

    println()
    for (y in 0 .. maxY) {
        println()
        for (x in 0 .. maxX) {
            val contents = grid[Point(x,y)]
            print( contents ?: ' ')
        }
    }
}

fun repeatUntilStable(state0: Map<Point, Char>): Int {
    var count = 0

    var state1 = state0
    do {
        val state2 = verticalStep(horizontalStep(state1))
        count++
        if (state1 == state2)
            break
        state1 = state2
    } while (true)
    return count
}