/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.support.layout

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import kotlin.math.max

abstract class AbstractBox<RT, CRT : RT>(
    adapter: AbstractCommonAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AbstractFixStack<RT, CRT>(
    adapter, parent, declarationIndex, 0, 2
) {

    override fun measure(): RawFrame {
        val frames = measureItems()

        var width = 0.0
        var height = 0.0

        for (frame in frames) {
            val right = if (frame.left.isNaN()) frame.width else frame.left + frame.width
            val bottom = if (frame.top.isNaN()) frame.height else frame.top + frame.height

            width = max(width, right)
            height = max(height, bottom)
        }

        renderData.measuredWidth = width
        renderData.measuredHeight = height

        return super.measure()
    }

    override fun layout(proposedFrame: RawFrame?) {
        calcLayoutFrame(proposedFrame)

        val surrounding = surrounding()
        val availableWidth = layoutFrame.width - surrounding.left - surrounding.right
        val availableHeight = layoutFrame.height - surrounding.top - surrounding.bottom

        val layoutFrame = RawFrame(0.0, 0.0, availableWidth, availableHeight)

        for (item in layoutItems) {
            item.layout(layoutFrame)
        }
    }

}