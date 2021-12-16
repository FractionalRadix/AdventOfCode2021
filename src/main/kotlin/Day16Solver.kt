import kotlin.io.path.Path
import kotlin.io.path.readLines

fun solveDay16() {
    val inputList = Path("""inputFiles\AoCDay16.txt""").readLines()
    val bits = parseInputDay16(inputList)
    println("Sum of version numbers: ${sumVersionNumbers(inputList[0])}")   // 895
    println("Message sent via the BITS protocol: ${evaluatePackets(inputList[0])}") // 1148595959144
}

fun parseInputDay16(inputList: List<String>): Iterable<Boolean> {
    val message = inputList[0]

    return hexStringToIntList(message)
}

private fun hexStringToIntList(message: String) = message
    .toCharArray()
    .joinToString(separator = "") { char -> "$char".toInt(16).toString(2).padStart(4, '0') }
    .map { char -> char == '1' }

fun Boolean.toInt() = if (this) 1 else 0
fun bitsToInt(bits: List<Boolean>) = bits.fold(0) { total, item -> 2 * total + item.toInt() }

var index = 0
fun process(stream: Iterable<Boolean>): Packet {
    val version = bitsToInt(stream.drop(index).take(3))
    index += 3
    val typeId = bitsToInt(stream.drop(index).take(3))
    index += 3

    if (typeId == 4) { // Literal value
        return parseLiteralValuePacket(stream, version)
    } else {
        // Operator
        val packet = OperatorPacket(version, typeId)
        val lengthTypeId = stream.drop(index).first()
        index++
        if (lengthTypeId) {
            return parseOperatorPacketFixedImmediateSubpackets(stream, packet)
        } else {
            return parseOperatorPacketFixedLength(stream, packet)
        }
    }
}

private fun parseOperatorPacketFixedImmediateSubpackets(
    stream: Iterable<Boolean>,
    packet: OperatorPacket
): OperatorPacket {
    // The next 11 bits are the number of sub-packets.
    val nrOfSubPackets = bitsToInt(stream.drop(index).take(11))
    index += 11
    for (i in 0 until nrOfSubPackets) {
        val subPacket = process(stream)
        packet.addPacket(subPacket)
    }
    return packet
}

private fun parseOperatorPacketFixedLength(
    stream: Iterable<Boolean>,
    packet: OperatorPacket
): OperatorPacket {
    // The next 15 bits are the total length, in bits, of sub-packets.
    val subPacketLength = bitsToInt(stream.drop(index).take(15))
    index += 15
    val lastPosition = index + subPacketLength
    while (index < lastPosition) {
        val subPacket = process(stream)
        packet.addPacket(subPacket)
    }
    return packet
}

private fun parseLiteralValuePacket(
    stream: Iterable<Boolean>,
    version: Int
): LiteralPacket {
    var literalValue = 0L
    do {
        val header = stream.drop(index).first()
        index++
        val number = bitsToInt(stream.drop(index).take(4))
        index += 4
        literalValue = 16 * literalValue + number
    } while (header)
    return LiteralPacket(version, literalValue)
}

abstract class Packet(val version: Int) {
    abstract fun summedVersion(): Int
    abstract fun print(spacing: Int)
    abstract fun eval(): Long
}

class LiteralPacket(version: Int, val value: Long): Packet(version) {
    override fun summedVersion(): Int {
        return version
    }

    override fun print(spacing: Int) {
        println("${" ".repeat(spacing)}Literal value: $value")
    }

    override fun eval() = value
}

class OperatorPacket(version: Int, val typeId: Int): Packet(version) {
    private val subPackets = mutableListOf<Packet>()

    fun addPacket(packet: Packet) {
        subPackets.add(packet)
    }

    override fun summedVersion(): Int {
        var result = version
        for (subPacket in subPackets) {
            result += subPacket.summedVersion()
        }
        return result
    }

    override fun print(spacing: Int) {
        print("${" ".repeat(spacing)}")
        for (subPacket in subPackets) {
            subPacket.print(spacing + 2)
        }
    }

    override fun eval(): Long {
        val list = subPackets.map { it.eval() }
        when(typeId) {
            0 -> return list.sum()
            1 -> return list.product()
            2 -> return list.minOrNull()!!
            3 -> return list.maxOrNull()!!
            5 -> {
                val firstValue = list[0]
                val secondValue = list[1]
                return if (firstValue > secondValue) 1 else 0
            }
            6 -> {
                val firstValue = list[0]
                val secondValue = list[1]
                return if (firstValue < secondValue) 1 else 0
            }
            7 -> {
                val firstValue = list[0]
                val secondValue = list[1]
                return if (firstValue == secondValue) 1 else 0
            }
            else -> {
                println("Oops! Unknown operator type: $typeId !!")
                return 30000
            }
        }
    }
}

//TODO!~ Use a fold...
fun List<Long>.product(): Long {
    var product = 1L
    for (value in this) {
        product *= value
    }
    return product
}

fun sumVersionNumbers(message: String): Int {
    index = 0
    val packet = process(hexStringToIntList(message))
    packet.print(3)
    return packet.summedVersion()
}

fun evaluatePackets(message:String): Long {
    index = 0
    val packet = process(hexStringToIntList(message))
    packet.print(3)
    return packet.eval()
}