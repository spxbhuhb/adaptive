/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.foundation.fragment.AdaptiveAnonymous
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.ui.common.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.browser.AdaptiveBrowserAdapter
import hu.simplexion.adaptive.ui.common.browser.canvas.AdaptiveCanvasAdapter
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.layout.RawFrame
import hu.simplexion.adaptive.ui.common.layout.RawSize
import hu.simplexion.adaptive.utility.checkIfInstance
import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement

class AdaptiveCanvas(
    adapter: AdaptiveBrowserAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveUIFragment<HTMLElement>(adapter, parent, index, 0, 2) {

    override val receiver: HTMLCanvasElement =
        document.createElement("canvas") as HTMLCanvasElement

    val canvasAdapter = AdaptiveCanvasAdapter(adapter, receiver, this)

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

    override fun measure(): RawSize = instructedOr { RawSize.NaS }

    override fun layout(proposedFrame: RawFrame) {
        calcLayoutFrame(proposedFrame)

        val size = layoutFrame.size

        receiver.width = size.width.toInt()
        receiver.height = size.height.toInt()

        uiAdapter.applyLayoutToActual(this)
        canvasAdapter.draw()
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveCanvas"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveCanvas(parent.adapter as AdaptiveBrowserAdapter, parent, index)

    }

}