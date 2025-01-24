/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.json.elements

import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.json.JsonWireFormatEncoder
import `fun`.adaptive.wireformat.json.formatting.JsonFormat

open class JsonElement {

    open val asUnit: Unit
        get() = throw IllegalStateException("JsonElement.asUnit")

    open val asBoolean: Boolean
        get() = throw IllegalStateException("JsonElement.asBoolean")

    open val asInt: Int
        get() = throw IllegalStateException("JsonElement.asInt")

    open val asShort: Short
        get() = throw IllegalStateException("JsonElement.asShort")

    open val asByte: Byte
        get() = throw IllegalStateException("JsonElement.asByte")

    open val asLong: Long
        get() = throw IllegalStateException("JsonElement.asLong")

    open val asFloat: Float
        get() = throw IllegalStateException("JsonElement.asFloat")

    open val asDouble: Double
        get() = throw IllegalStateException("JsonElement.asDouble")

    open val asChar: Char
        get() = throw IllegalStateException("JsonElement.asChar")

    open val asString: String
        get() = throw IllegalStateException("JsonElement.asString")

    open fun <T> asUuid(): UUID<T> {
        throw IllegalStateException("JsonElement.asUuid")
    }

    open val asUInt: UInt
        get() = throw IllegalStateException("JsonElement.asUInt")

    open val asUShort: UShort
        get() = throw IllegalStateException("JsonElement.asUShort")

    open val asUByte: UByte
        get() = throw IllegalStateException("JsonElement.asUByte")

    open val asULong: ULong
        get() = throw IllegalStateException("JsonElement.asULong")

    val asPrettyString: String
        get() = JsonFormat().format(this)

    fun toPrettyString(format: JsonFormat = JsonFormat()) = format.format(this)

}