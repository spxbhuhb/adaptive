/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.graphics.svg.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.graphics.canvas.CanvasAdapter
import `fun`.adaptive.graphics.canvas.fragment.CanvasSvg
import `fun`.adaptive.graphics.canvas.platform.ActualBrowserCanvas
import `fun`.adaptive.graphics.svg.svg
import `fun`.adaptive.resource.graphics.GraphicsResource
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.utility.checkIfInstance
import org.w3c.dom.HTMLElement

@AdaptiveActual(svg)
class SvgSvg(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractAuiFragment<HTMLElement>(adapter, parent, index, 1, 2) {

    val canvas = ActualBrowserCanvas()

    override val receiver: org.w3c.dom.HTMLCanvasElement = canvas.receiver

    val canvasAdapter = CanvasAdapter(adapter, canvas, this)

    override val patchDescendants: Boolean
        get() = true
    
    val resource: GraphicsResourceSet
        get() = state[0].checkIfInstance()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment {
        if (declarationIndex != 0) invalidIndex(declarationIndex)
        return CanvasSvg(canvasAdapter, this, declarationIndex).also { it.create() }
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        // FIXME hackish SGV patch
        (fragment as CanvasSvg).also {
            if (it.isInit || it.resource != resource) {
                it.resource = resource
                it.dirtyMask = it.dirtyMask or 1
            }
            it.state[it.instructionIndex] = instructions
            it.dirtyMask = it.dirtyMask or 2
        }
    }

    override fun auiPatchInternal() = Unit

    override fun placeLayout(top: Double, left: Double) {
        canvas.setSize(renderData.finalWidth, renderData.finalHeight)
        super.placeLayout(top, left)
    }
}