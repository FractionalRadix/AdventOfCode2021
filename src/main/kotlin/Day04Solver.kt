import kotlin.io.path.Path
import kotlin.io.path.readLines

fun solveDay04() {
    val inputList = Path("""inputFiles\AoCDay04.txt""").readLines()

    findWinningboard()

    val draws = inputList[0].split(',').map { it.toInt() }
    var boards = parseBoards(inputList.drop(2))

    findLoosingBoard(draws, boards)
}

fun findWinningboard() {
    val inputList = Path("""inputFiles\AoCDay04.txt""").readLines()

    val draws = inputList[0].split(',').map { it.toInt() }
    var boards = parseBoards(inputList.drop(2))

    for (board in boards) {
        board.print()
    }

    var sumOfWinningBoard = 0
    var lastDraw = 0
    for (draw in draws) {
        println("DRAW: $draw")
        lastDraw = draw
        for (board in boards) {
            board.draw(draw)
            if (board.hasColumnOfNulls() || board.hasRowOfNulls()) {
                board.print()
                print(board.sum())
                sumOfWinningBoard = board.sum()
                break
            }
        }
        if (sumOfWinningBoard > 0)
            break
    }

    println("Sum of winning board = $sumOfWinningBoard")
    println("Last draw= $lastDraw")
    println("Sum of winning board times last draw = ${sumOfWinningBoard * lastDraw}")
}

fun findLoosingBoard(draws: List<Int>, val_boards: MutableList<BingoBoard>) {

    var boards = val_boards
    var sumOfLoosingBoard = 0
    var lastDraw = 0
    for (draw in draws) {
        lastDraw = draw
        for (board in boards) {
            board.draw(draw)
            println("DRAW: $draw")
            //board.print()
        }
        // Filter out the boards that won this round, unless there is only one board remaining.
        if (boards.size > 1) {
            boards = boards.filter { !it.hasColumnOfNulls() && !it.hasRowOfNulls() }.toMutableList()
        }
        // If only one board remains, keep playing until it has a row or column.
        if (boards.size == 1) {
            if (boards[0].hasRowOfNulls() || boards[0].hasColumnOfNulls()) {
                sumOfLoosingBoard = boards[0].sum()
                break
            }
        }
    }

    println("Sum of loosing board: $sumOfLoosingBoard")
    println("Last draw: $lastDraw")
    println("Sum of loosing board times last draw: ${sumOfLoosingBoard * lastDraw}")
}

fun parseBoards(inputList: List<String>): MutableList<BingoBoard> {
    var boards = mutableListOf<BingoBoard>()
    var current = BingoBoard()

    var counter = 0;
    for (row in inputList.filter { it != "" }) {
        current.addRow(row)
        counter++
        if (counter == 5) {
            current.finish()
            boards.add(current)
            current = BingoBoard()
            counter = 0
        }
    }
    return boards
}

class BingoBoard {
    var rows = mutableListOf(mutableListOf<Int?>())

    fun addRow(row: String) {
        val values = row
            .split(' ')
            .filter { it != "" }
            .map { it.toInt() }
        rows.add(values.toMutableList())
    }

    // Auxiliary function.
    // Since the boards start with one empty list, remove that first line.
    fun finish() {
        rows.removeAt(0)
    }

    fun draw(number: Int) {
        for (row in rows) {
            for (colIdx in 0 until row.size) {
                if (row[colIdx] == number) {
                    row[colIdx] = null
                }
            }
        }
    }

    fun hasRowOfNulls(): Boolean {
        for (row in rows) {
            if (row.all { it == null }) {
                return true
            }
        }
        return false
    }

    fun getColumn(colIdx: Int): List<Int?> {
        var column = mutableListOf<Int?>()
        for (row in rows) {
            column.add(row[colIdx])
        }
        return column
    }

    fun hasColumnOfNulls(): Boolean {
        val nrOfColumns = rows[0].size
        for (colIdx in 0 until nrOfColumns) {
            val column = getColumn(colIdx)
            if (column.all { it == null}) {
                return true
            }
        }
        return false
    }

    fun sum(): Int {
        var s = 0
        rows.forEach {
            s += it
                .filter { element -> element != null }
                .sumOf { number -> number!! }
        }
        return s
    }

    fun print() {
        println()
        println("Board:")
        for (row in rows) {
            for (col in row) {
                print("[${col}]")
            }
            println()
        }
    }
}