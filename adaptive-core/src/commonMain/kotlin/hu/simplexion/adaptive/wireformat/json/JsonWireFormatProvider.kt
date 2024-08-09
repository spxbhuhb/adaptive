/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.json

import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder
import hu.simplexion.adaptive.wireformat.WireFormatProvider

class JsonWireFormatProvider : WireFormatProvider() {

    override val useTextFrame
        get() = true

    override fun encoder(): WireFormatEncoder =
        JsonWireFormatEncoder()

    override fun decoder(payload: ByteArray): WireFormatDecoder<*> =
        JsonWireFormatDecoder(payload)

    override fun writeMessage(payload: ByteArray, writeFun: (ByteArray) -> Unit) {
        writeFun(payload)
        writeFun(newLine)
    }

    override fun readMessage(buffer: ByteArray): Pair<List<ByteArray>, ByteArray> {
        val messages = mutableListOf<ByteArray>()

        var start = 0
        var current = 0
        val end = buffer.size
        val newLine = 0x0a.toByte()

        while (current < end) {
            if (buffer[current] != newLine) {
                current ++
                continue
            }

            messages.add(buffer.sliceArray(start until current))
            current ++
            start = current
        }

        val remaining = if (start < buffer.size) {
            buffer.sliceArray(start until buffer.size)
        } else {
            emptyByteArray
        }

        return Pair(messages, remaining)
    }

    override fun dump(payload: ByteArray): String {
        return payload.decodeToString()
    }

    companion object {
        val emptyByteArray = byteArrayOf()
        val newLine = byteArrayOf(0x0a)
    }
}