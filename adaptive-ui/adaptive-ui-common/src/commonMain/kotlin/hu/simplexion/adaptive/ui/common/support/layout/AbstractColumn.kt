/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.support.layout

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import kotlin.math.max

abstract class AbstractColumn<RT, CRT : RT>(
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
            width = max(width, frame.width)
            height += frame.height
        }

        renderData.measuredWidth = width
        renderData.measuredHeight = height

        return super.measure()
    }

    override fun layout(proposedFrame: RawFrame?) {
        calcLayoutFrame(proposedFrame)
        layoutStack(horizontal = false)
    }

}