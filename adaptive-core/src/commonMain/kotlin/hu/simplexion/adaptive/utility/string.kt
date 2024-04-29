/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.utility

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