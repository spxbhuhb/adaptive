/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.example.ui.counter
import hu.simplexion.adaptive.ktor.withWebSocketTransport
import hu.simplexion.adaptive.ui.dom.browserJs

fun main() {

    withWebSocketTransport()

    browserJs {
        counter()
    }

}