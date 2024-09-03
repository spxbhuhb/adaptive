/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat

import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.registry.Registry
import `fun`.adaptive.service.model.DisconnectException
import `fun`.adaptive.wireformat.builtin.*
import `fun`.adaptive.wireformat.signature.WireFormatTypeArgument

object WireFormatRegistry : Registry<WireFormat<*>>() {
    init {
        val n = WireFormatTypeArgument<Any>(PolymorphicWireFormat(), true)

        set("kotlin.Pair", PairWireFormat(n, n))
        set("kotlin.Array", ArrayWireFormat(n))
        set("kotlin.collections.List", ListWireFormat(n))
        set("kotlin.collections.Set", SetWireFormat(n))
        set("kotlin.collections.Map", MapWireFormat(n, n))

        set("kotlin.time.Duration", DurationWireFormat)
        set("kotlinx.datetime.Instant", InstantWireFormat)
        set("kotlinx.datetime.LocalDate", LocalDateWireFormat)
        set("kotlinx.datetime.LocalDateTime", LocalDateTimeWireFormat)
        set("kotlinx.datetime.LocalTime", LocalTimeWireFormat)

        this += AdatClassMetadata
        this += AdatPropertyMetadata
        this += AdatDescriptorMetadata

        this += DisconnectException
    }

    operator fun plusAssign(wireFormat: WireFormat<*>) {
        set(wireFormat.wireFormatName, wireFormat)
    }
}