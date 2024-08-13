/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.common

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveEntry
import `fun`.adaptive.foundation.AdaptiveFragmentFactory
import `fun`.adaptive.foundation.instruction.Trace
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
    trace: Trace? = null,
    @Adaptive block: (adapter : AdaptiveAdapter) -> Unit
) : CommonAdapter =
    CommonAdapter(
        rootView
    ).also {
        it.fragmentFactory += imports
        if (trace != null) { it.trace = trace.patterns }
        block(it)
        it.mounted()
    }