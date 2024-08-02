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
 * An envelope for service calls and returns.
 *
 * @property  callId       The unique ID of the call, assigned by the caller, same for call and return.
 * @property  serviceName  Call: name of the service. Return: `null`.
 * @property  funName      Call: signature of the function. Return: `null`
 * @property  success      Call: null. Return: true if successful, false if failed.
 * @property  payload      Call: arguments of the call. Return: return value or exception.
 */
@Wire
class TransportEnvelope(
    val callId: UUID<TransportEnvelope>,
    val serviceName: String?,
    val funName: String?,
    val success: Boolean?,
    val payload: ByteArray
) {

    override fun toString(): String {
        return "$callId $serviceName $funName $success ${payload.size}"
    }

    companion object : WireFormat<TransportEnvelope> {

        override val wireFormatName: String
            get() = "hu.simplexion.adaptive.service.model.TransportEnvelope"

        override fun wireFormatEncode(encoder: WireFormatEncoder, value: TransportEnvelope): WireFormatEncoder =
            encoder
                .uuid(1, "callId", value.callId)
                .stringOrNull(2, "serviceName", value.serviceName)
                .stringOrNull(3, "funName", value.funName)
                .booleanOrNull(4, "success", value.success)
                .byteArray(5, "payload", value.payload)

        override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): TransportEnvelope {
            requireNotNull(decoder)
            return TransportEnvelope(
                decoder.uuid(1, "callId"),
                decoder.stringOrNull(2, "serviceName"),
                decoder.stringOrNull(3, "funName"),
                decoder.booleanOrNull(4, "success"),
                decoder.byteArray(5, "payload")
            )
        }
    }

}