/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.persistence.read
import `fun`.adaptive.persistence.write
import kotlinx.io.files.Path

abstract class WireFormatProvider {

    abstract val useTextFrame: Boolean

    abstract fun encoder(): WireFormatEncoder

    abstract fun decoder(payload: ByteArray): WireFormatDecoder<*>

    /**
     * Write the payload with [writeFun] so that [readMessage] can read from a
     * non-positioned byte stream. This typically involves adding a header with
     * the message size and/or escaping the message and adding a delimiter.
     * The exact method depends on the actual wire format.
     */
    abstract fun writeMessage(payload: ByteArray, writeFun: (ByteArray) -> Unit)

    /**
     * Read messages from the incoming stream of bytes created with [writeMessage].
     * The list in the return value contains the messages found. The single byte
     * array contains the unprocessed bytes (in case there is a partial message in
     * the [buffer]).
     */
    abstract fun readMessage(buffer: ByteArray, offset: Int, length: Int): Pair<List<ByteArray>, ByteArray>

    abstract fun dump(payload: ByteArray): String

    fun <T> encode(instance: T, wireFormat: WireFormat<T>): ByteArray =
        encoder().rawInstance(instance, wireFormat).pack()

    fun <T> write(path: Path, instance: T, wireFormat: WireFormat<T>) {
        path.write(encoder().rawInstance(instance, wireFormat).pack())
    }

    fun <A : AdatClass> write(path: Path, instance: A) {
        @Suppress("UNCHECKED_CAST")
        path.write(encoder().rawInstance(instance, instance.adatCompanion.adatWireFormat as WireFormat<A>).pack())
    }

    fun <T> decode(byteArray: ByteArray, wireFormat: WireFormat<T>): T =
        decoder(byteArray).asInstance(wireFormat)

    fun <T> read(path: Path, wireFormat: WireFormat<T>): T =
        decoder(path.read()).asInstance(wireFormat)

}