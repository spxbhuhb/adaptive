/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service.model

import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder

class RequestEnvelope(
    val callId: UUID<RequestEnvelope>,
    val serviceName: String,
    val funName: String,
    val payload: ByteArray
) : WireFormat<RequestEnvelope> {

    override val wireFormatCompanion: WireFormat<RequestEnvelope>
        get() = RequestEnvelope

    companion object : WireFormat<RequestEnvelope> {

        override fun wireFormatEncode(encoder: WireFormatEncoder, value: RequestEnvelope): WireFormatEncoder =
            encoder
                .uuid(1, "callId", value.callId)
                .string(2, "serviceName", value.serviceName)
                .string(3, "funName", value.funName)
                .byteArray(4, "payload", value.payload)

        override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): RequestEnvelope {
            requireNotNull(decoder)
            return RequestEnvelope(
                decoder.uuid(1, "callId"),
                decoder.string(2, "serviceName"),
                decoder.string(3, "funName"),
                decoder.byteArray(4, "payload")
            )
        }
    }


}