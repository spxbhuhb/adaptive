package hu.simplexion.z2.wireformat.json

import hu.simplexion.z2.utility.UUID
import hu.simplexion.z2.wireformat.buffer.BufferWriter

/**
 * A low level JSON writer to write information into a list of ByteArrays
 * in JSON format. The list grows as more space needed up until the maximum
 * size set in the `maximumBufferSize` property of the companion object.
 *
 * @property  initialBufferSize     Size of the first buffer.
 * @property  additionalBufferSize  Size of the buffers that are added on-demand.
 * @property  maximumBufferSize     Maximum total size of buffers. The writer throws an exception if
 *                                  this is not enough.
 */
class JsonBufferWriter(
    initialBufferSize: Int = 200,
    additionalBufferSize: Int = 10_000,
    maximumBufferSize: Int = 5_000_000 + initialBufferSize
) : BufferWriter(initialBufferSize, additionalBufferSize, maximumBufferSize) {

    var level = 0

    // ------------------------------------------------------------------------
    // Public interface
    // ------------------------------------------------------------------------

    fun bool(fieldName: String, value: Boolean?) {
        fieldName(fieldName)
        if (value == null) put(NULL) else bool(value)
        separator()
    }

    fun number(fieldName: String, value: Number?) {
        fieldName(fieldName)
        put(value?.toString())
        separator()
    }

    fun string(fieldName: String, value: String?) {
        fieldName(fieldName)
        quotedString(value)
        separator()
    }

    fun uuid(fieldName: String, value: UUID<*>?) {
        fieldName(fieldName)
        quotedString(value?.toString())
        separator()
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun bytes(fieldName: String, value: ByteArray?) {
        fieldName(fieldName)
        quotedString(value?.toHexString())
        separator()
    }

    fun number(fieldName: String, value: UInt?) {
        fieldName(fieldName)
        put(value?.toString())
        separator()
    }

    fun number(fieldName: String, value: UShort?) {
        fieldName(fieldName)
        put(value?.toString())
        separator()
    }

    fun number(fieldName: String, value: UByte?) {
        fieldName(fieldName)
        put(value?.toString())
        separator()
    }

    fun number(fieldName: String, value: ULong?) {
        fieldName(fieldName)
        put(value?.toString())
        separator()
    }

    // ------------------------------------------------------------------------
    // JSON primitives
    // ------------------------------------------------------------------------

    fun fieldName(fieldName: String) {
        quotedString(fieldName)
        put(0x3a) // ':'
    }

    fun nullValue(fieldName: String) {
        fieldName(fieldName)
        nullValue()
        separator()
    }

    fun openArray() {
        put(0x5b) // '['
        level++
    }

    fun closeArray() {
        if (peekLast() == 0x2c.toByte()) rollback()
        put(0x5d) // ']'
        level--
        separator()
    }

    fun openObject() {
        put(0x7b) // '}'
        level++
    }

    fun closeObject() {
        if (peekLast() == 0x2c.toByte()) rollback()
        put(0x7d) // '}'
        level--
        separator()
    }

    fun separator() {
        if (level > 0) put(0x2c) // ','
    }

    // ------------------------------------------------------------------------
    // Functions without field name use for arrays only
    // ------------------------------------------------------------------------

    internal fun bool(value: Boolean) {
        if (value) string("true") else string("false")
    }

    internal fun number(value: Number) {
        put(value.toString())
    }

    internal fun string(value: String?) {
        put(value?.encodeToByteArray() ?: NULL)
    }

    internal fun uuid(value: UUID<*>) {
        quotedString(value.toString())
    }

    @OptIn(ExperimentalStdlibApi::class)
    internal fun bytes(value: ByteArray) {
        quotedString(value.toHexString())
    }

    fun nullValue() {
        put(NULL)
    }

    internal fun number(value: UInt) {
        put(value.toString())
    }

    internal fun number(value: UShort) {
        put(value.toString())
    }

    internal fun number(value: UByte) {
        put(value.toString())
    }

    internal fun number(value: ULong) {
        put(value.toString())
    }

    // ------------------------------------------------------------------------
    // Wire format writers
    // ------------------------------------------------------------------------

    fun quotedString(value: String?) {
        if (value == null) {
            put(NULL)
        } else {
            put(0x22) // '"'
            putEscaped(value)
            put(0x22) // '"'
        }
    }

    private val NULL = "null".encodeToByteArray()

    private fun put(value: String?) {
        put(value?.encodeToByteArray() ?: NULL)
    }

    private fun putEscaped(input: String) {
        for (char in input) {
            when (char) {
                '\"' -> {
                    put(0x5c); put(0x22)
                }

                '\\' -> {
                    put(0x5c); put(0x5c)
                }

                '\n' -> {
                    put(0x5c); put(110)
                }

                '\r' -> {
                    put(0x5c); put(114)
                }

                '\t' -> {
                    put(0x5c); put(116)
                }

                else -> {
                    when {
                        char < ' ' -> put(char.escape())
                        char.code < 128 -> put(char.code.toByte())
                        char.isLetterOrDigit() -> put(char.toString())
                        else -> put(char.escape())
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    // TODO us a hex encode with better performance
    fun Char.escape() = "\\u" + code.toHexString().padStart(4, '0').takeLast(4)

}