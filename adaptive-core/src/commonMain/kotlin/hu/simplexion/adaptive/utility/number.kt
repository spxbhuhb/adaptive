/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.utility

/**
 * Convert an int into a byte array (4 bytes).
 */
fun Int.toByteArray(): ByteArray = ByteArray(4).also { this.encodeInto(it) }

/**
 * Convert an int to bytes and write those bytes into [target] starting
 * from [offset].
 */
fun Int.encodeInto(target: ByteArray, offset: Int = 0) {
    for (i in 3 downTo 0) {
        target[offset + i] = (this shr (8 * (3 - i))).toByte()
    }
}

/**
 * Convert a long into a byte array (8 bytes).
 */
fun Long.toByteArray(): ByteArray = ByteArray(8).also { this.encodeInto(it) }

/**
 * Convert a long to bytes and write those bytes into [target] starting
 * from [offset].
 */
fun Long.encodeInto(target: ByteArray, offset: Int = 0) {
    for (i in 7 downTo 0) {
        target[offset + i] = (this shr (8 * (7 - i))).toByte()
    }
}

/**
 * Read a long from the byte array.
 */
fun ByteArray.toLong(offset: Int = 0): Long {
    var value: Long = 0L
    for (i in 0 until 8) {
        value = (value shl 8) or (this[offset + i].toLong() and 0xFF)
    }
    return value
}