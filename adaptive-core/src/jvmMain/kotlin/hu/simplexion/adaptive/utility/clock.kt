/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.utility

actual fun vmNowMicro(): Long {
    return System.nanoTime() / 1_000
}

actual fun vmNowSecond() : Long {
    return vmNowMicro() / 1_000_000
}