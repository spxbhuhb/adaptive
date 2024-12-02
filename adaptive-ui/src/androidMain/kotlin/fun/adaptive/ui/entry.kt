/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui

import android.content.Context
import android.view.ViewGroup
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveEntry
import `fun`.adaptive.foundation.AdaptiveFragmentFactory
import `fun`.adaptive.foundation.instruction.Trace
import `fun`.adaptive.service.transport.LocalServiceCallTransport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * The entry position of an Adaptive Android component tree.
 *
 * **IMPORTANT** variables declared outside the block are **NOT** reactive
 */
@AdaptiveEntry
fun android(
    context : Context,
    rootView : ViewGroup,
    vararg imports : AdaptiveFragmentFactory,
    backend: BackendAdapter = BackendAdapter(
        dispatcher = Dispatchers.Main,
        scope = CoroutineScope(Dispatchers.Main),
    transport = LocalServiceCallTransport()
    ),
    trace : Trace? = null,
    @Adaptive block: (adapter : AdaptiveAdapter) -> Unit
) : AuiAdapter =
    AuiAdapter(
        context, rootView, backend
    ).also {
        it.fragmentFactory += imports
        if (trace != null) { it.trace = trace.patterns }
        block(it)
        it.mounted()
    }