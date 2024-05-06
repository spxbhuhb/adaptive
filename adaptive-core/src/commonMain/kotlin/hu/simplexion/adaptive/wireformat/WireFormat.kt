/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat

interface WireFormat<T> {

    val wireFormatKind: WireFormatKind
        get() = WireFormatKind.Instance

    fun wireFormatEncode(encoder: WireFormatEncoder, value: T): WireFormatEncoder

    fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): T

    @Suppress("UNCHECKED_CAST")
    fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber : Int, fieldName : String, value : T?) =
        encoder.instanceOrNull(fieldNumber, fieldName, this as T?, this)

    fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber : Int, fieldName : String) =
        decoder.instanceOrNull(fieldNumber,fieldName, this)

}