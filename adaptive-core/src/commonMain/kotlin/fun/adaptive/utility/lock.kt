/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.utility

/**
 * Marks functions that are thread safe.
 */
annotation class ThreadSafe

/**
 * Marks functions that needs to run under the protection of a lock.
 */
annotation class RequireLock

/**
 * This is a copy of Ktor multiplatform lock as we clearly need it.
 * Only JVM and JS implementations are added at the moment, I'll add native
 * when it becomes important:
 *
 * https://github.com/ktorio/ktor/blob/master/ktor-utils/posix/src/io/ktor/util/LockNative.kt
 */
interface Lock {
    fun lock()
    fun unlock()
}

expect fun getLock() : Lock

inline fun <R> Lock.use(block: () -> R): R {
    try {
        lock()
        return block()
    } finally {
        unlock()
    }
}