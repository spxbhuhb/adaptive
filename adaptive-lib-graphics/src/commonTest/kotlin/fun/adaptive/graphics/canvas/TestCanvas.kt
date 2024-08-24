/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas

import `fun`.adaptive.graphics.canvas.platform.ActualCanvas
import `fun`.adaptive.graphics.canvas.platform.ActualPath
import `fun`.adaptive.graphics.svg.instruction.Fill
import `fun`.adaptive.graphics.svg.instruction.SvgTransform

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