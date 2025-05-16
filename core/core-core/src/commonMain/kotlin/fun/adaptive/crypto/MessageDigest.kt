package `fun`.adaptive.crypto

abstract class MessageDigest {
    abstract fun update(input: ByteArray)
    abstract fun update(input: ByteArray, offset: Int, length: Int)
    abstract fun digest(): ByteArray
}