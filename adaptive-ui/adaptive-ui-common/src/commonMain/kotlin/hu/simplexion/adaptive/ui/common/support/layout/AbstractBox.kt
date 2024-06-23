/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.support.layout

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter

abstract class AbstractBox<RT, CRT : RT>(
    adapter: AbstractCommonAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AbstractStackFragment<RT, CRT>(
    adapter, parent, declarationIndex, 0, 2
) {

    override fun measure() {
        measure(
            { width, itemLeft, itemWidth -> maxOf(width, itemLeft + itemWidth) },
            { height, itemTop, itemHeight -> maxOf(height, itemTop + itemHeight) }
        )
        super.measure()
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