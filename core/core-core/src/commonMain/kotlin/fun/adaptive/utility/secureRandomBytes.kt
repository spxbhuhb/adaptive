package `fun`.adaptive.utility

/**
 * Generates a securely random ByteArray of the given size.
 * Uses secureRandom to generate enough secure Ints and converts them to bytes.
 */
fun secureRandomBytes(byteCount: Int): ByteArray {
    val intCount = (byteCount + 3) / 4  // ceil division to cover all bytes
    val secureInts = secureRandom(intCount)
    val byteArray = ByteArray(byteCount)

    for (i in 0 until byteCount) {
        val intIndex = i / 4
        val byteIndex = 3 - (i % 4)
        byteArray[i] = ((secureInts[intIndex] shr (byteIndex * 8)) and 0xFF).toByte()
    }

    return byteArray
}