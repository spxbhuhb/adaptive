/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat

abstract class WireFormatProvider {

    abstract val useTextFrame: Boolean

    abstract fun encoder(): WireFormatEncoder

    abstract fun decoder(payload: ByteArray): WireFormatDecoder<*>

    abstract fun dump(payload: ByteArray): String

    fun <T> encode(instance: T, wireFormat: WireFormat<T>): ByteArray =
        encoder().rawInstance(instance, wireFormat).pack()

    fun <T> decode(byteArray: ByteArray, wireFormat: WireFormat<T>): T =
        decoder(byteArray).asInstance(wireFormat)

}
