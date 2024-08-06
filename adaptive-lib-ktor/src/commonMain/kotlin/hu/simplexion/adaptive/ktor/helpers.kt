/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor

import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.service.defaultServiceCallTransport
import hu.simplexion.adaptive.wireformat.json.JsonWireFormatProvider
import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatProvider

suspend fun withJsonWebSocketTransport(
    servicePath: String = "/adaptive/service-ws",
    clientIdPath: String = "/adaptive/client-id",
    trace: Boolean = false,
    adapter: AdaptiveServerAdapter? = null
) =
    ClientWebSocketServiceCallTransport(servicePath = servicePath, clientIdPath = clientIdPath, JsonWireFormatProvider(), adapter)
        .also {
            defaultServiceCallTransport = it
            it.trace = trace
            it.start()
        }

suspend fun withProtoWebSocketTransport(
    servicePath: String = "/adaptive/service-ws",
    clientIdPath: String = "/adaptive/client-id",
    trace: Boolean = false,
    adapter: AdaptiveServerAdapter? = null
) =
    ClientWebSocketServiceCallTransport(servicePath = servicePath, clientIdPath = clientIdPath, ProtoWireFormatProvider(), adapter)
        .also {
            defaultServiceCallTransport = it
            it.trace = trace
            it.start()
        }

