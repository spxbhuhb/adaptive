/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor

import hu.simplexion.adaptive.service.defaultServiceCallTransport
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.defaultWireFormatProvider
import hu.simplexion.adaptive.wireformat.json.JsonWireFormatProvider
import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatProvider

suspend fun withJsonWebSocketTransport(
    servicePath: String = "/adaptive/service",
    clientIdPath: String = "/adaptive/client-id",
    trace: Boolean = false
) =
    BasicWebSocketServiceCallTransport(servicePath = servicePath, clientIdPath = clientIdPath, useTextFrame = true, trace = trace)
        .also {
            defaultWireFormatProvider = JsonWireFormatProvider()
            defaultServiceCallTransport = it
            it.start()
        }

suspend fun withProtoWebSocketTransport(
    servicePath: String = "/adaptive/service",
    clientIdPath: String = "/adaptive/client-id",
    trace: Boolean = false
) =
    BasicWebSocketServiceCallTransport(servicePath = servicePath, clientIdPath = clientIdPath, useTextFrame = false, trace = trace)
        .also {
            defaultWireFormatProvider = ProtoWireFormatProvider()
            defaultServiceCallTransport = it
            it.start()
        }

