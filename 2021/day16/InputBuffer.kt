package advent2021.day16

class InputBuffer(private val bits: List<Char>) {
    companion object {
        fun parse(hexString: String): InputBuffer {
            if (hexString.length >= Int.MAX_VALUE / 4) {
                throw IllegalArgumentException("input too long")
            }
            return InputBuffer(
                hexString.flatMap { c ->
                    c.toString().toInt(16).toString(2).padStart(4, '0')
                        .toCharArray().toList()
                }
            )
        }
    }

    var position = 0

    fun readBits(length: Int): Int {
        val read = bits.subList(position, position + length).joinToString(separator = "")
        position += length
        return read.toInt(2)
    }

    fun readRaw(length: Int): String {
        val read = bits.subList(position, position + length).joinToString(separator = "")
        position += length
        return read
    }

    fun readLiteral(): Long {
        val sb = StringBuilder()
        while (true) {
            val more = readBits(1) == 1
            sb.append(readRaw(4))
            if (!more) break
        }
        return sb.toString().toLong(2)
    }

    fun skip(length: Int): Int {
        position += length
        return position
    }
}