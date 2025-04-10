/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat

import `fun`.adaptive.utility.pluginGenerated
import `fun`.adaptive.wireformat.signature.WireFormatTypeArgument

interface WireFormat<T> {

    val wireFormatName: String
        get() = pluginGenerated()

    val wireFormatKind: WireFormatKind
        get() = WireFormatKind.Instance

    fun wireFormatEncode(encoder: WireFormatEncoder, value: T): WireFormatEncoder

    fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): T

    fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber : Int, fieldName : String, value : T?) =
        encoder.instanceOrNull(fieldNumber, fieldName, value, this)

    fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber : Int, fieldName : String) =
        decoder.instanceOrNull(fieldNumber,fieldName, this)

    fun wireFormatCopy(typeArguments : List<WireFormatTypeArgument<*>>) : WireFormat<*> = this

    fun newInstance(values: Array<Any?>): T {
        throw NotImplementedError()
    }

}