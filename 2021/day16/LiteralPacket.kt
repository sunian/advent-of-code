package advent2021.day16

data class LiteralPacket(
    val basePacket: BasePacket,
    val literalValue: Long
) : Packet {
    override val version: Int = basePacket.version
    override val type: PacketType = basePacket.type

    override fun computeValue(): Long = literalValue
    override fun <T> reduce(initial: T, operation: (acc: T, Packet) -> T): T =
        operation(initial, this)

    companion object {
        fun parse(basePacket: BasePacket, input: InputBuffer): Packet = LiteralPacket(
            basePacket = basePacket,
            literalValue = input.readLiteral()
        )
    }
}