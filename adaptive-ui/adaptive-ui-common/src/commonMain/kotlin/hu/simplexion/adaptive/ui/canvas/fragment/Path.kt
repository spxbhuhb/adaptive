/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.canvas.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.lib.grapics.canvas.ActualCanvas
import hu.simplexion.adaptive.ui.common.canvas
import hu.simplexion.adaptive.ui.svg.SvgAdapter
import hu.simplexion.adaptive.ui.svg.SvgFragment

@AdaptiveActual(canvas)
open class Path(
    adapter: SvgAdapter,
    parent: AdaptiveFragment,
    index: Int
) : SvgFragment(adapter, parent, index, - 1, 0) {

    override fun draw(ctx: ActualCanvas) {

    }

}