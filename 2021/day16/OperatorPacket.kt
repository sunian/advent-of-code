package advent2021.day16

data class OperatorPacket(
    val basePacket: BasePacket,
    val subPackets: List<Packet>
) : Packet {
    override val version: Int = basePacket.version
    override val type: PacketType = basePacket.type

    override fun <T> reduce(initial: T, operation: (acc: T, Packet) -> T): T {
        var acc = operation(initial, this)
        subPackets.forEach { acc = it.reduce(acc, operation) }
        return acc
    }

    override fun computeValue(): Long = when (type) {
        PacketType.SUM -> subPackets.sumOf { it.computeValue() }
        PacketType.PRODUCT -> subPackets.map { it.computeValue() }.reduce { acc, num -> acc * num }
        PacketType.MIN -> subPackets.minOf { it.computeValue() }
        PacketType.MAX -> subPackets.maxOf { it.computeValue() }
        PacketType.GREATER_THAN -> when {
            subPackets[0].computeValue() > subPackets[1].computeValue() -> 1
            else -> 0
        }
        PacketType.LESS_THAN -> when {
            subPackets[0].computeValue() < subPackets[1].computeValue() -> 1
            else -> 0
        }
        PacketType.EQUAL_TO -> when {
            subPackets[0].computeValue() == subPackets[1].computeValue() -> 1
            else -> 0
        }
        else -> throw IllegalStateException("illegal type $type")
    }

    companion object {
        fun parse(basePacket: BasePacket, input: InputBuffer): Packet = OperatorPacket(
            basePacket = basePacket,
            subPackets = parseSubPackets(input)
        )

        private fun parseSubPackets(input: InputBuffer): List<Packet> {
            val subPackets = arrayListOf<Packet>()
            when (LengthType.values()[input.readBits(1)]) {
                LengthType.TOTAL_15BITS -> {
                    val subPacketBits = input.readBits(15)
                    val subPacketStart = input.position
                    while (input.position < subPacketStart + subPacketBits) {
                        subPackets.add(Packet.parse(input))
                    }
                }
                LengthType.COUNT_11BITS -> {
                    val numSubPackets = input.readBits(11)
                    repeat(numSubPackets) {
                        subPackets.add(Packet.parse(input))
                    }
                }
            }
            return subPackets
        }
    }
}