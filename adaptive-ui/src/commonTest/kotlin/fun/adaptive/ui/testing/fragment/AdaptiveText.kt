/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.testing.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.render.model.AuiRenderData
import `fun`.adaptive.ui.testing.AuiTestAdapter
import `fun`.adaptive.ui.testing.TestReceiver

@AdaptiveActual("test")
open class AdaptiveText(
    adapter: AuiTestAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractAuiFragment<TestReceiver>(adapter, parent, index, stateSize()) {

    override val receiver = TestReceiver()

    private val content: Any?
        by stateVariable()

    override fun auiPatchInternal() {
        val safeContent = content?.toString() ?: ""
        val contentChange = (isInit || safeContent != receiver.textContent)
        val styleChange = (renderData.text != previousRenderData.text)

        if (! haveToPatch(content) && ! contentChange && ! styleChange) {
            return
        }

        if (contentChange) {
            receiver.textContent = safeContent
        }

        if (renderData === previousRenderData) {
            renderData = AuiRenderData(uiAdapter, previousRenderData, uiAdapter.themeFor(this), instructions)
            if (renderData.text == null) {
                renderData.text = uiAdapter.defaultTextRenderData
            }
        }

        measureText(safeContent)
    }

    fun measureText(content: String) {
        renderData.innerWidth = content.length.toDouble() * 20.0
        renderData.innerHeight = 20.0
    }
}