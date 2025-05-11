/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.json.elements

import `fun`.adaptive.wireformat.json.visitor.JsonTransformer
import `fun`.adaptive.wireformat.json.visitor.JsonVisitor

class JsonArray : JsonElement() {

    val value = mutableListOf<JsonElement>()

    override fun toString(): String {
        return "[${this@JsonArray.value.joinToString(",") { it.toString() }}]"
    }

    override fun <R, D> accept(visitor: JsonVisitor<R, D>, data: D): R {
        return visitor.visitArray(this, data)
    }

    override fun <D> transform(transformer: JsonTransformer<D>, data: D): JsonElement {
        return transformer.visitArray(this, data)
    }

    override fun <D> acceptChildren(visitor: JsonVisitor<Unit, D>, data: D) {
        for (item in this@JsonArray.value) {
            item.accept(visitor, data)
        }
    }

    override fun <D> transformChildren(transformer: JsonTransformer<D>, data: D) {
        for (index in this@JsonArray.value.indices) {
            this@JsonArray.value[index] = this@JsonArray.value[index].transform(transformer, data)
        }
    }

}