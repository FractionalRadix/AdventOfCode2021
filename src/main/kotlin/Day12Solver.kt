import kotlin.io.path.Path
import kotlin.io.path.readLines

fun solveDay12() {
    val inputLines = Path("""inputFiles\AoCDay12.txt""").readLines()
    val parsedInput = parseInputDay12(inputLines)

    println("Number of paths that visit small caves at most once: ${countPathsWithSmallCavesAtMostOnce(parsedInput)} ") // 3563
    println("Number of paths that visit a single small cave at most twice: ${countPathsWithSmallCavesAtMostTwice(parsedInput)}") // 105453
}

fun parseInputDay12(inputLines: List<String>) : List<Pair<String, String>> {
    val connections = mutableListOf<Pair<String,String>>()
    inputLines.forEach {
        val (start,end) = it.split("-")
        connections.add(Pair(start,end))
    }
    return connections
}

fun countPathsWithSmallCavesAtMostOnce(graph: List<Pair<String, String>>) = depthFirstSearch(graph, Path(mutableListOf("start")))
fun countPathsWithSmallCavesAtMostTwice(graph: List<Pair<String, String>>) = depthFirstSearch2(graph, Path(mutableListOf("start")))

fun depthFirstSearch(graph: List<Pair<String, String>>, pathSoFar: Path): Long {
    //pathSoFar.print()
    val nextCaves = followingCaves(graph, pathSoFar)

    var sum: Long = 0
    for (cave in nextCaves) {
        if (cave == "end") {
            sum++
        } else {
            val nextPath = pathSoFar.copy()
            nextPath.add(cave)
            sum += depthFirstSearch(graph, nextPath)
        }
    }

    return sum
}

fun depthFirstSearch2(graph: List<Pair<String, String>>, pathSoFar: Path): Long {
    //pathSoFar.print()
    val nextCaves = followingCaves2(graph, pathSoFar)

    var sum: Long = 0
    for (cave in nextCaves) {
        if (cave == "end") {
            sum++
        } else {
            val nextPath = pathSoFar.copy()
            nextPath.add(cave)
            sum += depthFirstSearch2(graph, nextPath)
        }
    }

    return sum
}


class Path(private val nodes: MutableList<String>) {
    fun add(node: String) {
        nodes.add(node)
    }

    fun contains(node: String) = nodes.contains(node)

    fun copy(): Path {
        val copy = Path(mutableListOf())
        for(node in nodes) {
            copy.nodes.add(node)
        }
        return copy
    }

    fun lastNode() = nodes.last()

    fun print() {
        println(nodes.joinToString(separator="-"))
    }

    /**
     * Tell us how often the given cave has already been visited in this path.
     */
    fun countVisits(cave: String): Int {
        return nodes.count { it == cave }
    }

    fun hasSmallCaveBeenVisitedTwice(): Boolean {
        val smallCaves = nodes
            .filter { caveName -> caveName.isLowerCase() }  // Look only at the small caves.

        for (cave in smallCaves) {
            if (smallCaves.count { it == cave } >= 2) {
                return true
            }
        }

        return false
    }
}

fun String.isLowerCase() : Boolean {
    for (ch in this) {
        if (!ch.isLowerCase())
            return false
    }
    return true
}

fun followingCaves(graph: List<Pair<String, String>>, pathSoFar: Path) : List<String> {
    val nextAcceptableCaves = mutableListOf<String>()
    val startNode = pathSoFar.lastNode()

    if (startNode == "end")
        return listOf()

    val nextNodes1 = graph.filter { it.first == startNode }.map { it.second }
    val nextNodes2 = graph.filter { it.second == startNode }.map { it.first }
    val nextNodes = nextNodes1.union(nextNodes2)

    for (node in nextNodes) {
        if (node.isLowerCase()) {
            if (!pathSoFar.contains(node)) {
                nextAcceptableCaves.add(node)
            }
        } else {
            nextAcceptableCaves.add(node)
        }
    }

    //println("followingCaves: startNode==$startNode going to ${nextAcceptableCaves.joinToString(prefix="{",separator=",", postfix="}")}")

    return nextAcceptableCaves
}

/**
 * Given a path and map, find the next caves to explore, where a single small cave is visited at most twice.
 * As soon as you need to visit another single cave twice, or the already-visited small cave is visited a third time, that path is discarded.
 */
fun followingCaves2(graph: List<Pair<String, String>>, pathSoFar: Path) : List<String> {
    val nextAcceptableCaves = mutableListOf<String>()
    val startNode = pathSoFar.lastNode()

    if (startNode == "end")
        return listOf()

    val nextNodes1 = graph.filter { it.first == startNode }.map { it.second }
    val nextNodes2 = graph.filter { it.second == startNode }.map { it.first }
    val nextNodes = nextNodes1.union(nextNodes2)

    for (node in nextNodes) {
        if (node.isLowerCase()) {
            if (node == "start")
                continue

            // Condition:
            //  (1) The small cave has not yet been visited, or
            //  (2) the small cave has been visited once, and NO OTHER small cave has been visited twice.

            val previousVisits = pathSoFar.countVisits(node)
            val caveVisitedTwice = pathSoFar.hasSmallCaveBeenVisitedTwice()
            if (caveVisitedTwice) {
                if (previousVisits == 0) {
                    // A small cave has been visited twice, but it isn't this one... so we can still visit this one.
                    nextAcceptableCaves.add(node)
                }
            } else {
                if (previousVisits == 0) {
                    nextAcceptableCaves.add(node)
                } else if (previousVisits == 1) {
                    nextAcceptableCaves.add(node)
                }
            }

        } else {
            nextAcceptableCaves.add(node)
        }
    }

    //println("followingCaves: startNode==$startNode going to ${nextAcceptableCaves.joinToString(prefix="{",separator=",", postfix="}")}")

    return nextAcceptableCaves
}