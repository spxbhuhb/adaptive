/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment

import android.view.View
import android.widget.TextView
import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.aui

@AdaptiveActual(aui)
class AuiText(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractAuiFragment<View>(adapter, parent, index, stateSize()) {

    override val receiver = TextView(adapter.context)

    private val content: Any?
        by stateVariable()

    override fun auiPatchInternal() {

        if (renderData.text == null) {
            renderData.text = uiAdapter.defaultTextRenderData
        }

        val safeContent = content?.toString() ?: ""
        val contentChange = (safeContent != receiver.text)
        val styleChange = (renderData.text != previousRenderData.text)

        if (! haveToPatch(content) && ! contentChange && ! styleChange) {
            return
        }

        if (contentChange) {
            receiver.text = safeContent
        }

        val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

        receiver.measure(widthSpec, heightSpec)

        renderData.innerWidth = receiver.measuredWidth.toDouble()
        renderData.innerHeight = receiver.measuredHeight.toDouble()

        return
    }

}