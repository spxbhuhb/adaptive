/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.protobuf

import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder
import hu.simplexion.adaptive.wireformat.WireFormatProvider

class ProtoWireFormatProvider : WireFormatProvider() {

    override fun encoder(): WireFormatEncoder =
        ProtoWireFormatEncoder()

    override fun decoder(payload: ByteArray): WireFormatDecoder<*> =
        ProtoWireFormatDecoder(payload)

}