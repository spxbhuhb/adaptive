/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.android.fragment

import android.view.View
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.widget.TextView
import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.android.AdaptiveAndroidAdapter
import hu.simplexion.adaptive.ui.common.common
import hu.simplexion.adaptive.ui.common.layout.RawFrame
import hu.simplexion.adaptive.ui.common.layout.RawSize

@AdaptiveActual(common)
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
            if (receiver.text != content && measuredSize != RawSize.NaS) {
                receiver.text = content

                val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

                receiver.measure(widthSpec, heightSpec)

                measuredSize = RawSize(receiver.measuredWidth.toFloat(), receiver.measuredHeight.toFloat())
            }
        }

        return false
    }

    override fun measure(): RawSize = instructedOr { measuredSize }

    override fun layout(proposedFrame: RawFrame) {
        calcLayoutFrame(proposedFrame)
        receiver.textAlignment = TEXT_ALIGNMENT_CENTER // FIXME hard coded text alignment
        uiAdapter.applyLayoutToActual(this)
    }

}