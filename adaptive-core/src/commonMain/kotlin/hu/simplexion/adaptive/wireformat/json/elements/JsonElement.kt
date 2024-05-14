/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.json.elements

import hu.simplexion.adaptive.utility.UUID

open class JsonElement {

    open val asUnit: Unit
        get() = throw IllegalStateException()

    open val asBoolean: Boolean
        get() = throw IllegalStateException()

    open val asInt: Int
        get() = throw IllegalStateException()

    open val asShort: Short
        get() = throw IllegalStateException()

    open val asByte: Byte
        get() = throw IllegalStateException()

    open val asLong: Long
        get() = throw IllegalStateException()

    open val asFloat: Float
        get() = throw IllegalStateException()

    open val asDouble: Double
        get() = throw IllegalStateException()

    open val asChar: Char
        get() = throw IllegalStateException()

    open val asString: String
        get() = throw IllegalStateException()

    open fun <T> asUuid(): UUID<T> {
        throw IllegalStateException()
    }

    open val asUInt: UInt
        get() = throw IllegalStateException()

    open val asUShort: UShort
        get() = throw IllegalStateException()

    open val asUByte: UByte
        get() = throw IllegalStateException()

    open val asULong: ULong
        get() = throw IllegalStateException()
}