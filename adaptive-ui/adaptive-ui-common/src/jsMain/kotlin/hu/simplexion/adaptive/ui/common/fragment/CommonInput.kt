/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.CommonAdapter
import hu.simplexion.adaptive.ui.common.common
import hu.simplexion.adaptive.ui.common.support.RawFrame
import hu.simplexion.adaptive.ui.common.support.RawSize
import hu.simplexion.adaptive.utility.checkIfInstance
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement

@AdaptiveActual(common)
open class CommonInput(
    adapter: CommonAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractCommonFragment<HTMLElement>(adapter, parent, index, 0, 2) {

    // 0 - instructions
    // 1 - binding

    override val receiver: HTMLInputElement =
        document.createElement("input") as HTMLInputElement

    private val binding: AdaptiveStateVariableBinding<String>
        get() = state[1].checkIfInstance()

    override fun genPatchInternal(): Boolean {

        val b = binding

        patchInstructions()

        if (haveToPatch(dirtyMask, 1)) {
            receiver.value = b.value
        }

        if (isInit) {
            receiver.addEventListener("input", {
                if (receiver.value != b.value) {
                    b.setValue(receiver.value, true)
                }
            })

            receiver.style.outline = "none"
            b.path?.let { receiver.placeholder = it.last() }
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