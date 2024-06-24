/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.support.layout

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import kotlin.math.max

abstract class AbstractWrapRow<RT, CRT : RT>(
    adapter: AbstractCommonAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AbstractWrapStack<RT, CRT>(
    adapter, parent, declarationIndex, 0, 2
) {

    override fun measure(): RawFrame {
        val frames = measureItems()

        val instructed = renderData.layout
        val widthLimit = instructed?.width ?: Double.MAX_VALUE

        var rowWidth = 0.0
        var rowHeight = 0.0

        var width = 0.0
        var height = 0.0

        for (frame in frames) {

            if (rowWidth + frame.width > widthLimit) {
                height += rowHeight
                rowWidth = frame.width
                rowHeight = frame.height
            } else {
                rowWidth += frame.width
                rowHeight = max(rowHeight, frame.height)
            }

            width = max(width, rowWidth)
        }

        renderData.measuredWidth = width
        renderData.measuredHeight = height

        return super.measure()
    }

    override fun layout(proposedFrame: RawFrame?) {
        calcLayoutFrame(proposedFrame)
        layoutStack(horizontal = true)
    }

}