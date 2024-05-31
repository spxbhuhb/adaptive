/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.uikit.adapter

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.instruction.Frame
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIView

abstract class IosUIFragment(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionIndex: Int,
    stateSize: Int
) : AdaptiveUIFragment(adapter, parent, declarationIndex, instructionIndex, stateSize) {

    override lateinit var receiver: UIView

    abstract fun makeReceiver(): UIView

    override fun create() {
        receiver = makeReceiver()
        super.create()
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun layout(proposedFrame : Frame) {
        super.layout(proposedFrame)

        val layoutFrame = renderData.layoutFrame

        check(layoutFrame !== Frame.NaF) { "Missing layout frame in $this" }

        val point = layoutFrame.point
        val size = layoutFrame.size
        val top = point.top.toInt()
        val left = point.left.toInt()

        receiver.setFrame(
            CGRectMake(left.toDouble(), top.toDouble(), size.width.toDouble(), size.height.toDouble())
        )
    }
}