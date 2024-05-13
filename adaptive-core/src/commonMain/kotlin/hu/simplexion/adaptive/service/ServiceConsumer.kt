/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service

import hu.simplexion.adaptive.wireformat.WireFormatEncoder

interface ServiceConsumer : ServiceBase {

    /**
     * Perform an actual call to the service. This function is called by the code generated by
     * the plugin.
     */
    suspend fun callService(funName: String, payload: WireFormatEncoder): ByteArray =
        serviceCallTransportOrDefault.call(serviceName, funName, payload.pack())

}