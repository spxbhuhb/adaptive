/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas.render

import `fun`.adaptive.graphics.canvas.instruction.Fill
import `fun`.adaptive.graphics.canvas.instruction.Stroke
import `fun`.adaptive.ui.instruction.decoration.Color

open class GraphicsRenderData {
    var fill: Fill = Fill(Color(0x0u))
    var stroke : Stroke = Stroke(Color(0x0u))
}