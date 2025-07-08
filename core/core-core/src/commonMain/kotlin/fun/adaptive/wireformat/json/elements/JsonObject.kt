/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.json.elements

import `fun`.adaptive.wireformat.json.visitor.JsonTransformer
import `fun`.adaptive.wireformat.json.visitor.JsonVisitor

class JsonObject : JsonElement() {

    val value = mutableMapOf<String, JsonElement>()

    operator fun get(key : String) = value[key]

    operator fun set(key : String, value : JsonElement) { this.value[key] = value }

    override fun toString(): String {
        return "{${value.entries.joinToString(",") { "\"${it.key}\": ${it.value}" }}}"
    }

    override fun <R, D> accept(visitor: JsonVisitor<R, D>, data: D): R {
        return visitor.visitObject(this, data)
    }

    override fun <D> transform(transformer: JsonTransformer<D>, data: D): JsonElement {
        return transformer.visitObject(this, data)
    }

    override fun <D> acceptChildren(visitor: JsonVisitor<Unit, D>, data: D) {
        for (entry in value) {
            entry.value.accept(visitor, data)
        }
    }

    override fun <D> transformChildren(transformer: JsonTransformer<D>, data: D) {
        for (entry in value) {
            entry.setValue(entry.value.transform(transformer, data))
        }
    }
}