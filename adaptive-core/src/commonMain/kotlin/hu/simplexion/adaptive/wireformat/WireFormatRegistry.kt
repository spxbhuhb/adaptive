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

        entries["kotlin.Array"] = ArrayWireFormat(n)
        entries["kotlin.collections.List"] = ListWireFormat(n)
        entries["kotlin.collections.Set"] = SetWireFormat(n)
        entries["kotlin.collections.Map"] = MapWireFormat(n, n)
    }
}