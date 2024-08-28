/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg.render

import `fun`.adaptive.graphics.svg.instruction.Fill
import `fun`.adaptive.graphics.svg.instruction.transform.SvgTransform
import `fun`.adaptive.ui.instruction.decoration.Color

open class SvgRenderData {
    var fill: Fill = Fill(Color(0x0u))
    var transform : MutableList<SvgTransform>? = null
}