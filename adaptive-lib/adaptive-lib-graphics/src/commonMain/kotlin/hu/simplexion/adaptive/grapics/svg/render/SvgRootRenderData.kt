/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.grapics.svg.render

import hu.simplexion.adaptive.grapics.svg.instruction.ViewBox

class SvgRootRenderData : SvgRenderData() {
    var width: Double? = null
    var height: Double? = null
    var viewBox : ViewBox? = null
}