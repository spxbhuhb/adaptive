/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.adapter.RenderData
import hu.simplexion.adaptive.ui.common.browser.adapter.AdaptiveBrowserAdapter
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.instruction.Size
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLSpanElement

open class AdaptiveText(
    adapter: AdaptiveBrowserAdapter,
    parent : AdaptiveFragment,
    index : Int
) : AdaptiveUIFragment<HTMLElement>(adapter, parent, index, 1, 2) {

    override val receiver: HTMLSpanElement =
        document.createElement("span") as HTMLSpanElement

    private val content: String
        get() = state[0]?.toString() ?: ""

    override fun genPatchInternal(): Boolean {
        val closureMask = getThisClosureDirtyMask()

        if (haveToPatch(closureMask, 1)) {
            receiver.textContent = content
        }

        if (haveToPatch(closureMask, 1 shl instructionIndex)) {
            renderData = RenderData(instructions)
            uiAdapter.applyRenderInstructions(this)
        }

        return false
    }


    /**
     * In web browsers measuring text is not the usual way.
     */
    override fun measure() : Size? {
        traceMeasure()
        return Size(0f,0f)
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveText"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveText(parent.adapter as AdaptiveBrowserAdapter, parent, index)

    }

}