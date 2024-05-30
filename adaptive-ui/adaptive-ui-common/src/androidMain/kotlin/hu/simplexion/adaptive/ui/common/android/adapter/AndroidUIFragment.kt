/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.android.adapter

import android.view.View
import android.view.ViewGroup
import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.structural.AdaptiveAnonymous
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.instruction.Frame
import hu.simplexion.adaptive.utility.checkIfInstance

abstract class AndroidUIFragment(
    adapter: AdaptiveAndroidAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize: Int
) : AdaptiveUIFragment(adapter, parent, declarationIndex, instructionsIndex, stateSize) {

    val androidAdapter
        get() = adapter as AdaptiveAndroidAdapter

    override lateinit var receiver: View

    abstract fun makeReceiver(): View

    override fun create() {
        receiver = makeReceiver()
        super.create()
    }

    override fun layout(proposedFrame : Frame) {
        super.layout(proposedFrame)

        val layoutFrame = renderInstructions.layoutFrame

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