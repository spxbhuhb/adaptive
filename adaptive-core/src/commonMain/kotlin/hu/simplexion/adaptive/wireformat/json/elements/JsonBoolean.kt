/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.json.elements

class JsonBoolean(
    val value: Boolean
) : JsonElement() {

    override val asBoolean: Boolean
        get() = value

    override val asUnit: Unit
        get() = check(value) { "false value as Unit" }
}