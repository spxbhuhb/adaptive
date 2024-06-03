/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.android.fragment

import android.view.View
import android.widget.TextView
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.android.adapter.AdaptiveAndroidAdapter
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.layout.RawFrame
import hu.simplexion.adaptive.ui.common.layout.RawSize

class AdaptiveText(
    adapter: AdaptiveAndroidAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveUIFragment<View>(adapter, parent, index, 1, 2) {

    override val receiver = TextView(adapter.context)

    private val content: String
        get() = state[0]?.toString() ?: ""

    override fun genPatchInternal(): Boolean {

        patchInstructions()

        if (haveToPatch(dirtyMask, 1)) {
            if (receiver.text != content || measuredSize == null) {
                receiver.text = content
                val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                receiver.measure(widthSpec, heightSpec)
                measuredSize = RawSize(receiver.measuredWidth.toFloat(), receiver.measuredHeight.toFloat())
            }
        }

        return false
    }

    override fun measure(): RawSize = instructedOr { measuredSize !! }

    override fun layout(proposedFrame: RawFrame) {
        calcLayoutFrame(proposedFrame)
        uiAdapter.applyLayoutToActual(this)
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveText"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveText(parent.adapter as AdaptiveAndroidAdapter, parent, index)

    }

}