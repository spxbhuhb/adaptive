/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.common.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment.AdaptiveAnonymous
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.graphics.canvas.platform.ActualBrowserCanvas
import `fun`.adaptive.grapics.canvas.CanvasAdapter
import `fun`.adaptive.ui.common.AbstractCommonFragment
import `fun`.adaptive.ui.common.CommonAdapter
import `fun`.adaptive.ui.common.common
import `fun`.adaptive.utility.checkIfInstance
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

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment {
        if (declarationIndex != 0) invalidIndex(declarationIndex)
        return AdaptiveAnonymous.switchAdapter(canvasAdapter, this, declarationIndex, 0, content).apply { create() }
    }

    override fun genPatchInternal(): Boolean {
        patchInstructions()
        return false
    }

    override fun placeLayout(top: Double, left: Double) {
        val data = renderData

        canvas.setSize(data.innerWidth ?: 0.0, data.innerHeight ?: 0.0)

        uiAdapter.applyLayoutToActual(this)
        canvasAdapter.draw()
    }

}