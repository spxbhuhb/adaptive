/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.utility

/**
 * This is a copy of Ktor multiplatform lock as we clearly need it.
 * Only JVM and JS implementations are added at the moment, I'll add native
 * when it becomes important:
 *
 * https://github.com/ktorio/ktor/blob/master/ktor-utils/posix/src/io/ktor/util/LockNative.kt
 */
actual class Lock actual constructor() {
    actual fun lock() {
    }

    actual fun unlock() {
    }
}