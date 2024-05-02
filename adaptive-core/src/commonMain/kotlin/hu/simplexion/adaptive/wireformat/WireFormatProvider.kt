/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat

import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatProvider

abstract class WireFormatProvider {

    abstract fun encoder(): WireFormatEncoder

    abstract fun decoder(payload: ByteArray): WireFormatDecoder<*>

    companion object {

        var defaultWireFormatProvider: WireFormatProvider = ProtoWireFormatProvider()

        fun <T> encode(instance : T, wireFormat : WireFormat<T>) : ByteArray =
            defaultWireFormatProvider.encoder().rawInstance(instance, wireFormat).pack()


        fun <T> decode(byteArray : ByteArray, wireFormat : WireFormat<T>) : T =
            defaultWireFormatProvider.decoder(byteArray).asInstance(wireFormat)

    }
}
