/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor

import hu.simplexion.adaptive.service.defaultServiceCallTransport

fun withWebSocketTransport() {
    defaultServiceCallTransport = BasicWebSocketServiceCallTransport(useTextFrame = true).also { it.start() }
}
