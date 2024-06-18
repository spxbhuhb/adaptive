/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.fragment.AdaptiveAnonymous
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.graphics.canvas.platform.ActualBrowserCanvas
import hu.simplexion.adaptive.grapics.canvas.CanvasAdapter
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.CommonAdapter
import hu.simplexion.adaptive.ui.common.common
import hu.simplexion.adaptive.ui.common.support.RawFrame
import hu.simplexion.adaptive.utility.checkIfInstance
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement

@AdaptiveActual(common)
class CommonCanvas(
    adapter: CommonAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractCommonFragment<HTMLElement>(adapter, parent, index, 0, 2) {

    val canvas = ActualBrowserCanvas()

    override val receiver: HTMLCanvasElement = canvas.receiver

    val canvasAdapter = CanvasAdapter(adapter, canvas, this)

    val content: BoundFragmentFactory
        get() = state[state.size - 1].checkIfInstance()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {
        if (declarationIndex != 0) invalidIndex(declarationIndex)
        return AdaptiveAnonymous.switchAdapter(canvasAdapter, this, declarationIndex, 0, content).apply { create() }
    }

    override fun genPatchInternal(): Boolean {
        patchInstructions()
        return false
    }

    override fun layout(proposedFrame: RawFrame?) {
        calcLayoutFrame(proposedFrame)

        canvas.setSize(layoutFrame.width, layoutFrame.height)

        uiAdapter.applyLayoutToActual(this)
        canvasAdapter.draw()
    }

}