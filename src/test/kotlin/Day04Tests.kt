import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class Day04Tests {

    private var boards = listOf<BingoBoard>()
    private var draws = listOf<Int>()

    @Before
    fun init() {
        val inputList = listOf("7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1",
            "",
            "22 13 17 11  0",
            "8  2 23  4 24",
            "21  9 14 16  7",
            "6 10  3 18  5",
            "1 12 20 15 19",

            "3 15  0  2 22",
            "9 18 13 17  5",
            "19  8  7 25 23",
            "20 11 10 24  4",
            "14 21 16 12  6",

            "14 21 17 24  4",
            "10 16 15  9 19",
            "18  8 23 26 20",
            "22 11 13  6  5",
            "2  0 12  3  7"
        )

        draws = inputList[0].split(',').map { it.toInt() }
        boards = parseBoards(inputList.drop(2))
    }

    @Test
    fun testWinningBoard() {
        var sumOfWinningBoard = 0
        for (draw in draws) {
            //println("DRAW: $draw")
            for (board in boards) {
                board.draw(draw)
                if (board.hasColumnOfNulls() || board.hasRowOfNulls()) {
                    //board.print()
                    //print(board.sum())
                    sumOfWinningBoard = board.sum()
                    break
                }
            }
            if (sumOfWinningBoard > 0)
                break
        }

        assertEquals(188, sumOfWinningBoard)
    }

    @Test
    fun testLoosingBoard() {
        var sumOfLoosingBoard = 0
        for (draw in draws) {
            for (board in boards) {
                board.draw(draw)
            }
            // Filter out the boards that won this round, unless there is only one board remaining.
            if (boards.size > 1) {
                boards = boards.filter { !it.hasColumnOfNulls() && !it.hasRowOfNulls() }
            }
            // If only one board remains, keep playing until it has a row or column.
            if (boards.size == 1) {
                if (boards[0].hasRowOfNulls() || boards[0].hasColumnOfNulls()) {
                    sumOfLoosingBoard = boards[0].sum()
                    break
                }
            }
        }
        assertEquals(148, sumOfLoosingBoard)
    }
}