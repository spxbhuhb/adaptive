@file:OptIn(ExperimentalUnsignedTypes::class)

package `fun`.adaptive.lib.util.bytearray

val crc32Table by lazy { generateCrc32Table() }

fun calculateCrc32(data: ByteArray): UInt {
    var crc32 = 0xffffffffU
    val table = crc32Table

    for (byte in data) {
        val crcIndex = byte.toUInt().xor(crc32).toUByte()
        crc32 = table[crcIndex.toInt()].xor(crc32.shr(8))
    }

    return crc32.xor(0xffffffffU)
}

private fun generateCrc32Table(): UIntArray {
    val table = UIntArray(256)

    for (idx in table.indices) {
        table[idx] = idx.toUInt()
        for (bit in 8 downTo 1) {
            table[idx] = if (table[idx] % 2U == 0U) {
                table[idx].shr(1)
            } else {
                table[idx].shr(1).xor(0xEDB88320U)
            }
        }
    }

    return table
}