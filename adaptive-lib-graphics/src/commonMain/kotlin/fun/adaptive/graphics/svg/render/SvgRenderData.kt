/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg.render

import `fun`.adaptive.graphics.canvas.render.CanvasRenderData
import `fun`.adaptive.ui.DensityIndependentAdapter

open class SvgRenderData(
    adapter : DensityIndependentAdapter
) : CanvasRenderData(adapter)