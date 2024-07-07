/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.grapics.svg.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.grapics.svg.SvgAdapter
import hu.simplexion.adaptive.grapics.svg.SvgFragment
import hu.simplexion.adaptive.grapics.svg.instruction.Scale
import hu.simplexion.adaptive.grapics.svg.instruction.Translate
import hu.simplexion.adaptive.grapics.svg.render.SvgRootRenderData

@AdaptiveActual
class SvgRoot(
    adapter: SvgAdapter,
    parent : AdaptiveFragment?,
    declarationIndex : Int
) : SvgFragment<SvgRootRenderData>(adapter, parent, declarationIndex, 0, 1) {

    override fun newRenderData() = SvgRootRenderData()

    override fun draw() {
        val viewBox = renderData.viewBox
        val height = renderData.height
        val width = renderData.width

        println("$viewBox $width $height")
        if (viewBox != null && width != null && height != null) {
            val scaleX = width / viewBox.width
            val scaleY = height / viewBox.height

            val translateX = - viewBox.minX * scaleX
            val translateY = - viewBox.minY * scaleY

            canvas.transform(Translate(translateX, translateY))
            canvas.transform(Scale(scaleX, scaleY))

            println("$translateX $translateY $scaleX $scaleY")
        }

        super.draw()
    }
}