/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service.model

import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.Wire
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder

/**
 * Envelope a response payload.
 *
 * @property  [callId]   Call id from the corresponding [RequestEnvelope].
 * @property  [status]   Result status of the call.
 * @property  [payload]  Return value of the called service function.
 */
@Wire
class ResponseEnvelope(
    val callId: UUID<RequestEnvelope>,
    val status: ServiceCallStatus,
    val payload: ByteArray,
) {

    companion object : WireFormat<ResponseEnvelope> {

        override val wireFormatName: String
            get() = "hu.simplexion.adaptive.service.model.ResponseEnvelope"

        override fun wireFormatEncode(encoder: WireFormatEncoder, value: ResponseEnvelope): WireFormatEncoder =
            encoder
                .uuid(1, "callId", value.callId)
                .int(2, "status", value.status.value)
                .byteArray(3, "payload", value.payload)

        override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): ResponseEnvelope {
            requireNotNull(decoder)
            return ResponseEnvelope(
                decoder.uuid(1, "callId"),
                requireNotNull(decoder.int(2, "status")).let { mv -> ServiceCallStatus.entries.first { it.value == mv } },
                decoder.byteArray(3, "payload")
            )
        }
    }

}