/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.testing

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveEntry
import hu.simplexion.adaptive.foundation.AdaptiveFragmentFactory
import hu.simplexion.adaptive.foundation.instruction.Trace
import hu.simplexion.adaptive.ui.common.layout.RawFrame

@AdaptiveEntry
fun uiTest(
    top: Int,
    left: Int,
    width: Int,
    height: Int,
    vararg imports : AdaptiveFragmentFactory,
    trace : Trace? = null,
    @Adaptive block: (adapter : AdaptiveAdapter) -> Unit
) : AdaptiveUITestAdapter {

    return AdaptiveUITestAdapter(
        TestReceiver(RawFrame(top.toFloat(), left.toFloat(), width.toFloat(), height.toFloat()))
    ).also {
        it.fragmentFactory += imports
        if (trace != null) { it.trace = trace.patterns }
        block(it)
        it.mounted()
    }

}