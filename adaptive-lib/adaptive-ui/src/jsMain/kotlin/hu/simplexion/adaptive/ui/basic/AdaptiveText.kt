/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.basic

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.ui.dom.AdaptiveDOMNodeFragment
import org.w3c.dom.Node

class AdaptiveText(
    adapter: AdaptiveAdapter<Node>,
    parent : AdaptiveFragment<Node>,
    index : Int
) : AdaptiveDOMNodeFragment(adapter, parent, index, 1, true) {

    override val receiver = org.w3c.dom.Text()

    private val content: String get() = state[0]?.toString() ?: ""

    override fun genPatchInternal() {
        val closureMask = getThisClosureDirtyMask()

        if (haveToPatch(closureMask, 1)) {
            receiver.data = content
        }
    }

}