package hu.simplexion.z2.wireformat.json

import hu.simplexion.z2.wireformat.json.elements.*

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
                        if (nextChar in '0' .. '9' || nextChar == '.' || nextChar == 'E' || nextChar == 'e' || nextChar == '-') {
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
                    val value = StringBuilder()

                    while (currentPosition + 1 < json.length) {
                        val nextChar = json[++ currentPosition]
                        if (nextChar == '\"' && value[value.length - 1] != '\\') {
                            break
                        } else {
                            value.append(nextChar)
                        }
                    }

                    if (valueExpected) {
                        JsonString(value.toString()).append()
                        fieldName = null
                    } else {
                        fieldName = value.toString()
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

}