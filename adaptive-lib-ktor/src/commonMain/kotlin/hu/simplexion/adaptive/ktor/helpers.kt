/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor

import hu.simplexion.adaptive.service.defaultServiceCallTransport
import hu.simplexion.adaptive.service.defaultServiceImplFactory
import hu.simplexion.adaptive.service.factory.ServiceImplFactory
import hu.simplexion.adaptive.wireformat.json.JsonWireFormatProvider
import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatProvider

suspend fun withJsonWebSocketTransport(
    host: String,
    servicePath: String = "/adaptive/service-ws",
    clientIdPath: String = "/adaptive/client-id",
    trace: Boolean = false,
    serviceImplFactory: ServiceImplFactory = defaultServiceImplFactory,
) =
    ClientWebSocketServiceCallTransport(host, servicePath, clientIdPath, JsonWireFormatProvider(), serviceImplFactory)
        .also {
            defaultServiceCallTransport = it
            it.trace = trace
            it.start()
        }

suspend fun withProtoWebSocketTransport(
    host: String,
    servicePath: String = "/adaptive/service-ws",
    clientIdPath: String = "/adaptive/client-id",
    trace: Boolean = false,
    serviceImplFactory: ServiceImplFactory = defaultServiceImplFactory,
) =
    ClientWebSocketServiceCallTransport(host, servicePath, clientIdPath, ProtoWireFormatProvider(), serviceImplFactory)
        .also {
            defaultServiceCallTransport = it
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