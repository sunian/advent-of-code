package advent2021.day16

interface Packet {
    val version: Int
    val type: PacketType

    fun computeValue(): Long
    fun <T> reduce(initial: T, operation: (acc: T, Packet) -> T): T

    companion object {
        fun parse(input: InputBuffer): Packet {
            val basePacket = BasePacket(
                version = input.readBits(3),
                type = PacketType.getType(input.readBits(3))
            )
            return when (basePacket.type) {
                PacketType.LITERAL -> LiteralPacket.parse(basePacket, input)
                PacketType.SUM,
                PacketType.PRODUCT,
                PacketType.MIN,
                PacketType.MAX,
                PacketType.GREATER_THAN,
                PacketType.LESS_THAN,
                PacketType.EQUAL_TO -> OperatorPacket.parse(basePacket, input)
            }
        }
    }
}

data class BasePacket(
    val version: Int,
    val type: PacketType,
)