/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.grapics.svg.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveExpect
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.grapics.svg.SvgAdapter
import hu.simplexion.adaptive.grapics.svg.SvgFragment
import hu.simplexion.adaptive.grapics.svg.render.SvgRenderData
import hu.simplexion.adaptive.grapics.svg.svg

@AdaptiveActual
class SvgGroup(
    adapter: SvgAdapter,
    parent : AdaptiveFragment?,
    declarationIndex : Int
) : SvgFragment<SvgRenderData>(adapter, parent, declarationIndex, 0, 1) {

    override fun newRenderData() = SvgRenderData()

    override fun draw() {
        renderData.transform {
            canvas.save(id)
            it.forEach { t -> canvas.transform(t) }
        }

        super.draw()

        canvas.restore(id)
    }

}