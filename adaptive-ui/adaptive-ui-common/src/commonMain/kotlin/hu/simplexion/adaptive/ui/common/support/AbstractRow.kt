/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.support

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter

/**
 * @param autoSizing When true, the row accepts unknown item sizes. This is useful for browser when the size of
 *                   text items is unknown.
 */
abstract class AbstractRow<RT, CRT : RT>(
    adapter: AbstractCommonAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    val autoSizing: Boolean
) : AbstractContainerFragment<RT, CRT>(
    adapter, parent, declarationIndex, 0, 2
) {

    override fun measure(): RawSize =
        instructed() ?: measure(
            { w: Float, _, s: RawSize -> w + s.width },
            { h: Float, _, s: RawSize -> maxOf(h, s.height) }
        )

    override fun layout(proposedFrame: RawFrame) {
        calcLayoutFrame(proposedFrame)

        layoutStack(horizontal = true, autoSizing)
    }

}