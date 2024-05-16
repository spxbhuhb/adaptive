/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor

import hu.simplexion.adaptive.service.defaultServiceCallTransport

fun withWebSocketTransport(path : String = "/adaptive/service") {
    defaultServiceCallTransport = BasicWebSocketServiceCallTransport(path = path, useTextFrame = true).also { it.start() }
}
