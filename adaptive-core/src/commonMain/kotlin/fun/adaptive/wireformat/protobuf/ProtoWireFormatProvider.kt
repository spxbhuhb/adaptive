/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.protobuf

import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatEncoder
import `fun`.adaptive.wireformat.WireFormatProvider

class ProtoWireFormatProvider : WireFormatProvider() {

    override val useTextFrame
        get() = false

    override fun encoder(): WireFormatEncoder =
        ProtoWireFormatEncoder()

    override fun decoder(payload: ByteArray): WireFormatDecoder<*> =
        ProtoWireFormatDecoder(payload)

    override fun writeMessage(payload: ByteArray, writeFun: (ByteArray) -> Unit) {
        writeFun(
            ProtoBufferWriter().let {
                it.bytesHeader(1, payload)
                it.pack()
            }
        )
        writeFun(payload)
    }

    override fun readMessage(buffer: ByteArray, offset: Int, length: Int): Pair<List<ByteArray>, ByteArray> {
        val reader = ProtoBufferReader(buffer, offset, length)
        val arrays = reader.records(true).map { it.bytes() }.toList()
        val remaining = buffer.copyOfRange(reader.readOffset, buffer.size)
        return arrays to remaining
    }

    override fun dump(payload: ByteArray): String {
        return payload.dumpProto()
    }

}