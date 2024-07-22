/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service.transport

import hu.simplexion.adaptive.service.model.ResponseEnvelope
import hu.simplexion.adaptive.service.model.ServiceExceptionData

class ServiceResultException(
    val serviceName: String,
    val funName: String,
    val responseEnvelope: ResponseEnvelope,
    val serviceExceptionData: ServiceExceptionData
) : RuntimeException()