/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service.model

import hu.simplexion.adaptive.wireformat.Wire
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder

@Wire
class ServiceExceptionData(
    val className: String,
    val message: String?,
    val payload: ByteArray
) {

    companion object : WireFormat<ServiceExceptionData> {

        override val wireFormatName: String
            get() = "hu.simplexion.adaptive.service.model.ServiceExceptionData"

        override fun wireFormatEncode(encoder: WireFormatEncoder, value: ServiceExceptionData): WireFormatEncoder =
            encoder
                .string(1, "className", value.className)
                .stringOrNull(2, "message", value.message)
                .byteArray(3, "payload", value.payload)

        override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): ServiceExceptionData {
            requireNotNull(decoder)
            return ServiceExceptionData(
                decoder.string(1, "className"),
                decoder.stringOrNull(2, "message"),
                decoder.byteArray(3, "payload")
            )
        }
    }

}