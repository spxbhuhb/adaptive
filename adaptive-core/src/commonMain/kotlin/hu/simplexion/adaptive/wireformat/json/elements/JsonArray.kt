/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.json.elements

class JsonArray : JsonElement() {

    val items = mutableListOf<JsonElement>()

    override fun toString(): String {
        return "[${items.joinToString(",") { it.toString() }}]"
    }
}