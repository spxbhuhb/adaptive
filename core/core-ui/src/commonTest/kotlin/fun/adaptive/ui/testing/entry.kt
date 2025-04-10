/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.testing

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveEntry
import `fun`.adaptive.foundation.AdaptiveFragmentFactory
import `fun`.adaptive.foundation.instruction.Trace
import `fun`.adaptive.ui.fragment.layout.RawFrame

@AdaptiveEntry
fun uiTest(
    top: Int,
    left: Int,
    width: Int,
    height: Int,
    vararg imports : AdaptiveFragmentFactory,
    trace : Trace? = null,
    @Adaptive block: (adapter: AuiTestAdapter) -> Unit
) : AuiTestAdapter {

    return AuiTestAdapter(
        TestReceiver(RawFrame(top.toDouble(), left.toDouble(), width.toDouble(), height.toDouble()))
    ).also {
        it.fragmentFactory += imports
        if (trace != null) { it.trace = trace.patterns }
        block(it)
        it.mounted()
    }

}