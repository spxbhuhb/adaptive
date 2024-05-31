/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.android.adapter

import android.view.View
import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.instruction.Frame

abstract class AndroidUIFragment(
    override val adapter: AdaptiveAndroidAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize: Int
) : AdaptiveUIFragment(adapter, parent, declarationIndex, instructionsIndex, stateSize) {

    abstract override val receiver: View

    override fun layout(proposedFrame : Frame) {
        super.layout(proposedFrame)

        val layoutFrame = renderData.layoutFrame

        check(layoutFrame !== Frame.NaF) { "Missing layout frame in $this" }

        val point = layoutFrame.point
        val size = layoutFrame.size
        val top = point.top.toInt()
        val left = point.left.toInt()

        receiver.layout(
            left,
            top,
            left + size.width.toInt(),
            top + size.height.toInt()
        )
    }

}