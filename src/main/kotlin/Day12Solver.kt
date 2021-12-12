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

fun countPathsWithSmallCavesAtMostOnce(graph: List<Pair<String, String>>) =
    generalizedDepthFirstSearch(graph, Path(mutableListOf("start")), ::followingCaves)
fun countPathsWithSmallCavesAtMostTwice(graph: List<Pair<String, String>>) =
    generalizedDepthFirstSearch(graph, Path(mutableListOf("start")), ::followingCaves2)


fun generalizedDepthFirstSearch(
    graph: List<Pair<String, String>>,
    pathSoFar: Path,
    nextAcceptableCaves: (List<Pair<String,String>>, Path) -> List<String>
): Long {
    val nextCaves = nextAcceptableCaves(graph, pathSoFar)

    var sum: Long = 0
    for (cave in nextCaves) {
        if (cave == "end") {
            sum++
        } else {
            val nextPath = pathSoFar.copy()
            nextPath.add(cave)
            sum += generalizedDepthFirstSearch(graph, nextPath, nextAcceptableCaves)
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

    /**
     * Tell us how often the given cave has already been visited in this path.
     */
    fun countVisits(cave: String): Int {
        return nodes.count { it == cave }
    }

    fun hasSmallCaveBeenVisitedTwice(): Boolean {
        return nodes
            .filter { caveName -> caveName.isLowerCase() }  // Look only at the small caves.
            .groupingBy { it }                              // Group them by name.
            .eachCount()                                    // For every group, see how big it is (how often this name occurs).
            .values                                         // Select the group sizes; we're not interested in the names themselves, only in how often they occur.
            .any { it >= 2 }                                // Check if any of them occurs more than once. If so, return true; otherwise, return false.
    }
}

/**
 * Given a path and a map of a cave system, find the next caves to explore, where small caves are visited at most once.
 * Each cave has a name. Caves whose names are in lower case are small caves, the rest are large caves.
 * The cave system has a designated "start" and "end" cave, named "start" and "end" respectively.
 * The map consists of pairs of cave names; if two cave names occur in a pair, these caves are connected.
 * @param graph A map of a cave system, represented by pairs of caves: if a pair is present in the map, then those two caves are connected.
 * @param pathSoFar The path travelled so sar: an ordered list of cave names.
 * @return The list of caves connected to the last cave in the path, if no small cave must be visited twice.
 */
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
 * Given a path and map of a cave system, find the next caves to explore, where a <em>single</em> small cave is visited <em>at most twice</em>.
 * Each cave has a name. Caves whose names are in lower case are small caves, the rest are large caves.
 * The cave system has a designated "start" and "end" cave, named "start" and "end" respectively.
 * The map consists of pairs of cave names; if two cave names occur in a pair, these caves are connected.
 * @param graph A map of a cave system, represented by pairs of caves: if a pair is present in the map, then those two caves are connected.
 * @param pathSoFar The path travelled so sar: an ordered list of cave names.
 * @return The list of caves connected to the last cave in the path, if <em>one/<em> small cave can be visited twice, but all other small caves at most once.
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