package advent2021.day16

enum class PacketType(val id: Int) {
    LITERAL(4),
    SUM(0),
    PRODUCT(1),
    MIN(2),
    MAX(3),
    GREATER_THAN(5),
    LESS_THAN(6),
    EQUAL_TO(7),
    ;

    companion object {
        fun getType(id: Int): PacketType =
            values().firstOrNull { it.id == id }
                ?: LITERAL
    }
}