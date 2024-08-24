/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.graphics.svg.SvgAdapter
import `fun`.adaptive.graphics.svg.SvgFragment
import `fun`.adaptive.graphics.svg.render.SvgPathRenderData

@AdaptiveActual
class SvgPath(
    adapter: SvgAdapter,
    parent : AdaptiveFragment?,
    declarationIndex : Int
) : SvgFragment<SvgPathRenderData>(adapter, parent, declarationIndex, 0, 1) {

    val path = canvas.newPath()

    override fun newRenderData() = SvgPathRenderData()

    override fun genPatchInternal(): Boolean {
        super.genPatchInternal()
        renderData.commands.forEach { it.apply(path) }
        return false
    }

    override fun draw() {
        renderData.transform { it.forEach { t -> canvas.transform(t) } }
        renderData.fill { canvas.setFill(it) }

        canvas.fill(path)

        canvas.restore(id)
    }

}