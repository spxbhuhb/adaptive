/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.backend

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveEntry
import `fun`.adaptive.service.transport.LocalServiceCallTransport
import `fun`.adaptive.service.transport.ServiceCallTransport
import kotlinx.coroutines.Dispatchers

/**
 * The entry point of a backend component tree.
 *
 * **IMPORTANT** variables declared outside the block are **NOT** reactive
 */
@AdaptiveEntry
fun backend(transport: ServiceCallTransport = LocalServiceCallTransport(), wait : Boolean = false, @Adaptive block: (adapter : BackendAdapter) -> Unit) : BackendAdapter =
    BackendAdapter(wait, transport, Dispatchers.Default).also {
        block(it)
        it.mounted()
    }