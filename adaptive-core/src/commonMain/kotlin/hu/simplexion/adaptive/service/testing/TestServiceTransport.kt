/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service.testing

import hu.simplexion.adaptive.service.BasicServiceContext
import hu.simplexion.adaptive.service.ServiceImpl
import hu.simplexion.adaptive.service.transport.ServiceCallTransport

class TestServiceTransport(
    val serviceImpl: ServiceImpl<*,*>,
    val dump: Boolean = false
) : ServiceCallTransport {

    override suspend fun call(serviceName: String, funName: String, payload: ByteArray): ByteArray {
        if (dump) {
            println("==== REQUEST ====")
            println(serviceName)
            println(funName)
            println(payload.decodeToString())
        }

        val responsePayload = serviceImpl
            .newInstance(BasicServiceContext())
            .dispatch(funName, wireFormatDecoder(payload))

        if (dump) {
            println("==== RESPONSE ====")
            println(responsePayload)
        }

        return responsePayload
    }
}