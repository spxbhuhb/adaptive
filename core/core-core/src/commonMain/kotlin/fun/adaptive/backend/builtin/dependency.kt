/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.backend.builtin

import `fun`.adaptive.backend.BackendFragment
import `fun`.adaptive.foundation.query.single
import `fun`.adaptive.foundation.query.singleOrNull


/**
 * Finds the service of the given type [T].
 *
 * Throws exception when:
 *
 * - the fragment is not part of an adaptive backend
 * - there is no service of the given type
 * - there is more than one service of the given type
 */
inline fun <reified T : ServiceImpl<T>> BackendFragmentImpl.service(): Lazy<T> =
    lazy {
        checkNotNull(adapter) { "this implementation is not part of an adaptive backend" }
            .single { it is BackendFragment && it.impl is T }
            .let { (it as BackendFragment) }
            .impl as T
    }

/**
 * Finds the worker of the given type [T].
 *
 * Throws exception when:
 *
 * - the fragment is not part of an adaptive backend
 * - there is no worker of the given type
 * - there is more than one worker of the given type
 */
inline fun <reified T : WorkerImpl<T>> BackendFragmentImpl.worker(): Lazy<T> =
    lazy {
        checkNotNull(adapter) { "this implementation is not part of an adaptive backend" }
            .single { it is BackendFragment && it.impl is T }
            .let { (it as BackendFragment) }
            .impl as T
    }

/**
 * Finds the worker of the given type [T]. Uses `null` if there is no such
 * fragment.
 *
 * Throws exception when:
 *
 * - the fragment is not part of an adaptive backend
 * - there is more than one worker of the given type
 */
inline fun <reified T : WorkerImpl<T>> BackendFragmentImpl.workerOrNull(): Lazy<T?> =
    lazy {
        checkNotNull(adapter) { "this implementation is not part of an adaptive backend" }
            .singleOrNull { it is BackendFragment && it.impl is T }
            ?.let { (it as BackendWorker).workerImpl as T }
    }

/**
 * Finds the service fragment with  of the given implementation type [T]. Uses `null`
 * if there is no such fragment.
 *
 * Throws exception when:
 *
 * - the fragment is not part of an adaptive backend
 * - there is more than one fragment of the given implementation type
 */
inline fun <reified T> BackendFragmentImpl.implOrNull(): Lazy<T?> =
    lazy {
        checkNotNull(adapter) { "this implementation is not part of an adaptive backend" }
            .singleOrNull { it is BackendFragment && it.impl is T }
            ?.let { (it as BackendFragment).impl as T }
    }