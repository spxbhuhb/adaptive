/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.json

import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.buffer.BufferWriter

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
@OptIn(ExperimentalStdlibApi::class)
class JsonBufferWriter(
    initialBufferSize: Int = 200,
    additionalBufferSize: Int = 10_000,
    maximumBufferSize: Int = 5_000_000 + initialBufferSize
) : BufferWriter(initialBufferSize, additionalBufferSize, maximumBufferSize) {

    // ------------------------------------------------------------------------
    // Public interface
    // ------------------------------------------------------------------------

    fun bool(fieldName: String, value: Boolean?) {
        fieldName(fieldName)
        if (value == null) put(NULL) else rawBool(value)
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
    // JSON structure
    // ------------------------------------------------------------------------

    fun fieldName(fieldName: String) {
        quotedString(fieldName)
        put(0x3a) // ':'
    }

    fun nullValue(fieldName: String) {
        fieldName(fieldName)
        rawNullValue()
    }

    fun openArray() {
        put(0x5b) // '['
    }

    fun closeArray() {
        if (peekLast() == 0x2c.toByte()) rollback()
        put(0x5d) // ']'
    }

    fun openObject() {
        put(0x7b) // '}'
    }

    fun closeObject() {
        if (peekLast() == 0x2c.toByte()) rollback()
        put(0x7d) // '}'
    }

    fun separator() {
        put(0x2c) // ','
    }

    // ------------------------------------------------------------------------
    // Functions without field name
    // ------------------------------------------------------------------------

    internal fun rawBool(value: Boolean) {
        if (value) rawString("true") else rawString("false")
    }

    internal fun rawNumber(value: Number) {
        put(value.toString())
    }

    internal fun rawString(value: String?) {
        put(value?.encodeToByteArray() ?: NULL)
    }

    internal fun rawUuid(value: UUID<*>) {
        quotedString(value.toString())
    }

    @OptIn(ExperimentalStdlibApi::class)
    internal fun rawBytes(value: ByteArray) {
        quotedString(value.toHexString())
    }

    fun rawNullValue() {
        put(NULL)
    }

    internal fun rawNumber(value: UInt) {
        put(value.toString())
    }

    internal fun rawNumber(value: UShort) {
        put(value.toString())
    }

    internal fun rawNumber(value: UByte) {
        put(value.toString())
    }

    internal fun rawNumber(value: ULong) {
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