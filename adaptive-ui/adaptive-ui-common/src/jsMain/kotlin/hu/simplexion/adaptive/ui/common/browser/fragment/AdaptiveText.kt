/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.browser.AdaptiveBrowserAdapter
import hu.simplexion.adaptive.ui.common.common
import hu.simplexion.adaptive.ui.common.layout.RawFrame
import hu.simplexion.adaptive.ui.common.layout.RawSize
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLSpanElement

@AdaptiveActual(common)
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

        patchInstructions()

        if (haveToPatch(dirtyMask, 1)) {
            receiver.textContent = content
        }

        return false
    }


    /**
     * In web browsers measuring text is not the usual way.
     */
    override fun measure() = instructedOr { RawSize.NaS }

    override fun layout(proposedFrame: RawFrame) {
        calcLayoutFrame(proposedFrame)
        uiAdapter.applyLayoutToActual(this)
    }

}