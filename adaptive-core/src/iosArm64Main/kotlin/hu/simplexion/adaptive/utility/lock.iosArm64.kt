/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.utility

import platform.Foundation.NSLock

actual fun getLock(): Lock =
    IOSLock()

internal class IOSLock : Lock {
    val lock = NSLock()

    override fun lock() {
        lock.lock()
    }

    override fun unlock() {
        lock.unlock()
    }
}