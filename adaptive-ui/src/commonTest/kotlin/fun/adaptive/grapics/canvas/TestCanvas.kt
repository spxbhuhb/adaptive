/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.grapics.canvas

import `fun`.adaptive.grapics.canvas.platform.ActualCanvas
import `fun`.adaptive.grapics.canvas.platform.ActualPath
import `fun`.adaptive.grapics.svg.instruction.Fill
import `fun`.adaptive.grapics.svg.instruction.SvgTransform

class TestCanvas : ActualCanvas {
    override fun startDraw() {
        TODO("Not yet implemented")
    }

    override fun endDraw() {
        TODO("Not yet implemented")
    }

    override fun save(id: Long) {
        TODO("Not yet implemented")
    }

    override fun restore(id: Long) {
        TODO("Not yet implemented")
    }

    override fun newPath(): ActualPath {
        TODO("Not yet implemented")
    }

    override fun fill(path: ActualPath) {
        TODO("Not yet implemented")
    }

    override fun transform(t: SvgTransform) {
        TODO("Not yet implemented")
    }

    override fun setFill(fill: Fill) {
        TODO("Not yet implemented")
    }
}