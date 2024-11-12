/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.graphics.canvas.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment.AdaptiveAnonymous
import `fun`.adaptive.foundation.instruction.Trace
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.graphics.canvas.platform.ActualBrowserCanvas
import `fun`.adaptive.graphics.canvas.CanvasAdapter
import `fun`.adaptive.graphics.canvas.canvas
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.utility.checkIfInstance
import `fun`.adaptive.utility.firstOrNullIfInstance
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement

@AdaptiveActual(canvas)
class CanvasCanvas(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractAuiFragment<HTMLElement>(adapter, parent, index, 0, 2) {

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

        instructions.firstOrNullIfInstance<Trace>()?.let {
            canvasAdapter.trace = it.patterns
        }

        return true
    }

    override fun patchInternal() {
        super.patchInternal()
        canvasAdapter.draw()
    }

    override fun placeLayout(top: Double, left: Double) {
        val data = renderData

        canvas.setSize(data.finalWidth, data.finalWidth)

        uiAdapter.applyLayoutToActual(this)
        canvasAdapter.draw()
    }

}