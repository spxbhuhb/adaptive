/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.grapics.svg.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.grapics.svg.SvgAdapter
import `fun`.adaptive.grapics.svg.SvgFragment
import `fun`.adaptive.grapics.svg.render.SvgRenderData

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