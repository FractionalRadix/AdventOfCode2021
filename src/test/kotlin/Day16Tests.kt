import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class Day16Tests {
    private val inputList = listOf(
        ""
    )

    @Test
    fun testParseInputDay16() {
        val parsedInput = parseInputDay16(listOf("D2FE28"))
        val bits = "110100101111111000101000"
            .map { ch -> if (ch=='1') true else false }
        assertContentEquals(bits, parsedInput)
    }

    @Test
    fun testSumVersionNumbers1() {
        assertEquals(6, sumVersionNumbers("D2FE28"))
    }

    //@Test
    //fun testSumVersionNumbers2() {
    //    assertEquals(1, sumVersionNumbers("38006F45291200"))
    //}

    @Test
    fun testSumVersionNumbers3() {
        assertEquals(16, sumVersionNumbers("8A004A801A8002F478"))
    }

    @Test
    fun testSumVersionNumbers4() {
        assertEquals(12, sumVersionNumbers("620080001611562C8802118E34"))
    }

    @Test
    fun testSumVersionNumbers5() {
        assertEquals(23, sumVersionNumbers("C0015000016115A2E0802F182340"))
    }

    @Test
    fun testSumVersionNumbers6() {
        assertEquals(31, sumVersionNumbers("A0016C880162017C3686B18A3D4780"))
    }

    @Test
    fun testEvaluateExpression() {

        assertEquals(3, evaluatePackets("C200B40A82")) // finds the sum of 1 and 2, resulting in the value 3.
        assertEquals(54, evaluatePackets("04005AC33890")) // finds the product of 6 and 9, resulting in the value 54.
        assertEquals(7, evaluatePackets("880086C3E88112")) // finds the minimum of 7, 8, and 9, resulting in the value 7.
        assertEquals(9, evaluatePackets("CE00C43D881120")) // finds the maximum of 7, 8, and 9, resulting in the value 9.
        assertEquals(1, evaluatePackets("D8005AC2A8F0")) // produces 1, because 5 is less than 15.
        assertEquals(0, evaluatePackets("F600BC2D8F")) // produces 0, because 5 is not greater than 15.
        assertEquals(0, evaluatePackets("9C005AC2F8F0")) // produces 0, because 5 is not equal to 15.
        assertEquals(1, evaluatePackets("9C0141080250320F1802104A08")) // produces 1, because 1 + 3 = 2 * 2.

    }
}