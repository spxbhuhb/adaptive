/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat

import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.registry.Registry
import `fun`.adaptive.service.model.DisconnectException
import `fun`.adaptive.wireformat.builtin.*
import `fun`.adaptive.wireformat.signature.WireFormatTypeArgument

object WireFormatRegistry : Registry<WireFormat<*>>() {
    init {
        val n = WireFormatTypeArgument<Any>(PolymorphicWireFormat, true)

        set("kotlin.Any", PolymorphicWireFormat)

        set("kotlin.Byte", ByteWireFormat)
        set("kotlin.Short", ShortWireFormat)
        set("kotlin.Int", IntWireFormat)
        set("kotlin.Long", LongWireFormat)
        set("kotlin.Float", FloatWireFormat)
        set("kotlin.Double", DoubleWireFormat)
        set("kotlin.Boolean", BooleanWireFormat)
        set("kotlin.Char", CharWireFormat)
        set("kotlin.String", StringWireFormat)

        set("kotlin.ByteArray", ByteArrayWireFormat)
        set("kotlin.ShortArray", ShortArrayWireFormat)
        set("kotlin.IntArray", IntArrayWireFormat)
        set("kotlin.LongArray", LongArrayWireFormat)
        set("kotlin.FloatArray", FloatArrayWireFormat)
        set("kotlin.DoubleArray", DoubleArrayWireFormat)
        set("kotlin.BooleanArray", BooleanArrayWireFormat)
        set("kotlin.CharArray", CharArrayWireFormat)

        set("kotlin.UByte", UByteWireFormat)
        set("kotlin.UShort", UShortWireFormat)
        set("kotlin.UInt", UIntWireFormat)
        set("kotlin.ULong", ULongWireFormat)
        set("kotlin.UByteArray", UByteArrayWireFormat)
        set("kotlin.UShortArray", UShortArrayWireFormat)
        set("kotlin.UIntArray", UIntArrayWireFormat)
        set("kotlin.ULongArray", ULongArrayWireFormat)

        set("kotlin.Pair", PairWireFormat(n, n))
        set("kotlin.collections.List", ListWireFormat(n))
        set("kotlin.collections.Set", SetWireFormat(n))
        set("kotlin.collections.Map", MapWireFormat(n, n))

        set("kotlin.time.Duration", DurationWireFormat)
        set("kotlinx.datetime.Instant", InstantWireFormat)
        set("kotlinx.datetime.LocalDate", LocalDateWireFormat)
        set("kotlinx.datetime.LocalDateTime", LocalDateTimeWireFormat)
        set("kotlinx.datetime.LocalTime", LocalTimeWireFormat)

        set("fun.adaptive.utility.UUID", UuidWireFormat)

        this += AdatClassMetadata
        this += AdatPropertyMetadata
        this += AdatDescriptorMetadata

        this += AdaptiveInstructionGroup

        this += DisconnectException
    }

    operator fun plusAssign(wireFormat: WireFormat<*>) {
        set(wireFormat.wireFormatName, wireFormat)
    }

    operator fun WireFormat<*>.unaryPlus() {
        set(this.wireFormatName, this)
    }
}