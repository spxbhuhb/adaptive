/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.fragment

import android.view.View
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.widget.TextView
import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.CommonAdapter
import hu.simplexion.adaptive.ui.common.common
import hu.simplexion.adaptive.ui.common.support.RawFrame
import hu.simplexion.adaptive.ui.common.support.RawSize

@AdaptiveActual(common)
class CommonText(
    adapter: CommonAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractCommonFragment<View>(adapter, parent, index, 1, 2) {

    override val receiver = TextView(adapter.context)

    private val content: String
        get() = state[0]?.toString() ?: ""

    override fun genPatchInternal(): Boolean {

        patchInstructions()

        if (haveToPatch(dirtyMask, 1)) {
            if (receiver.text != content && measuredSize != RawSize.UNKNOWN) {
                receiver.text = content

                val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

                receiver.measure(widthSpec, heightSpec)

                measuredSize = RawSize(receiver.measuredWidth.toDouble(), receiver.measuredHeight.toDouble())
            }
        }

        return false
    }

    override fun measure(): RawSize = instructedOr { measuredSize ?: RawSize.UNKNOWN }

    override fun layout(proposedFrame: RawFrame?) {
        calcLayoutFrame(proposedFrame)
        uiAdapter.applyLayoutToActual(this)
    }

}