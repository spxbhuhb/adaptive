/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.CommonAdapter
import hu.simplexion.adaptive.ui.common.common
import hu.simplexion.adaptive.ui.common.support.RawFrame
import hu.simplexion.adaptive.ui.common.support.RawSize
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLSpanElement

@AdaptiveActual(common)
open class CommonText(
    adapter: CommonAdapter,
    parent : AdaptiveFragment,
    index : Int
) : AbstractCommonFragment<HTMLElement>(adapter, parent, index, 1, 2) {

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
    override fun measure() = instructedOr { RawSize.UNKNOWN }

    override fun layout(proposedFrame: RawFrame?) {
        calcLayoutFrame(proposedFrame)
        uiAdapter.applyLayoutToActual(this)
    }

}