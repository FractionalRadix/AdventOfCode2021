fun solveDay19() {
    //val inputList = Path("""inputFiles\AoCDay19.txt""").readLines()
    //TODO!+


}

class SeaScanner(val number: Int) {
    val data = mutableListOf<Triple<Int,Int,Int>>()

    fun addPoint(x: Int, y: Int, z: Int) {
        data.add(Triple(x,y,z))
    }
}

// A few convenience classes to make the whole thing more legible.
data class Point3D(val x: Int, val y: Int, val z: Int)
data class Translation3D(val x: Int, val y: Int, val z: Int)

fun parseDay19(inputList: List<String>): List<SeaScanner> {
    val seaScanners = mutableListOf<SeaScanner>()

    var currentScanner: SeaScanner? = null
    for (line in inputList) {
        if (line.contains("scanner")) {
            if (currentScanner != null) {
                seaScanners.add(currentScanner)
            }
            val scannerIdentificationToken = line.split(' ')[2]
            val scannerId = scannerIdentificationToken.toInt()
            currentScanner = SeaScanner(scannerId)
        } else if (line.isNotEmpty()) {
            val (x,y,z) = line.split(',')
            currentScanner?.addPoint(x.toInt(), y.toInt(), z.toInt())
        }
    }
    seaScanners.add(currentScanner!!)

    return seaScanners
}

operator fun Triple<Int,Int,Int>.minus(b: Triple<Int, Int, Int>): Triple<Int, Int, Int> {
    return Triple(this.first - b.first, this.second - b.second, this.third - b.third)
}


fun determineScannerPositions(scanners: List<SeaScanner>): Map<SeaScanner, Triple<Int, Int, Int>> {
    val positions = mutableMapOf<SeaScanner, Triple<Int, Int, Int>>()
    val relativePositions = compareAllScans(scanners)

    positions[scanners[0]] = Triple(0, 0, 0)

    //TODO!~ New algorithm.
    //  Look at all the positions we already know, and look which scanners are nearby that... that aren't yet filled in.


    TODO()

    return positions
}

fun OLD_determineScannerPositions(scanners: List<SeaScanner>): Map<SeaScanner, Triple<Int, Int, Int>> {
    val positions = mutableMapOf<SeaScanner, Triple<Int, Int, Int>>()
    val relativePositions = compareAllScans(scanners)

    positions[scanners[0]] = Triple(0,0,0)

    while (positions.size < scanners.size) {
        for (scanner in scanners) {
            if (!positions.containsKey(scanner)) {
                // We have us a scanner whose position is not yet known.
                // Is there a scanner relative to it, whose position IS known?
                val nearbyScanners1 = relativePositions
                    .filter { it.first == scanner }
                    .map { it.second }
                    .filter { positions.containsKey(it) }

                val nearbyScanners2 = relativePositions
                    .filter { it.second == scanner }
                    .map { it.first }
                    .filter { positions.containsKey(it) }    // Separate case: negative translation?

                if (nearbyScanners1.isNotEmpty()) {
                    val nearbyScanner = nearbyScanners1[0]
                    if (positions.containsKey(nearbyScanner)) {
                        val locationOfNearbyScanner = positions[nearbyScanner]!!
                        val relativeLocationOfNearbyScanner = relativePositions
                            .firstOrNull { it.first == scanner && it.second == nearbyScanner }
                            ?.third

                        val position = Triple(
                            locationOfNearbyScanner.first + relativeLocationOfNearbyScanner!!.x,
                            locationOfNearbyScanner.second + relativeLocationOfNearbyScanner.y,
                            locationOfNearbyScanner.third + relativeLocationOfNearbyScanner.z
                        )

                        positions[scanner] = position
                    }
                } else if (nearbyScanners2.isNotEmpty()) {
                    val nearbyScanner = nearbyScanners2[0]
                    if (positions.containsKey(nearbyScanner)) {
                        val locationOfNearbyScanner = positions[nearbyScanner]!!
                        val relativeLocationOfNearbyScanner = relativePositions
                            .firstOrNull { it.first == nearbyScanner && it.second == scanner }
                            ?.third

                        val position = Triple(
                            locationOfNearbyScanner.first + relativeLocationOfNearbyScanner!!.x,
                            locationOfNearbyScanner.second + relativeLocationOfNearbyScanner.y,
                            locationOfNearbyScanner.third + relativeLocationOfNearbyScanner.z
                        )

                        positions[scanner] = position
                    }
                }

            }
        }
    }

    return positions
}

