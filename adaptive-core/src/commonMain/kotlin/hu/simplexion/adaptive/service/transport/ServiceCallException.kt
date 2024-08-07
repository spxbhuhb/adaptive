/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service.transport

import hu.simplexion.adaptive.service.model.ServiceExceptionData
import hu.simplexion.adaptive.service.model.TransportEnvelope

class ServiceCallException(
    val serviceName: String,
    val funName: String,
    val response: TransportEnvelope,
    val exceptionData: ServiceExceptionData
) : RuntimeException()