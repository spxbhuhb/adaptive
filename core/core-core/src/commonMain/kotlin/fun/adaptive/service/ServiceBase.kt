/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.service

import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.manualOrPlugin
import `fun`.adaptive.utility.overrideManually
import `fun`.adaptive.utility.pluginGenerated
import `fun`.adaptive.wireformat.WireFormatDecoder

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
     * field to use different call transport than the default.
     *
     * Service implementations use the transport in the context if not overridden.
     *
     * Overridden by the plugin with:
     *
     * ```kotlin
     * override var callTransport : ServiceCallTransport = null
     * ```
     */
    var serviceCallTransport: ServiceCallTransport
        get() = manualOrPlugin("serviceCallTransport")
        set(value) {
            manualOrPlugin("serviceCallTransport", value)
        }

    fun wireFormatEncoder() =
        serviceCallTransport.encoder()

    fun wireFormatDecoder(payload: ByteArray): WireFormatDecoder<*> =
        serviceCallTransport.decoder(payload)

}