/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat

import hu.simplexion.adaptive.registry.Registry
import hu.simplexion.adaptive.wireformat.builtin.*
import hu.simplexion.adaptive.wireformat.signature.WireFormatTypeArgument

object WireFormatRegistry : Registry<WireFormat<*>>() {
    init {
        val n = WireFormatTypeArgument(NothingWireFormat, true)

        set("kotlin.Array", ArrayWireFormat(n))
        set("kotlin.collections.List", ListWireFormat(n))
        set("kotlin.collections.Set", SetWireFormat(n))
        set("kotlin.collections.Map", MapWireFormat(n, n))
    }
}