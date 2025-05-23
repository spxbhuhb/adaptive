/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.xlsx.internal.model

import `fun`.adaptive.xlsx.internal.dom.Node

internal class Cell(reference: String, value: Any, type: Type, numberFormat: Int) : Node("c") {

    enum class Type(val code: String?) {
        NORMAL(null),
        SHARED_STRING("s"),
        STRING("str"),
        BOOLEAN("b"),
        ISO_DATE("d")
    }

    init {
        this["r"] = reference // coordinate eg: B5

        if (type.code != null) { // type: default is normal
            this["t"] = type.code
        }

        if (numberFormat != 0) { // numeric format index default: 0
            this["s"] = numberFormat.toString()
        }

        +node("v", value.toString())
    }

}
