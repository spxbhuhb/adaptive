/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.service.factory

import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.wireformat.WireFormatProvider

fun interface ServiceTransportFactory {

    fun invoke(
        url: String,
        wireFormatProvider: WireFormatProvider,
        setupFun: suspend (ServiceCallTransport) -> Unit
    ) : ServiceCallTransport

}