/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.utility

actual fun getLock(): Lock =
    BrowserLock()

internal class BrowserLock : Lock {
    override fun lock() {
    }

    override fun unlock() {
    }
}