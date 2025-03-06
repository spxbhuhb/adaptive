/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas.render

import `fun`.adaptive.graphics.canvas.instruction.Fill
import `fun`.adaptive.graphics.canvas.instruction.Stroke

open class GraphicsRenderData {
    var fill: Fill? = null
    var stroke: Stroke? = null

    override fun toString(): String {
        return "GraphicsRenderData(fill=${fill?.color ?: "null"}, stroke=${stroke?.color ?: "null"})"
    }
}