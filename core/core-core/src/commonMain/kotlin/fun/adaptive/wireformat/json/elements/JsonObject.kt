/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.json.elements

class JsonObject : JsonElement() {
    val entries = mutableMapOf<String, JsonElement>()

    override fun toString(): String {
        return "{${entries.entries.joinToString(",") { "\"${it.key}\": ${it.value}" }}}"
    }
}