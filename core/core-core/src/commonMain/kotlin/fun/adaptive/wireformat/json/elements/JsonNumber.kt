/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.json.elements

import `fun`.adaptive.wireformat.json.visitor.JsonTransformer
import `fun`.adaptive.wireformat.json.visitor.JsonVisitor

class JsonNumber(val value: String) : JsonElement() {

    constructor(value: Number) : this(
        value.toString()
    )

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

    override fun <R, D> accept(visitor: JsonVisitor<R, D>, data: D): R {
        return visitor.visitNumber(this, data)
    }

    override fun <D> transform(transformer: JsonTransformer<D>, data: D): JsonElement {
        return transformer.visitNumber(this, data)
    }

    override fun toString(): String {
        return value
    }
}