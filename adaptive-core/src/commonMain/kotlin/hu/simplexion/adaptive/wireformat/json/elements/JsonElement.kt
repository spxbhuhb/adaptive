/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.json.elements

import hu.simplexion.adaptive.utility.UUID

interface JsonElement {

    val asUnit: Unit
        get() = throw IllegalStateException()

    val asBoolean: Boolean
        get() = throw IllegalStateException()

    val asInt: Int
        get() = throw IllegalStateException()

    val asShort: Short
        get() = throw IllegalStateException()

    val asByte: Byte
        get() = throw IllegalStateException()

    val asLong: Long
        get() = throw IllegalStateException()

    val asFloat: Float
        get() = throw IllegalStateException()

    val asDouble: Double
        get() = throw IllegalStateException()

    val asChar: Char
        get() = throw IllegalStateException()

    val asString: String
        get() = throw IllegalStateException()

    val asByteArray: ByteArray
        get() = throw IllegalStateException()

    fun <T> asUuid(): UUID<T> {
        throw IllegalStateException()
    }

    val asUInt: UInt
        get() = throw IllegalStateException()

    val asUShort: UShort
        get() = throw IllegalStateException()

    val asUByte: UByte
        get() = throw IllegalStateException()

    val asULong: ULong
        get() = throw IllegalStateException()
}