/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.json.elements

class JsonNumber(val value: String) : JsonElement {

    override val asInt
        get() = value.toInt()

    override val asShort
        get() = value.toShort()

    override val asByte
        get() = value.toByte()

    override val asLong
        get() = value.toLong()

    override val asFloat
        get() = value.toFloat()

    override val asDouble
        get() = value.toDouble()

    override val asUInt
        get() = value.toUInt()

    override val asUShort
        get() = value.toUShort()

    override val asUByte
        get() = value.toUByte()

    override val asULong
        get() = value.toULong()

}