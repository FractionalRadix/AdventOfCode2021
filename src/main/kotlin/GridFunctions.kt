import java.awt.Point

/**
 * Given a mapping from (2D) points to T, find the range from the lowest to the highest x-coordinate (inclusive).
 * @param grid A mapping from points to values.
 * @return The range starting at the lowest x-coordinate and ending at the highest x-coordinate, inclusive.
 */
fun <T> getXRange(grid: Map<Point, T>): IntRange {
    val minX = grid.keys.minOf { it.x }
    val maxX = grid.keys.maxOf { it.x }
    return IntRange(minX, maxX)
}

/**
 * Given a mapping from (2D) points to T, find the range from the lowest to the highest y-coordinate (inclusive).
 * @param grid A mapping from points to values.
 * @return The range starting at the lowest y-coordinate and ending at the highest y-coordinate, inclusive.
 */
fun <T> getYRange(grid: Map<Point, T>): IntRange {
    val minY = grid.keys.minOf { it.y }
    val maxY = grid.keys.maxOf { it.y }
    return IntRange(minY, maxY)
}

/**
 * Given a mapping from (2D) points to T, find all points that are horizontally or vertically adjacent to the given point.
 * For example, if the mapping contains values for all elements in the range [0,0]-[10,10], then (5,5) would yield the points (4,5),(6,5),(5,4), and (5,6),
 * Similarly, the point (0,0) would only yield (0,1) and (1,0), as (-1,0) and (0,-1) are not in the grid.
 * Note that if a value is not in the mapping, it is not counted as a neighbour.
 * So in the above example, if <code>mapping[Point(4,5)]==null</code>, but all other points were defined, the result would be (6,5),(5,4), and (5,6).
 * The point at (4,5) would not be counted as a neighbour.
 * @param grid A mapping from points to values.
 * @param position A point on the grid.
 * @return All the direct horizontal and vertical neighbours of the given point, that do not map to <code>null</code>.
 */
fun <T> getHorizontalAndVerticalNeighbours(grid: MutableMap<Point, T>, position: Point): Set<Point> {
    val neighbours = mutableSetOf<Point>()
    val deltaList = listOf(Point(-1,0), Point(+1, 0), Point(0, -1), Point(0, +1))

    for (delta in deltaList) {
        val neighbour = Point(position.x + delta.x, position.y + delta.y)
        if (grid[neighbour] != null) {
            neighbours.add(neighbour)
        }
    }

    return neighbours
}

/**
 * Given a mapping from (2D) points to T, find all points that are diagonally adjacent to the given point.
 * For example, if the mapping contains values for all elements in the range [0,0]-[10,10], then (5,5) would yield the points (4,4), (4,6), (6,4), and (6,6).
 * Similarly, the point (0,0) would only yield (1,1), as (-1,-1), (-1,1), and (1,-1) are not in the grid.
 * Note that if a value is not in the mapping, it is not counted as a neighbour.
 * So in the above example, if <code>mapping[Point(4,6)]==null</code>, but all other points were defined, the result would be (4,4),(6,4), and (6,6).
 * The point at (4,6) would not be counted as a neighbour.
 * @param grid A mapping from points to values.
 * @param position A point on the grid.
 * @return All the direct diagonal neighbours of the given point, that do not map to <code>null</code>.
 */
fun <T> getDiagonalNeighbours(grid: MutableMap<Point, T>, position: Point): Set<Point> {
    val neighbours = mutableSetOf<Point>()
    for (dx in -1 .. +1) {
        for (dy in -1 .. +1) {
            if (dx != 0 && dy != 0) {
                val neighbour = Point(position.x + dx, position.y + dy)
                if (grid[neighbour] != null) {
                    neighbours.add(neighbour)
                }
            }
        }
    }
    return neighbours
}

/**
 * Given a mapping from (2D) points to T, find all points that are adjacent to the given point - horizontally, vertically, or diagonally.
 * For example, if the mapping contains values for all elements in the range [0,0]-[10,10], then (5,5) would yield the points (4,4), (4,5), (4,6),
 * (5,4), (5,6), (6,4), (6,5), and (6,6).
 * Similarly, the point (0,0) would only yield (0,1), (1,0), and (1,1); as (-1,-1), (-1,0), (1,-1), (-1,0) and (0,-1) are not in the grid.
 * Note that if a value is not in the mapping, it is not counted as a neighbour.
 * So in the above example, if <code>mapping[Point(4,5)]==null</code>, but all other points were defined, the result would be (4,4), (4,6),
 * (5,4), (5,6), (6,4), (6,5), and (6,6).
 * The point at (4,5) would not be counted as a neighbour.
 * @param grid A mapping from points to values.
 * @param position A point on the grid.
 * @return All the direct neighbours of the given point, that do not map to <code>null</code>.
 */
fun <T> getAllNeighbours(grid: MutableMap<Point, T>, position: Point): Set<Point> {
    val neighbours = mutableSetOf<Point>()
    for (dx in -1 .. +1) {
        for (dy in -1 .. +1) {
            if (dx != 0 || dy != 0) {
                val neighbour = Point(position.x + dx, position.y + dy)
                if (grid[neighbour] != null) {
                    neighbours.add(neighbour)
                }
            }
        }
    }
    return neighbours
}
