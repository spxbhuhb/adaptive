/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.json.elements

import `fun`.adaptive.utility.UUID

class JsonString(val value: String) : JsonElement() {

    override val asString
        get() = value

    override val asChar
        get() = value.single()

    override fun <T> asUuid(): UUID<T> {
        return UUID(value)
    }

    override fun toString(): String {
        return "\"${escape(value)}\""
    }

    private fun escape(value: String): String {
        return value
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
            .replace("\\", "\\\\")
    }
}