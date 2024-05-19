/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.ui.common.browser.adapter.AdaptiveBrowserFragment
import hu.simplexion.adaptive.ui.common.commonUI

class AdaptiveText(
    adapter: AdaptiveAdapter,
    parent : AdaptiveFragment,
    index : Int
) : AdaptiveBrowserFragment(adapter, parent, index, 1) {

    override val receiver = org.w3c.dom.Text()

    private val content: String get() = state[0]?.toString() ?: ""

    override fun genPatchInternal(): Boolean {
        val closureMask = getThisClosureDirtyMask()

        if (haveToPatch(closureMask, 1)) {
            receiver.data = content
        }

        return false
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveText"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveText(parent.adapter, parent, index)

    }

}