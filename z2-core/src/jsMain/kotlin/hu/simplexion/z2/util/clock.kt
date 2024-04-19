/*
 * Copyright © 2020-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.util

import kotlinx.browser.window

actual fun vmNowMicro(): Long {
    // performance.now returns with a double that contains milliseconds
    return (window.performance.now() * 1_000).toLong()
}

actual fun vmNowSecond() : Long {
    return vmNowMicro() / 1_000_000
}