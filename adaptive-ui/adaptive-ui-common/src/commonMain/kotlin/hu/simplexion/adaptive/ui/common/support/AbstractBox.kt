/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.support

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter

abstract class AbstractBox<RT, CRT : RT>(
    adapter: AbstractCommonAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AbstractContainerFragment<RT, CRT>(
    adapter, parent, declarationIndex, 0, 2
) {

    override fun measure(): RawSize =
        instructed() ?: measure(
            { w: Float, p: RawPoint, s: RawSize -> maxOf(w, p.top + s.width) },
            { h: Float, p: RawPoint, s: RawSize -> maxOf(h, p.left + s.height) }
        )

    override fun layout(proposedFrame: RawFrame) {
        calcLayoutFrame(proposedFrame)

        val layoutFrame = RawFrame(RawPoint.ORIGIN, this.layoutFrame.size)

        for (item in layoutItems) {
            item.layout(layoutFrame)
        }
    }

}