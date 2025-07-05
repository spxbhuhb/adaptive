/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.graphics.canvas.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment.AdaptiveAnonymous
import `fun`.adaptive.foundation.instruction.Trace
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.graphics.canvas.CanvasAdapter
import `fun`.adaptive.graphics.canvas.canvas
import `fun`.adaptive.graphics.canvas.platform.ActualBrowserCanvas
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiBrowserAdapter
import `fun`.adaptive.ui.fragment.layout.RawSize
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement

@AdaptiveActual(canvas)
class CanvasCanvas(
    adapter: AuiBrowserAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractAuiFragment<HTMLElement>(adapter, parent, index, stateSize()), ActualCanvasOwner {

    override val canvas = ActualBrowserCanvas()

    override val receiver: HTMLCanvasElement = canvas.receiver

    val canvasAdapter = CanvasAdapter(adapter, canvas, this)

    override val patchDescendants: Boolean
        get() = true

    val content: BoundFragmentFactory
        by stateVariable()

    var size = RawSize.ZERO

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? =
        null

    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        fragment.setStateVariable(1, size)
    }

    // This fragment has to catch actuals and add them to the canvas
    // instead the parent fragment.

    override fun addActual(fragment: AdaptiveFragment, direct: Boolean?) {
        canvasAdapter.addActualRoot(fragment)
    }

    override fun removeActual(fragment: AdaptiveFragment, direct: Boolean?) {
        canvasAdapter.removeActualRoot(fragment)
    }

    override fun auiPatchInternal() {
        instructions.firstInstanceOfOrNull<Trace>()?.let {
            canvasAdapter.trace = it.patterns
        }
    }

    override fun patchInternal() {
        super.patchInternal()
        if (size.width > 0 && size.height > 0 && isMounted && canvas.shouldRedraw) {
            canvasAdapter.draw()
            canvas.shouldRedraw = false
        }
    }

    override fun placeLayout(top: Double, left: Double) {
        val data = renderData

        data.finalTop = top
        data.finalLeft = left

        val canvasWidth = data.finalWidth - data.surroundingHorizontal
        val canvasHeight = data.finalHeight - data.surroundingVertical

        canvas.setSize(canvasWidth, canvasHeight)

        uiAdapter.applyLayoutToActual(this)

        // building the fragment tree just before the first draw avoids rebuild caused by size calculations

        if (children.isEmpty()) {
            size = RawSize(canvasWidth, canvasHeight)
            children += AdaptiveAnonymous.switchAdapter(canvasAdapter, this, 0, 2, content)
                .apply {
                    create()
                    mount()
                }
        } else {
            if (size.width != canvasWidth || size.height != canvasHeight) {
                size = RawSize(canvasWidth, canvasHeight)
                children.first().also {
                    it.setStateVariable(1, size)
                    it.patch()
                }
            }
        }

        if (canvas.shouldRedraw) {
            canvasAdapter.draw()
            canvas.shouldRedraw = false
        }
    }

}