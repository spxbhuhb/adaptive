/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.utility

import kotlinx.browser.window

actual fun exitProcessCommon(status: Int) {
    // FIXME add exitProcess for node in Js target
    window.location.pathname = "/exitProcess"
}