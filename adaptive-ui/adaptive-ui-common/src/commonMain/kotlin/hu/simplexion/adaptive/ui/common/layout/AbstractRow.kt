/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.layout

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AdaptiveUIAdapter
import hu.simplexion.adaptive.ui.common.AdaptiveUIContainerFragment

/**
 * @param autoSizing When true, the row accepts unknown item sizes. This is useful for browser when the size of
 *                   text items is unknown.
 */
abstract class AbstractRow<RT, CRT : RT>(
    adapter: AdaptiveUIAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    val autoSizing: Boolean
) : AdaptiveUIContainerFragment<RT, CRT>(
    adapter, parent, declarationIndex, 0, 2
) {

    override fun measure(): RawSize =
        measure(
            { w: Float, _, s: RawSize -> w + s.width },
            { h: Float, _, s: RawSize -> maxOf(h, s.height) }
        )

    override fun layout(proposedFrame: RawFrame) {
        calcLayoutFrame(proposedFrame)

        layoutStack(horizontal = true, autoSizing)

        uiAdapter.applyLayoutToActual(this)
    }

}