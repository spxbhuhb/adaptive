package `fun`.adaptive.crypto

actual fun sha256(): MessageDigest = MessageDigestImpl("SHA-256")

private class MessageDigestImpl(
    algorithm : String
) : MessageDigest() {
    private val impl = java.security.MessageDigest.getInstance(algorithm)

    override fun update(input: ByteArray) {
        impl.update(input)
    }

    override fun update(input: ByteArray, offset: Int, length: Int) {
        impl.update(input, offset, length)
    }

    override fun digest(): ByteArray {
        return impl.digest()
    }
}