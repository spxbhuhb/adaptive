/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.canvas

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.resource.DrawableResource
import hu.simplexion.adaptive.ui.common.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.browser.AdaptiveBrowserAdapter
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.layout.RawFrame
import hu.simplexion.adaptive.ui.common.layout.RawSize
import hu.simplexion.adaptive.utility.checkIfInstance
import kotlinx.browser.document
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import kotlin.math.PI

open class AdaptiveCircle(
    adapter: AdaptiveCanvasAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveCanvasFragment(adapter, parent, index, -1, 0) {

    override fun draw(ctx : CanvasRenderingContext2D) {
        val centerX = 200.0
        val centerY = 200.0
        val radius = 100.0

        ctx.beginPath()
        ctx.arc(centerX, centerY, radius, 0.0, 2 * PI)
        ctx.fillStyle = "gray"
        ctx.fill()
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveCircle"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveCircle(parent.adapter as AdaptiveCanvasAdapter, parent, index)

    }

}