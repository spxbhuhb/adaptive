/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.grapics.svg.render

import `fun`.adaptive.grapics.svg.instruction.Fill
import `fun`.adaptive.grapics.svg.instruction.SvgTransform

open class SvgRenderData {
    var fill : Fill = Fill("#000")
    var transform : MutableList<SvgTransform>? = null
}