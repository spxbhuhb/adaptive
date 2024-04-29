/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.service.transport

import hu.simplexion.adaptive.service.model.ResponseEnvelope

/**
 * Handle service error responses.
 */
open class ServiceErrorHandler {

    open fun connectionError(ex: Exception) {

    }

    open fun callError(serviceName: String, funName: String, responseEnvelope: ResponseEnvelope) {

    }

}