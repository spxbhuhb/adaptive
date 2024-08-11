/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.json.elements

class JsonBoolean(
    val value: Boolean
) : JsonElement() {

    override val asBoolean: Boolean
        get() = value

    override val asUnit: Unit
        get() = check(value) { "false value as Unit" }

    override fun toString(): String {
        return value.toString()
    }
}