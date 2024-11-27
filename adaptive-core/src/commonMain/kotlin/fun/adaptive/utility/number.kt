/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.utility

import kotlin.jvm.JvmName
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.round

/**
 * Convert an int into a byte array (4 bytes).
 */
fun Int.toByteArray(): ByteArray = ByteArray(4).also { this.encodeInto(it) }

val Int.p02
    get() = this.toString().padStart(2, '0')

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

const val maxDecimals = 10
val shifts = Array(maxDecimals) { idx -> 10.0.pow(idx) }

fun format(
    double: Double,
    decimals: Int = 1,
    decimalSeparator: String = ".",
    thousandSeparator: String? = null,
    hideZeroDecimals: Boolean = false,
) = double.format(decimals, decimalSeparator, thousandSeparator, hideZeroDecimals)

@JvmName("formatDouble")
fun Double.format(
    decimals: Int = 1,
    decimalSeparator: String = ".",
    thousandSeparator: String? = null,
    hideZeroDecimals: Boolean = false,
): String {
    check(decimals < maxDecimals) { "decimals must to be less than $maxDecimals" }

    return when {
        isNaN() -> "NaN"

        isInfinite() -> if (this < 0) "-Inf" else "+Inf"

        decimals == 0 -> {
            round(this).toString().substringBefore('.').withSeparators(".").let {
                if (it == "-0") "0" else it
            }
        }

        else -> {
            val shifted = abs(round(this * shifts[decimals]))
            if (shifted > Int.MAX_VALUE) return this.toString()

            val s = shifted.toInt().toString().padStart(decimals + 1, '0')
            val cutAt = s.length - decimals

            val sign = if (this < 0.0) "-" else ""

            val integralString = s.substring(0, cutAt)
                .let { if (thousandSeparator != null) it.withSeparators(thousandSeparator) else it }

            val decimalString = s.substring(cutAt).let {
                if (hideZeroDecimals && it.toLong() == 0L) "" else decimalSeparator + it
            }

            return sign + integralString + decimalString
        }
    }
}

fun String.withSeparators(separator: String) =
    reversed()
        .chunked(3)
        .joinToString(separator)
        .reversed()