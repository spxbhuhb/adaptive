/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.builtin

import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder
import hu.simplexion.adaptive.wireformat.WireFormatKind

class ArrayWireFormat<T>(
    val itemWireFormat: WireFormat<T>,
    val nullable : Boolean = false
) : WireFormat<Array<T?>> {

    override val kind: WireFormatKind
        get() = WireFormatKind.Collection

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Array<T?>): WireFormatEncoder =
        encoder.items(value.toList(), itemWireFormat, nullable)

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Array<T?> {
        val list = (decoder?.items(source, itemWireFormat, nullable) ?: emptyList()) as List<Any?>
        @Suppress("UNCHECKED_CAST") // I have no idea why it doesn't let me create an array from the list directly
        return Array(list.size) { list[it] } as Array<T?>
    }

}

class ListWireFormat<T>(
    val itemWireFormat: WireFormat<T>,
    val nullable : Boolean = false
) : WireFormat<List<T?>> {

    override val kind: WireFormatKind
        get() = WireFormatKind.Collection

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: List<T?>): WireFormatEncoder =
        encoder.items(value, itemWireFormat, nullable)

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): List<T?> =
        decoder?.items(source, itemWireFormat, nullable) ?: emptyList()

}

class SetWireFormat<T>(
    val itemWireFormat: WireFormat<T>,
    val nullable : Boolean = false
) : WireFormat<Set<T?>> {

    override val kind: WireFormatKind
        get() = WireFormatKind.Collection

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Set<T?>): WireFormatEncoder =
        encoder.items(value, itemWireFormat, nullable)

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Set<T?> =
        decoder?.items(source, itemWireFormat, nullable)?.toSet() ?: emptySet()

}

class MapWireFormat<K,V>(
    val keyWireFormat : WireFormat<K>,
    val valueWireFormat: WireFormat<V>,
    val keyNullable : Boolean = false,
    val valueNullable: Boolean = false,
) : WireFormat<Map<K?,V?>> {

    override val kind: WireFormatKind
        get() = WireFormatKind.Collection

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Map<K?,V?>): WireFormatEncoder =
        encoder.items(value.toList(), PairWireFormat(keyWireFormat, valueWireFormat, keyNullable, valueNullable), false)

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Map<K?,V?> {
        val list = decoder?.items(source, PairWireFormat(keyWireFormat, valueWireFormat, keyNullable, valueNullable), false)
        @Suppress("UNCHECKED_CAST") // nullable parameter of `items` is false
        list as List<Pair<K,V>>
        return list.toMap()
    }
}