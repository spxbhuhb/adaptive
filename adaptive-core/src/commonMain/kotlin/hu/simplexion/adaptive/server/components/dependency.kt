/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server.components

import hu.simplexion.adaptive.server.AdaptiveServerFragment
import hu.simplexion.adaptive.service.ServiceImpl

/**
 * Finds the store of the given type [T].
 *
 * Throws exception when:
 *
 * - the fragment is not part of an adaptive server
 * - there is no store of the given type
 * - there is more than one store of the given type
 */
inline fun <reified T : StoreImpl<T>> ServerFragmentImpl.store() : T =
    checkNotNull(serverAdapter) { "this implementation is not part of an adaptive server" }
        .rootFragment
        .filter { it is AdaptiveServerFragment && it.impl is T }
        .single() as T

/**
 * Finds the service of the given type [T].
 *
 * Throws exception when:
 *
 * - the fragment is not part of an adaptive server
 * - there is no service of the given type
 * - there is more than one service of the given type
 */
inline fun <reified T : ServiceImpl<T>> ServerFragmentImpl.service() : T =
    checkNotNull(serverAdapter) { "this implementation is not part of an adaptive server" }
        .rootFragment
        .filter { it is AdaptiveServerFragment && it.impl is T }
        .single() as T

/**
 * Finds the worker of the given type [T].
 *
 * Throws exception when:
 *
 * - the fragment is not part of an adaptive server
 * - there is no worker of the given type
 * - there is more than one worker of the given type
 */
inline fun <reified T : WorkerImpl<T>> ServerFragmentImpl.worker() : T =
    checkNotNull(serverAdapter) { "this implementation is not part of an adaptive server" }
        .rootFragment
        .filter { it is AdaptiveServerFragment && it.impl is T }
        .single() as T
