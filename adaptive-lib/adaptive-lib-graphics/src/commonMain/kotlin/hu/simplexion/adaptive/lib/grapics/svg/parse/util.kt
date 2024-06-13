/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.grapics.svg.parse

const val FLOAT_CHARS = "0123456789.eE-"

internal inline fun List<StringBuilder>.toFloat(index: Int): Float =
    this[index].toString().toFloat()

internal inline fun List<StringBuilder>.toInt(index: Int): Int =
    this[index].toString().toInt()

private val COMMA_WSP = Regex("[\\s,]")

fun String.toFloats(): List<Float> =
    split(COMMA_WSP).map { it.trim().toFloat() }
