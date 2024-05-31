/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.layout

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIAdapter
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIContainerFragment
import hu.simplexion.adaptive.ui.common.instruction.Frame
import hu.simplexion.adaptive.ui.common.instruction.Point
import hu.simplexion.adaptive.ui.common.instruction.Size

abstract class AbstractColumn<CRT : RT,RT>(
    adapter: AdaptiveUIAdapter<CRT, RT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AdaptiveUIContainerFragment<CRT,RT>(
    adapter, parent, declarationIndex, 0, 2
) {

    override fun measure(): Size =
        measure(
            { w : Float, _ : Point, s : Size -> maxOf(w, s.width)},
            { h : Float, _ : Point, s : Size -> h + s.height }
        )

    override fun layout(proposedFrame : Frame) {
        setLayoutFrame(proposedFrame)

        val layoutFrame = renderData.layoutFrame

        for (item in items) {
            item.layout(layoutFrame)
        }
    }

}