/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ktor.api

import `fun`.adaptive.ktor.websocket.ClientWebSocketServiceCallTransport
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.api.Json

fun webSocketTransport(
    host: String,
    wireFormatProvider: WireFormatProvider = Json,
    setupFun: suspend (ServiceCallTransport) -> Unit = { },
    clientIdPath: String = "/adaptive/client-id",
    servicePath: String = "/adaptive/service-ws"
) =
    ClientWebSocketServiceCallTransport(host, servicePath, clientIdPath, wireFormatProvider, setupFun)