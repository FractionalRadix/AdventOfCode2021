import java.awt.Point
import java.lang.Integer.min
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun solveDay15() {
    val inputList = Path("""inputFiles\AoCDay15.txt""").readLines()
    val map = parseInputDay15(inputList)

    println("Lowest total risk through the caves covered with chitons: ${dijkstra(map, Point(0,0))}") // 527.
    val largerMap = enlargeMap(map)
    //TODO!~ Brute force won't cut it...
    //  Use Dijkstra's algorithm to annotate the small map.
    //  Then apply it to the new maps.

    // PLAN: for every node on the edges, add the values of their neighbours
    //  (It's slightly more complicated: you need to do a mini-Dijkstra along every edge).
    // (And then you hope there's not some point where your submarine has to take a detour along one of those edges on the map....)

    //println("Lowest total risk through larger caves covered with chitons: ${dijkstra(largerMap, Point(0,0))}")
}

fun enlargeMap(map: Map<Point, Int>): Map<Point, Int> {
    val minX = map.keys.minOf { it.x }
    val minY = map.keys.minOf { it.y }
    val maxX = map.keys.maxOf { it.x }
    val maxY = map.keys.maxOf { it.y }
    val xRange = maxX - minX + 1
    val yRange = maxY - minY + 1

    val nextMap = mutableMapOf<Point, Int>()
    for (bigX in 0 until 5) {
        for (bigY in 0 until 5) {

            for (x in 0 .. maxX) {
                for (y in 0 .. maxY) {
                    var newVal = map[Point(x,y)]!! + bigX + bigY
                    if (newVal > 9) {
                        newVal -= 9
                    }
                    nextMap[Point(bigX * xRange + x, bigY * yRange + y)] = newVal
                }
            }
        }
    }

    //printMap(nextMap)

    return nextMap
}

fun printMap(map: Map<Point, Int>) {
    val minX = map.keys.minOf { it.x }
    val minY = map.keys.minOf { it.y }
    val maxX = map.keys.maxOf { it.x }
    val maxY = map.keys.maxOf { it.y }

    println()
    for (y in minY .. maxY) {
        println()
        for (x in minX .. maxX) {
            print(map[Point(x,y)])
        }
    }
}

fun dijkstra(map: Map<Point, Int>, source: Point): Int {
    val maxX = map.keys.maxOf { it.x }
    val maxY = map.keys.maxOf { it.y }

    val vertices_Q = mutableSetOf<Point>()

    val distances = mutableMapOf<Point, Int>()
    val previous = mutableMapOf<Point, Point>()
    for (vertex in map.keys) {
        distances[vertex] = Int.MAX_VALUE
        // previous[vertex] = undefined  <- automatic in Kotlin
        vertices_Q.add(vertex)
    }

    distances[source] = 0

    while (vertices_Q.isNotEmpty()) {
        val nrOfVertices = vertices_Q.size
        if (nrOfVertices % 100 == 0) {
            println(nrOfVertices)
        }
        //println("Nr of vertices in Q: ${vertices_Q.size}")
        // Find the node in Q with the smallest distance.
        // In other words, that entry in "distances" where the value is smallest, AND the key is in Q.
        val vertex_u = distances.filter { it.key in vertices_Q }.minByOrNull { it.value }!!.key
        vertices_Q.remove(vertex_u)

        val neighbours = mutableSetOf<Point>()

        if (vertex_u.x < maxX) {
            neighbours.add(Point(vertex_u.x + 1, vertex_u.y))
        }
        if (vertex_u.x > 0) {
            neighbours.add(Point(vertex_u.x - 1, vertex_u.y))
        }
        if (vertex_u.y < maxY) {
            neighbours.add(Point(vertex_u.x, vertex_u.y + 1))
        }
        if (vertex_u.y > 0) {
            neighbours.add(Point(vertex_u.x, vertex_u.y - 1))
        }

        for (neighbour_v in neighbours) {
            val alt = distances[vertex_u]!! + map[neighbour_v]!!
            if (alt < distances[neighbour_v]!!) {
                distances[neighbour_v] = alt
                previous[neighbour_v] = vertex_u
            }
        }
    }

    return distances[Point(maxX, maxY)]!!
}

fun parseInputDay15(inputLines: List<String>): Map<Point, Int> {
    val map = mutableMapOf<Point, Int>()
    for (y in inputLines.indices) {
        val digits = inputLines[y]
            .toCharArray()
            .map { it.digitToInt() }
        for (x in digits.indices) {
            map[Point(x,y)] = digits[x]
        }
    }
    return map
}