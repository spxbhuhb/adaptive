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

    override fun dump(payload: ByteArray): String {
        return payload.decodeToString()
    }

}