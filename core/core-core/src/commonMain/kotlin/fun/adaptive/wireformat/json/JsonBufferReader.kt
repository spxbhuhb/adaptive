/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.json

import `fun`.adaptive.wireformat.json.elements.*

/**
 * A low level JSON reader that decodes the JSON wire
 * format into a list of [JsonElement].
 *
 * @property   buffer  Data to read.
 * @property   offset  Offset of the first byte in [buffer] this reader should process.
 * @property   length  Number of bytes available for this reader. There may be more bytes
 *                     in the buffer, but the reader does not read over the length.
 */
class JsonBufferReader(
    private val buffer: ByteArray,
    private val offset: Int = 0,
    private val length: Int = buffer.size
) {

    private val json = buffer.decodeToString()

    private val stack = mutableListOf<Pair<String?, JsonElement>>()
    private var current: JsonElement? = null

    private var valueExpected = true
    private var fieldName: String? = null

    private var currentPosition = 0

    /**
     * Push happens at the start of objects and arrays. For both there is no known current field name
     * at the very beginning.
     */
    private fun push(value: JsonElement) {
        stack += fieldName to value
        current = value
        fieldName = null
    }

    /**
     * Pop happens at the end of objects and arrays. For both
     */
    private fun pop() {
        val entry = stack.removeLast()

        fieldName = entry.first
        current = stack.lastOrNull()?.second

        entry.second.append()

        fieldName = null

        valueExpected = false
    }

    fun read(): JsonElement {

        while (currentPosition < json.length) {
            val char = json[currentPosition]

            when (char) {

                // -- object --

                '{' -> {
                    require(valueExpected) { "unexpected value at $currentPosition" }
                    push(JsonObject())
                    valueExpected = false // object open must be followed by a field name or object close
                }

                '}' -> {
                    require(current is JsonObject && fieldName == null) { "unexpected close object at $currentPosition" }
                    pop()
                }

                // -- array --

                '[' -> {
                    require(valueExpected) { "unexpected value at $currentPosition" }
                    push(JsonArray())
                    valueExpected = true // array open must be followed by a value or array close
                }

                ']' -> {
                    require(current is JsonArray) { "unexpected close array at $currentPosition" }
                    pop()
                    valueExpected = false
                }

                // separator between field name and field value

                ':' -> {
                    requireNotNull(fieldName) { "missing field name at $currentPosition" }
                    valueExpected = true
                }

                // separator between values in an array or in an object name and field value

                ',' -> {
                    require(! valueExpected && fieldName == null)
                    valueExpected = (current is JsonArray)
                }

                // a numeric value

                in '0' .. '9', '-', '.' -> {
                    require(valueExpected) { "unexpected value at $currentPosition" }

                    val value = StringBuilder()
                    value.append(char)
                    while (currentPosition + 1 < json.length) {
                        val nextChar = json[currentPosition + 1]
                        if (nextChar in '0' .. '9' || nextChar == '.' || nextChar == 'E' || nextChar == 'e' || nextChar == '-' || nextChar == '+') {
                            value.append(nextChar)
                            currentPosition ++
                        } else if (nextChar == 'I') {
                            // `-Infinity`, `-` is already in value
                            check(currentPosition + 8 < json.length) { "unfinished Infinity in JSON" }
                            value.append(json.subSequence(currentPosition + 1, currentPosition + 9))
                            currentPosition += 8
                        } else {
                            break
                        }
                    }

                    JsonNumber(value.toString()).append()
                    valueExpected = false
                }

                '\"' -> {
                    val value = readString()

                    if (valueExpected) {
                        JsonString(value).append()
                        fieldName = null
                    } else {
                        fieldName = value
                    }

                    valueExpected = false
                }

                't', 'f', 'n', 'N', 'I' -> {
                    val value = StringBuilder()
                    value.append(char)

                    while (currentPosition + 1 < json.length) {
                        val nextChar = json[currentPosition + 1]
                        if (nextChar in 'a' .. 'z' || nextChar in 'A' .. 'Z') {
                            value.append(nextChar)
                            currentPosition ++
                        } else {
                            break
                        }
                    }

                    when (value.toString()) {
                        "true" -> JsonBoolean(true)
                        "false" -> JsonBoolean(false)
                        "null" -> JsonNull()
                        "NaN" -> JsonNumber("NaN")
                        "nan" -> JsonNumber("NaN")
                        "Infinity" -> JsonNumber("Infinity")
                        else -> throw IllegalArgumentException("unexpected value: $value")
                    }.append()
                }

                else -> {
                    if (! char.isWhitespace()) {
                        throw IllegalArgumentException("invalid character: $char")
                    }
                }
            }
            currentPosition ++
        }

        return requireNotNull(current) { "no value in the buffer" }
    }

    fun JsonElement.append() {
        val c = current // make it immutable for null checks

        when {
            c is JsonArray -> {
                c.value += this
                valueExpected = false
            }

            c is JsonObject -> {
                c.value[requireNotNull(fieldName) { "missing field name at $currentPosition" }] = this
                fieldName = null
                valueExpected = false
            }

            // this is for standalone values like 'null'
            else -> {
                require(current == null) { "unexpected value at $currentPosition" }
                current = this
                valueExpected = false
            }
        }
    }

    private fun Char.isDigitOrDecimal(): Boolean {
        return this.isDigit() || this == '.'
    }

    private fun readString(): String {
        val value = StringBuilder()

        while (currentPosition + 1 < json.length) {
            val char = json[++ currentPosition]

            if (char == '\"') return value.toString()

            if (char != '\\') {
                value.append(char)
                continue
            }

            val next = json[++ currentPosition]

            when (next) {
                '\"' -> {
                    value.append(next)
                }

                '\\' -> {
                    value.append(next)
                }

                'n' -> {
                    value.append('\n')
                }

                'r' -> {
                    value.append('\r')
                }

                't' -> {
                    value.append('\t')
                }

                'u' -> {
                    require(currentPosition + 4 < json.length) { "invalid unicode escape" }
                    val code = json.substring(currentPosition + 1, currentPosition + 5).toInt(16)
                    value.append(code.toChar())
                    currentPosition += 4
                }

                else -> throw IllegalArgumentException("invalid escape sequence: \\$next")
            }
        }

        throw IllegalArgumentException("unterminated string")
    }

}