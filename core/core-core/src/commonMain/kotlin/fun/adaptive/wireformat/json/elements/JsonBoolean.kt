/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.json.elements

import `fun`.adaptive.wireformat.json.visitor.JsonTransformer
import `fun`.adaptive.wireformat.json.visitor.JsonVisitor

class JsonBoolean(
    val value: Boolean
) : JsonElement() {

    override val asBoolean: Boolean
        get() = value

    override val asUnit: Unit
        get() = check(value) { "false value as Unit" }

    override fun <R, D> accept(visitor: JsonVisitor<R, D>, data: D): R {
        return visitor.visitBoolean(this, data)
    }

    override fun <D> transform(transformer: JsonTransformer<D>, data: D): JsonElement {
        return transformer.visitBoolean(this, data)
    }

    override fun toString(): String {
        return value.toString()
    }
}