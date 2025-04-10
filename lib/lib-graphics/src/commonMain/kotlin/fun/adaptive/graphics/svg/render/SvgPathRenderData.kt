/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg.render

import `fun`.adaptive.graphics.canvas.model.path.PathCommand
import `fun`.adaptive.graphics.svg.SvgAdapter

class SvgPathRenderData(
    adapter: SvgAdapter
) : SvgRenderData(adapter) {
    var commands: List<PathCommand> = emptyList()
}