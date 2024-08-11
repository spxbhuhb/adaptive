/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.server.builtin

import `fun`.adaptive.foundation.query.single
import `fun`.adaptive.foundation.query.singleOrNull
import `fun`.adaptive.server.AdaptiveServerFragment

/**
 * Finds the store of the given type [T].
 *
 * Throws exception when:
 *
 * - the fragment is not part of an adaptive server
 * - there is no store of the given type
 * - there is more than one store of the given type
 */
inline fun <reified T : StoreImpl<T>> ServerFragmentImpl.store(): Lazy<T> =
    lazy {
        checkNotNull(adapter) { "this implementation is not part of an adaptive server" }
            .single { it is AdaptiveServerFragment && it.impl is T }
            .let { (it as AdaptiveServerFragment) }
            .impl as T
    }

/**
 * Finds the service of the given type [T].
 *
 * Throws exception when:
 *
 * - the fragment is not part of an adaptive server
 * - there is no service of the given type
 * - there is more than one service of the given type
 */
inline fun <reified T : ServiceImpl<T>> ServerFragmentImpl.service(): Lazy<T> =
    lazy {
        checkNotNull(adapter) { "this implementation is not part of an adaptive server" }
            .single { it is AdaptiveServerFragment && it.impl is T }
            .let { (it as AdaptiveServerFragment) }
            .impl as T
    }

/**
 * Finds the worker of the given type [T].
 *
 * Throws exception when:
 *
 * - the fragment is not part of an adaptive server
 * - there is no worker of the given type
 * - there is more than one worker of the given type
 */
inline fun <reified T : WorkerImpl<T>> ServerFragmentImpl.worker(): Lazy<T> =
    lazy {
        checkNotNull(adapter) { "this implementation is not part of an adaptive server" }
            .single { it is AdaptiveServerFragment && it.impl is T }
            .let { (it as AdaptiveServerFragment) }
            .impl as T
    }

/**
 * Finds the worker of the given type [T]. Uses `null` if there is no such
 * fragment.
 *
 * Throws exception when:
 *
 * - the fragment is not part of an adaptive server
 * - there is more than one worker of the given type
 */
inline fun <reified T : WorkerImpl<T>> ServerFragmentImpl.workerOrNull(): Lazy<T?> =
    lazy {
        checkNotNull(adapter) { "this implementation is not part of an adaptive server" }
            .singleOrNull { it is AdaptiveServerFragment && it.impl is T }
            ?.let { (it as ServerWorker).workerImpl as T }
    }

/**
 * Finds the service fragment with  of the given implementation type [T]. Uses `null`
 * if there is no such fragment.
 *
 * Throws exception when:
 *
 * - the fragment is not part of an adaptive server
 * - there is more than one fragment of the given implementation type
 */
inline fun <reified T> ServerFragmentImpl.implOrNull(): Lazy<T?> =
    lazy {
        checkNotNull(adapter) { "this implementation is not part of an adaptive server" }
            .singleOrNull { it is AdaptiveServerFragment && it.impl is T }
            ?.let { (it as AdaptiveServerFragment).impl as T }
    }