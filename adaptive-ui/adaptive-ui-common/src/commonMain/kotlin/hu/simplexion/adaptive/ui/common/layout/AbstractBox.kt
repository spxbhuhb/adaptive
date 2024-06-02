/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.layout

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIAdapter
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIContainerFragment

abstract class AbstractBox<CRT : RT,RT>(
    adapter: AdaptiveUIAdapter<CRT, RT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AdaptiveUIContainerFragment<CRT,RT>(
    adapter, parent, declarationIndex, 0, 2
) {

    override fun measure(): RawSize =
        measure(
            { w : Float, p : RawPoint, s : RawSize -> maxOf(w, p.top + s.width)},
            { h : Float, p : RawPoint, s : RawSize -> maxOf(h, p.left + s.height)}
        )

    override fun layout(proposedFrame : RawFrame) {
        calcLayoutFrame(proposedFrame)

        val layoutFrame = this.layoutFrame

        for (item in items) {
            item.layout(layoutFrame)
        }

        uiAdapter.applyLayoutToActual(this)
    }

}