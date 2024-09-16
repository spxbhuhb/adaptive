/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ktor.api

import `fun`.adaptive.ktor.ClientWebSocketServiceCallTransport
import `fun`.adaptive.service.defaultServiceCallTransport
import `fun`.adaptive.service.defaultServiceImplFactory
import `fun`.adaptive.service.factory.ServiceImplFactory
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.api.Json

suspend fun withWebSocketTransport(
    host: String,
    wireFormatProvider: WireFormatProvider = Json,
    servicePath: String = "/adaptive/service-ws",
    clientIdPath: String = "/adaptive/client-id",
    trace: Boolean = false,
    serviceImplFactory: ServiceImplFactory = defaultServiceImplFactory,
) =
    webSocketTransport(host, wireFormatProvider, servicePath, clientIdPath, trace, serviceImplFactory).also {
        defaultServiceCallTransport = it
    }

suspend fun webSocketTransport(
    host: String,
    wireFormatProvider: WireFormatProvider = Json,
    servicePath: String = "/adaptive/service-ws",
    clientIdPath: String = "/adaptive/client-id",
    trace: Boolean = false,
    serviceImplFactory: ServiceImplFactory = defaultServiceImplFactory,
) =
    ClientWebSocketServiceCallTransport(host, servicePath, clientIdPath, wireFormatProvider, serviceImplFactory)
        .also {
            it.trace = trace
            it.start()
        }

fun String.toHttp(path: String): String {
    var url = this.trim()

    when {
        url.startsWith("ws://") -> url = url.replaceRange(0, 4, "http://")
        url.startsWith("wss://") -> url = url.replaceRange(0, 5, "https://")
        ! url.startsWith("http://") && ! url.startsWith("https://") -> url = "wss://$url"
    }

    return url.removeSuffix("/") + '/' + path.trimStart('/')
}

fun String.toWs(path: String): String {
    var url = this.trim()

    when {
        url.startsWith("http://") -> url = url.replaceRange(0, 7, "ws://")
        url.startsWith("https://") -> url = url.replaceRange(0, 8, "wss://")
        ! url.startsWith("ws://") && ! url.startsWith("wss://") -> url = "wss://$url"
    }

    return url.trimEnd('/') + '/' + path.trimStart('/')
}