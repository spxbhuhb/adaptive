package hu.simplexion.z2.serialization.json

import hu.simplexion.z2.serialization.json.elements.*

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

    private val stack = mutableListOf<JsonElement?>()
    private var current: JsonElement? = null

    private var valueExpected = true
    private var fieldName: String? = null

    private var currentPosition = 0

    fun read(): JsonElement {

        while (currentPosition < json.length) {
            val char = json[currentPosition]

            when (char) {

                // -- object --

                '{' -> {
                    require(valueExpected) { "unexpected value at $currentPosition" }
                    stack += current
                    current = JsonObject()

                    valueExpected = false // object open must be followed by a field name or object close
                }

                '}' -> {
                    require(current is JsonObject && fieldName == null) { "unexpected close object at $currentPosition" }
                    current = stack.removeLast()
                    valueExpected = false  // object close must be followed by another close or a separator
                }

                // -- array --

                '[' -> {
                    require(valueExpected) { "unexpected value at $currentPosition" }
                    stack += current
                    current = JsonArray()
                    valueExpected = true // array open must be followed by a value or array close
                }

                ']' -> {
                    require(current is JsonArray) { "unexpected close array at $currentPosition" }
                    current = stack.removeLast()
                    valueExpected = false // array close must be followed by another close or a separator
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
                    while (currentPosition + 1 < json.length && json[currentPosition + 1].isDigitOrDecimal()) {
                        value.append(json[++ currentPosition])
                    }

                    JsonNumber(value.toString()).append()
                    valueExpected = false
                }

                '\"' -> {
                    val value = StringBuilder()
                    value.append(char)
                    while (currentPosition + 1 < json.length) {
                        val nextChar = json[++ currentPosition]
                        value.append(nextChar)
                        if (nextChar == '\"' && value[value.length - 2] != '\\') {
                            break
                        }
                    }

                    if (valueExpected) {
                        JsonString(value.toString()).append()
                        fieldName = null
                        valueExpected = false
                    } else {
                        fieldName = value.toString()
                        valueExpected = false
                    }
                }

                't', 'f', 'n' -> {
                    val value = StringBuilder()
                    value.append(char)
                    while (currentPosition + 1 < json.length) {
                        val nextChar = json[++ currentPosition]
                        value.append(nextChar)
                        if (nextChar < 'a' || nextChar > 'z') {
                            break
                        }
                    }

                    when (value.toString()) {
                        "true" -> JsonBoolean(true)
                        "false" -> JsonBoolean(false)
                        "null" -> JsonNull()
                        else -> throw IllegalArgumentException("unexpected value: $value")
                    }.append()

                    fieldName = null
                    valueExpected = false
                }

                else -> {
                    if (char.isWhitespace()) {
                        // Ignore whitespace characters
                        currentPosition ++
                    } else {
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
                c.items += this
                valueExpected = false
            }

            c is JsonObject -> {
                c.entries[requireNotNull(fieldName) { "missing field name at $currentPosition" }] = this
                fieldName = null
                valueExpected = false
            }

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

}