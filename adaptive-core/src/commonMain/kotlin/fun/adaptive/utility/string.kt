/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.utility

/**
 * Convert a [ByteArray] into a string, replacing all non-printable and non-ASCII
 * characters with a dot.
 *
 * @param  limit  Maximum number of characters in the return value. Might be less
 *                if the array is smaller.
 */
fun ByteArray.toDotString(limit: Int = this.size, offset: Int = 0): String {

    val end = kotlin.math.min(offset + limit, this.size)

    val chars = CharArray(end - offset)
    var charIndex = 0

    for (i in offset until end) {
        val byte = this[i].toInt()
        if (byte < 0x20 || byte > 0x7f) {
            chars[charIndex ++] = '.'
        } else {
            chars[charIndex ++] = Char(byte)
        }
    }

    return chars.concatToString()
}

/**
 * Find the first character for which [condition] is false.
 *
 * @return  [end] if no such character is in the remaining part of the string
 *          the index of the character if there is such character
 */
inline fun String.firstNot(start: Int, end: Int, condition: (it: Char) -> Boolean): Int {
    var index = start
    while (index < end && condition(this[index])) index ++
    return if (index < end) index else end
}

/**
 * Find the first character for which [condition] is false.
 *
 * @return  null if no such character is in the remaining part of the string
 *          the index of the character if there is such character
 */
inline fun String.firstNotOrNull(start: Int, end: Int, condition: (it: Char) -> Boolean): Int? {
    var index = start
    while (index < end && condition(this[index])) index ++
    return if (index < end) index else null
}

/**
 * Find the first character for which [condition] is true.
 *
 * @return  null if no such character is in the remaining part of the string
 *          the index of the character if there is such character
 */
inline fun String.firstOrNull(start: Int, end: Int, condition: (it: Char) -> Boolean): Int? {
    var index = start
    while (index < end && ! condition(this[index])) index ++
    return if (index < end) index else null
}

/**
 * Find the first character for which [condition] is true.
 *
 * @return  [end] if no such character is in the remaining part of the string
 *          the index of the character if there is such character
 */
inline fun String.first(start: Int, end: Int, condition: (it: Char) -> Boolean): Int {
    var index = start
    while (index < end && ! condition(this[index])) index ++
    return if (index < end) index else end
}

/**
 * Find the first **not** [isSpace] character in the string.
 *
 * @return  null if no such character is in the remaining part of the string
 *          the index of the character if there is such character
 */
fun String.firstNotSpaceOrNull(start: Int, end: Int): Int? {
    var index = start
    while (index < end && this[index].isSpace()) index ++
    return if (index < end) index else null
}

/**
 * Find the first **not** [isSpace] character in the string.
 *
 * @return  [end] if no such character is in the remaining part of the string
 *          the index of the character if there is such character
 */
fun String.firstNotSpace(start: Int, end: Int): Int {
    var index = start
    while (index < end && this[index].isSpace()) index ++
    return if (index < end) index else end
}

/**
 * Find the last **not** [isSpace] character in the string **before** the [start] position
 * up until the [until] position (exclusive).
 *
 * @return  [until] if no such character is in the remaining part of the string
 *          the index of the character if there is such character
 */
fun String.firstNotSpaceBefore(start: Int, until: Int): Int {
    var index = start
    while (until < index && this[index].isSpace()) index --
    return if (index > until) index else until
}

/**
 * True when the character:
 *
 * - is in Unicode "Separator,Space" category
 * - is a tab character
 */
fun Char.isSpace() =
    (
        this == ' '
            || this == '\t'
            || this == '\u00A0'
            || this == '\u1680'
            || (this in ' ' .. ' ')
            || this == '\u202F'
            || this == '\u205F'
            || this == '\u3000'
        )

/**
 * True when the character:
 *
 * - is `\n`
 * - is `\r`
 * - is `\u2028`
 * - is `\u2029`
 */
fun Char.isNewLine() =
    (
        this == '\n'
            || this == '\r'
            || this == '\u2028'
            || this == '\u2029'
        )

fun String.encodeToUrl(): String {
    var result = ""
    for (byte in encodeToByteArray()) {
        val unsignedByte = byte.toInt() and 0xFF

        when (unsignedByte) {
            in 0x30 .. 0x39 -> result += unsignedByte.toChar() // 0-9
            in 0x41 .. 0x5A -> result += unsignedByte.toChar()  // A-Z
            in 0x61 .. 0x7A -> result += unsignedByte.toChar() // a-z
            0x20 -> result += '+'
            0x2D -> result += '-' // -
            0x2E -> result += '.' // .
            0x5F -> result += '_' // _
            0x7E -> result += '~'  // ~
            else -> {
                result += "%"
                result += unsignedByte.toString(16).uppercase().padStart(2, '0')
            }
        }
    }
    return result
}

fun String.decodeFromUrl(): String {
    val result = mutableListOf<Byte>()
    var i = 0
    while (i < this.length) {
        when {
            this[i] == '%' -> {
                val hexValue = this.substring(i + 1, i + 3)
                result += hexValue.toInt(16).toByte()
                i += 3
            }

            this[i] == '+' -> {
                result += 0x20
                i ++
            }

            else -> {
                result += this[i].code.toByte()
                i ++
            }
        }
    }
    return result.toByteArray().decodeToString()
}