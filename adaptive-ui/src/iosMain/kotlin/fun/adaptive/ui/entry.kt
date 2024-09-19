/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveEntry
import `fun`.adaptive.foundation.AdaptiveFragmentFactory
import `fun`.adaptive.foundation.instruction.Trace
import `fun`.adaptive.service.transport.LocalServiceCallTransport
import kotlinx.coroutines.Dispatchers
import platform.UIKit.UIView

/**
 * The entry position of an Adaptive iOS component tree.
 *
 * **IMPORTANT** variables declared outside the block are **NOT** reactive
 */
@AdaptiveEntry
fun ios(
    rootView : UIView,
    vararg imports : AdaptiveFragmentFactory,
    backend: BackendAdapter = BackendAdapter(
        dispatcher = Dispatchers.Main,
        transport = LocalServiceCallTransport()
    ),
    trace: Trace? = null,
    @Adaptive block: (adapter : AdaptiveAdapter) -> Unit
) : AuiAdapter =
    AuiAdapter(
        rootView, backend
    ).also {
        it.fragmentFactory += imports
        if (trace != null) { it.trace = trace.patterns }
        block(it)
        it.mounted()
    }