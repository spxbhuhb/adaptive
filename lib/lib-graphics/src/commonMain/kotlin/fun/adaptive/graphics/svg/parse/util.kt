/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg.parse

const val DOUBLE_CHARS = "0123456789.eE-"

internal fun List<StringBuilder>.toDouble(index: Int): Double =
    this[index].toString().toDouble()

internal fun List<StringBuilder>.toInt(index: Int): Int =
    this[index].toString().toInt()

private val COMMA_WSP = Regex("[\\s,]")

fun String.toDoubles(): List<Double> =
    split(COMMA_WSP).map { it.trim().toDouble() }
