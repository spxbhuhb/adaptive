/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.foundation.testing

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveEntry
import `fun`.adaptive.service.testing.TestServiceTransport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * The general entry point of an Adaptive test component tree.
 *
 * **IMPORTANT** variables declared outside the block are **NOT** reactive
 */
@AdaptiveEntry
fun test(
    backendAdapter: BackendAdapter = BackendAdapter(
        transport = TestServiceTransport(),
        dispatcher = Dispatchers.Default,
        scope = CoroutineScope(Dispatchers.Default)
    ),
    printTrace: Boolean = false,
    block: @Adaptive (adapter: AdaptiveAdapter) -> Unit
): AdaptiveTestAdapter =
    AdaptiveTestAdapter(printTrace, backendAdapter).also {
        block(it)
        it.mounted()
    }