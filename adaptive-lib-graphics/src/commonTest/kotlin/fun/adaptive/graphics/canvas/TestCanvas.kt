/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas

import `fun`.adaptive.graphics.canvas.platform.ActualCanvas
import `fun`.adaptive.graphics.canvas.platform.ActualPath
import `fun`.adaptive.graphics.svg.instruction.SvgFill
import `fun`.adaptive.graphics.svg.instruction.transform.SvgTransform

class TestCanvas : ActualCanvas {

    val trace = mutableListOf<String>()

    override fun save(id: Long) {

    }

    override fun restore(id: Long) {

    }

    override fun draw(drawFun: () -> Unit) {

    }

    override fun newPath(): ActualPath {
        return TestPath(this)
    }

    override fun fill(path: ActualPath) {

    }

    override fun transform(t: SvgTransform) {

    }

    override fun setFill(fill: SvgFill) {

    }

    override fun clear() {

    }

}