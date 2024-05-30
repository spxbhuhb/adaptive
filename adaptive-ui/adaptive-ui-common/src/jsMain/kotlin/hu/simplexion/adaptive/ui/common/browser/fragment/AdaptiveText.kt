/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.browser.adapter.BrowserUIFragment
import hu.simplexion.adaptive.ui.common.instruction.Frame
import kotlinx.browser.document
import org.w3c.dom.HTMLElement

open class AdaptiveText(
    adapter: AdaptiveAdapter,
    parent : AdaptiveFragment,
    index : Int
) : BrowserUIFragment(adapter, parent, index, 1, 2) {

    private val content: String
        get() = state[0]?.toString() ?: ""

    override fun genPatchInternal(): Boolean {
        val closureMask = getThisClosureDirtyMask()

        if (haveToPatch(closureMask, 1)) {
            receiver.textContent = content
        }

        if (haveToPatch(closureMask, 1 shl instructionIndex)) {
            applyRenderInstructions()
        }

        return false
    }

    override fun makeReceiver(): HTMLElement =
        document.createElement("span") as HTMLElement

    /**
     * In web browsers measuring text is not the usual way.
     */
    override fun measure() = Unit

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveText"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveText(parent.adapter, parent, index)

    }

}