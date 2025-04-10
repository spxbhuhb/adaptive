/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ktor.api

import `fun`.adaptive.ktor.ClientWebSocketServiceCallTransport
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.api.Json

fun webSocketTransport(
    host: String,
    wireFormatProvider: WireFormatProvider = Json,
    servicePath: String = "/adaptive/service-ws",
    clientIdPath: String = "/adaptive/client-id"
) =
    ClientWebSocketServiceCallTransport(host, servicePath, clientIdPath, wireFormatProvider)