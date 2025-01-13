/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.builtin

import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatEncoder
import `fun`.adaptive.wireformat.WireFormatKind
import `fun`.adaptive.wireformat.signature.WireFormatTypeArgument

class ListWireFormat<T>(
    val typeArgument: WireFormatTypeArgument<T>
) : WireFormat<List<T?>> {

    constructor(wireFormat: WireFormat<T>) : this(WireFormatTypeArgument(wireFormat, false))

    override val wireFormatName: String
        get() = "kotlin.collections.List"

    override val wireFormatKind: WireFormatKind
        get() = WireFormatKind.Collection

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: List<T?>): WireFormatEncoder =
        encoder.items(value, typeArgument)

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): List<T?> =
        decoder?.items(source, typeArgument) ?: emptyList()

    override fun wireFormatCopy(typeArguments: List<WireFormatTypeArgument<*>>) : ListWireFormat<*> {
        check(typeArguments.size == 1)
        return ListWireFormat(typeArguments[0])
    }
}

class SetWireFormat<T>(
    val typeArgument: WireFormatTypeArgument<T>
) : WireFormat<Set<T?>> {

    constructor(wireFormat: WireFormat<T>) : this(WireFormatTypeArgument(wireFormat, false))

    override val wireFormatName: String
        get() = "kotlin.collections.Set"

    override val wireFormatKind: WireFormatKind
        get() = WireFormatKind.Collection

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Set<T?>): WireFormatEncoder =
        encoder.items(value, typeArgument)

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Set<T?> =
        decoder?.items(source, typeArgument)?.toSet() ?: emptySet()

    override fun wireFormatCopy(typeArguments: List<WireFormatTypeArgument<*>>) : SetWireFormat<*> {
        check(typeArguments.size == 1)
        return SetWireFormat(typeArguments[0])
    }
}

class MapWireFormat<K, V>(
    keyTypeArgument: WireFormatTypeArgument<K>,
    valueTypeArgument: WireFormatTypeArgument<V>
) : WireFormat<Map<K?, V?>> {

    override val wireFormatName: String
        get() = "kotlin.collections.Map"

    override val wireFormatKind: WireFormatKind
        get() = WireFormatKind.Collection

    val itemTypeArgument = WireFormatTypeArgument(PairWireFormat(keyTypeArgument, valueTypeArgument), false)

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Map<K?, V?>): WireFormatEncoder =
        encoder.items(value.toList(), itemTypeArgument)

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Map<K?, V?> {
        val list = decoder?.items(source, itemTypeArgument)
        @Suppress("UNCHECKED_CAST") // nullable parameter of `items` is false
        list as List<Pair<K, V>>
        return list.toMap()
    }

    override fun wireFormatCopy(typeArguments: List<WireFormatTypeArgument<*>>) : MapWireFormat<*,*> {
        check(typeArguments.size == 2)
        return MapWireFormat(typeArguments[0], typeArguments[1])
    }
}