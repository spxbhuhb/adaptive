/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.utility

import java.util.concurrent.locks.ReentrantLock

actual fun getLock(): Lock =
    JvmLock()

internal class JvmLock : Lock {

    private val mutex = ReentrantLock()

    override fun lock() {
        mutex.lock()
    }

    override fun unlock() {
        mutex.unlock()
    }

}