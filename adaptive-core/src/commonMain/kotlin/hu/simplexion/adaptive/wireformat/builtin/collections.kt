/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.builtin

import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder
import hu.simplexion.adaptive.wireformat.WireFormatKind
import hu.simplexion.adaptive.wireformat.signature.WireFormatTypeArgument

class ArrayWireFormat<T>(
    val typeArgument: WireFormatTypeArgument<T>
) : WireFormat<Array<T?>> {

    override val wireFormatName: String
        get() = "kotlin.Array"

    override val wireFormatKind: WireFormatKind
        get() = WireFormatKind.Collection

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Array<T?>): WireFormatEncoder =
        encoder.items(value.toList(), typeArgument)

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Array<T?> {
        val list = (decoder?.items(source, typeArgument) ?: emptyList()) as List<Any?>
        @Suppress("UNCHECKED_CAST") // FIXME this does not work, Java arrays have to know the class of the type argument at compile time
        return Array(list.size) { list[it] } as Array<T?>
    }

    override fun wireFormatCopy(typeArguments: List<WireFormatTypeArgument<*>>) : ArrayWireFormat<*> {
        check(typeArguments.size == 1)
        return ArrayWireFormat(typeArguments[0])
    }

}

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