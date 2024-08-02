/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service

import hu.simplexion.adaptive.service.transport.ServiceCallTransport
import hu.simplexion.adaptive.utility.manualOrPlugin
import hu.simplexion.adaptive.utility.overrideManually
import hu.simplexion.adaptive.utility.pluginGenerated
import hu.simplexion.adaptive.wireformat.WireFormatDecoder

interface ServiceBase {

    /**
     * Name of the service, the fully qualified class name of the
     * interface by default.
     */
    var serviceName : String
        get() = pluginGenerated()
        set(value) = overrideManually("serviceName", value)

    /**
     * The call transport to use when calling a service function. You can change this
     * field to use different call transport than the default. When null, service
     * calls use [defaultServiceCallTransport].
     *
     * Overridden by the plugin with:
     *
     * ```kotlin
     * override var callTransport : ServiceCallTransport = defaultServiceCallTransport
     * ```
     */
    var serviceCallTransport: ServiceCallTransport?
        get() = null
        set(value) {
            manualOrPlugin("serviceCallTransport", value)
        }

    val serviceCallTransportOrDefault: ServiceCallTransport
        get() = serviceCallTransport ?: defaultServiceCallTransport

    fun wireFormatEncoder() =
        serviceCallTransportOrDefault.encoder()

    fun wireFormatDecoder(payload: ByteArray): WireFormatDecoder<*> =
        serviceCallTransportOrDefault.decoder(payload)

}