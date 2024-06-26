/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.graphics.canvas.platform.ActualBrowserCanvas
import hu.simplexion.adaptive.grapics.canvas.CanvasAdapter
import hu.simplexion.adaptive.grapics.canvas.fragment.CanvasSvg
import hu.simplexion.adaptive.resource.DrawableResource
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.CommonAdapter
import hu.simplexion.adaptive.ui.common.common
import hu.simplexion.adaptive.utility.checkIfInstance
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement

@AdaptiveActual(common)
class CommonSvg(
    adapter: CommonAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractCommonFragment<HTMLElement>(adapter, parent, index, 1, 2) {

    val canvas = ActualBrowserCanvas()

    override val receiver: HTMLCanvasElement = canvas.receiver

    val canvasAdapter = CanvasAdapter(adapter, canvas, this)

    val resource: DrawableResource
        get() = state[0].checkIfInstance()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {
        if (declarationIndex != 0) invalidIndex(declarationIndex)
        return CanvasSvg(canvasAdapter, this, declarationIndex).also { it.create() }
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        (fragment as CanvasSvg).resource = resource
    }

    override fun genPatchInternal(): Boolean {
        patchInstructions()
        return false
    }

    override fun placeLayout(top: Double, left: Double) {
        canvas.setSize(renderData.finalWidth, renderData.finalHeight)
        super.placeLayout(top, left)
    }
}