import java.lang.Integer.*
import java.lang.Math.abs
import kotlin.math.sign
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun solveDay05() {
    val inputList = Path("""inputFiles\AoCDay05.txt""").readLines()

    val lineSegments = parseInputDay05(inputList)
    val field = fillField1(lineSegments)
    println("Overlaps in horizontal and vertical lines: ${field.countOverlaps()}")
    val field2 = fillField2(lineSegments)
    println("Overlaps in horizontal, vertical, and diagonal lines: ${field2.countOverlaps()}")

    /*
    5280
    16716
     */
}

fun fillField1(lineSegments: List<LineSegment>) : Field {
    val field = Field()
    for (line in lineSegments) {
        if (line.x1 == line.x2) {
            val minY = min(line.y1, line.y2)
            val maxY = max(line.y1, line.y2)
            for (y in minY .. maxY) {
                field.add(line.x1, y)
            }
        } else if (line.y1 == line.y2) {
            val minX = min(line.x1, line.x2)
            val maxX = max(line.x1, line.x2)
            for (x in minX .. maxX) {
                field.add(x, line.y1)
            }
        }
    }
    return field
}

fun fillField2(lineSegments: List<LineSegment>) : Field {
    val field = Field()
    for (line in lineSegments) {
        val xlen = abs(line.x1 - line.x2)
        val ylen = abs(line.y1 - line.y2)
        val len = max(xlen, ylen)
        for (i in 0 .. len) {
            val dx = (line.x2 - line.x1).sign
            val dy = (line.y2 - line.y1).sign
            field.add(line.x1 + i * dx, line.y1 + i * dy)
        }
    }
    return field
}

class Field {
    private var map = mutableMapOf<Pair<Int,Int>,Int>()

    fun print() {
        val maxX = map.keys.maxOf { it.first }
        val maxY = map.keys.maxOf { it.second }
        for (y in 0 .. maxY) {
            println()
            for (x in 0 .. maxX) {
                val counter = map[Pair(x,y)]
                if (counter==null)
                    print('.')
                else
                    print(counter)
            }
        }
    }
    fun add(x: Int, y:Int) {
        val key = Pair(x,y)
        val counter = map[key]
        map[key] = if (counter == null) {
            1
        }
        else {
            counter + 1
        }
    }

    fun countOverlaps(): Int {
        return map.values.count { it >= 2 }
    }
}

fun parseInputDay05(input: List<String>) : List<LineSegment> {
    val result = mutableListOf<LineSegment>()
    for (line in input) {
        val split = line.split("->")
        val coor1 = split[0].trim().split(",")
        val x1 = parseInt(coor1[0])
        val y1 = parseInt(coor1[1])
        val coor2 = split[1].trim().split(",")
        val x2 = parseInt(coor2[0])
        val y2 = parseInt(coor2[1])
        result.add(LineSegment(x1,y1,x2,y2))
    }
    return result
}

data class LineSegment(val x1: Int, val y1: Int, val x2: Int, val y2:Int)