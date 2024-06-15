/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveEntry
import hu.simplexion.adaptive.foundation.AdaptiveFragmentFactory
import hu.simplexion.adaptive.foundation.instruction.Trace
import platform.UIKit.UIView

/**
 * The entry point of an Adaptive iOS component tree.
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