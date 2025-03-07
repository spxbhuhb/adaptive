/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.graphics.svg.SvgAdapter
import `fun`.adaptive.graphics.svg.SvgFragment
import `fun`.adaptive.graphics.canvas.instruction.Scale
import `fun`.adaptive.graphics.canvas.instruction.Translate
import `fun`.adaptive.graphics.svg.render.SvgRootRenderData

@AdaptiveActual
class SvgRoot(
    adapter: SvgAdapter,
    parent : AdaptiveFragment?,
    declarationIndex : Int
) : SvgFragment<SvgRootRenderData>(adapter, parent, declarationIndex, stateSize()) {

    override fun newRenderData() = SvgRootRenderData()

    override fun draw() {
        val viewBox = renderData.viewBox
        val height = renderData.height
        val width = renderData.width

        if (trace) trace("draw", "$viewBox $width $height")

        if (viewBox != null && width != null && height != null) {
            val scaleX = width / viewBox.width
            val scaleY = height / viewBox.height

            val translateX = - viewBox.minX * scaleX
            val translateY = - viewBox.minY * scaleY

            canvas.transform(Translate(translateX, translateY))
            canvas.transform(Scale(scaleX, scaleY))

            if (trace) trace("draw", "$translateX $translateY $scaleX $scaleY")
        }

        super.draw()
    }
}