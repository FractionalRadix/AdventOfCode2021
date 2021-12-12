import java.awt.Point
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun solveDay11() {
    val inputLines =  Path("""inputFiles\AoCDay11.txt""").readLines()
    var parsedInput = parseInputDay11(inputLines)

    println("Number of flashes in 100 steps: ${countFlashes(parsedInput)}") // 1647

    parsedInput = parseInputDay11(inputLines)
    println("First step in which all octopuses flash: ${firstSynchronizedFlash(parsedInput)}") // 348
}

fun parseInputDay11(inputLines: List<String>): MutableMap<Point, Int> {
    val grid = mutableMapOf<Point, Int>()
    for (y in inputLines.indices) {
        val line = inputLines[y]
        for (x in line.indices) {
            val digit = line[x]
            grid[Point(x,y)] = digit - '0'
        }
    }
    return grid
}

fun countFlashes(grid: MutableMap<Point, Int>): Long {
    var flashes: Long = 0

    for (i in 1 .. 100) {
        flashes += step(grid)
        //print(grid)
    }

    return flashes
}

fun print(grid: MutableMap<Point, Int>) {
    println()
    for (y in 0..9) {
        for (x in 0..9) {
            print(grid[Point(x,y)])
        }
        println()
    }
}

fun step(grid: MutableMap<Point, Int>): Long {
    // First, increase every position by 1.
    grid.keys.forEach { grid[it] = grid[it]!! + 1 }

    // Second, if any octopus reaches 10, increase the energy level of its neighbours.
    // And if that causes more octopuses to reach 10, increase the energy level of their neighbours, too.
    // And if THAT causes more octopuses to reach 10, increase the energy level of THEIR neighbours...
    // ...And so on, until no more octopuses reach 10 in this step.
    val flashers = mutableSetOf<Point>()
    do {
        val newFlashers = grid.keys.filter { grid[it]!! > 9 }.toMutableSet() - flashers
        newFlashers.forEach { increaseNeighbours(grid, it) }
        flashers.addAll(newFlashers)
    } while (newFlashers.isNotEmpty())

    // Finally, reset the energy level of all flashers to 0.
    flashers.forEach { grid[it] = 0 }

    return flashers.size.toLong()
}

fun increaseNeighbours(grid: MutableMap<Point, Int>, position: Point) {
    //getNeighbours(grid, position).forEach { grid[it] = grid[it]!! + 1 }
    getAllNeighbours(grid, position).forEach { grid[it] = grid[it]!! + 1 }
}

fun firstSynchronizedFlash(grid: MutableMap<Point, Int>): Long {
    var counter = 0
    do {
        val count = step(grid)
        counter++
    } while (count < 100)

    return counter.toLong()
}