fun compareAllScans(scanners: List<SeaScanner>): List<Triple<SeaScanner, SeaScanner, Translation3D>> {
    val result = mutableListOf<Triple<SeaScanner, SeaScanner, Translation3D>>()
    val indices1 = scanners.indices
    val indices2 = scanners.indices.toMutableList()

    for (idx1 in indices1) {
        indices2.remove(idx1)
        for (idx2 in indices2) {
            val scanner1 = scanners[idx1]
            val scanner2 = scanners[idx2]
            println("Comparing scanners ${scanner1.number} and ${scanner2.number}:")
            val comparison = compareScansAllRotations(scanner1.data, scanner2.data)
            if (comparison.isNotEmpty()) {
                // Note that there should be only 1 value in it...
                val translation = comparison.elementAt(0)
                result.add(Triple(scanner1, scanner2, translation))
            }
        }
    }
    return result
}

fun compareScansAllRotations(scan1: List<Triple<Int, Int, Int>>, scan2: List<Triple<Int, Int, Int>>): Set<Translation3D> {
    val translations = mutableSetOf<Translation3D>()
    val modifiedScans = mutableMapOf<Int, MutableList<Triple<Int, Int, Int>>>()

    // For every transformation, create a separate list.
    for (beacon2 in scan2) {
        val possibleCoordinates = allPossiblePositions(beacon2)
        for (i in possibleCoordinates.indices) {
            if (modifiedScans[i] == null) {
                modifiedScans[i] = mutableListOf()
            }
            modifiedScans[i]!!.add(possibleCoordinates[i])
        }
    }

    // For every transformed list, determine the distances between the points in the transformed list, and those in the first scan.
    for (i in modifiedScans.keys) {
        val scanResult = compareScans(scan1, modifiedScans[i]!!)
        translations.addAll(scanResult)
    }

    return translations
}

fun compareScans(scan1: List<Triple<Int, Int, Int>>, scan2: List<Triple<Int, Int, Int>>): Set<Translation3D> {
    val translations = mutableListOf<Translation3D>()
    for (beacon1 in scan1) {
        for (beacon2 in scan2) {
            val diff = beacon1 - beacon2
            translations.add(Translation3D(diff.first, diff.second, diff.third))
        }
    }
    val grouping = translations
        .groupingBy { it }
        .eachCount()
        .filter { it.value >= 12 }

    if (grouping.isNotEmpty()) {
        println(grouping)
    }

    return grouping.keys
}

fun allPossiblePositions(point: Triple<Int, Int, Int>): List<Triple<Int, Int, Int>> {
    val result = mutableListOf<Triple<Int, Int, Int>>()

    result.addAll(rotateAroundZAxis(point))

    // Rotate the original point 90 degrees around the Y axis, then calculate again.
    result.addAll(rotateAroundZAxis(Triple(point.third, point.second, -point.first)))

    // Rotate the original point 180 degrees around the Y axis, then calculate again.
    result.addAll(rotateAroundZAxis(Triple(-point.first, point.second, -point.third)))

    // Rotate the original point 270 degrees around the Y axis, then calculate again.
    result.addAll(rotateAroundZAxis(Triple(-point.third, point.second, point.first)))

    // Rotate the original point +90 degrees around the X axis, then calculate again.
    result.addAll(rotateAroundZAxis(Triple(point.first, -point.third, point.second)))

    // Rotate the original point -90 degrees around the X axis, then calculate again.
    result.addAll(rotateAroundZAxis(Triple(point.first, point.third, -point.second)))

    return result
}

/**
 * Given a point in a 3D space, determine the positions of this point if it is rotated around the Z-axis
 * by 0 degrees, 90 degrees, 180 degrees, and 270 degrees, respectively.
 * @param A point in a 3-dimensional space.
 * @return A list of the coordinates of this point, when it is rotated by a straight angle around the Z-axis.
 */
fun rotateAroundZAxis(point: Triple<Int, Int, Int>) = listOf(
    Triple(point.first, point.second, point.third),
    Triple(-point.second, point.first, point.third),
    Triple(-point.first, -point.second, point.third),
    Triple(point.second, -point.first, point.third),
)
