/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.graphics.canvas.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.graphics.canvas.CanvasAdapter
import `fun`.adaptive.graphics.canvas.CanvasFragment
import `fun`.adaptive.graphics.canvas.canvas
import `fun`.adaptive.graphics.canvas.model.path.PathCommand

@AdaptiveActual(canvas)
open class CanvasPath(
    adapter: CanvasAdapter,
    parent: AdaptiveFragment,
    index: Int,
) : CanvasFragment(adapter, parent, index, stateSize()) {

    var path = canvas.newPath()

    val commands: List<PathCommand>
        by stateVariable()

    override fun genPatchInternal(): Boolean {
        super.genPatchInternal()

        if (haveToPatch(commands)) {
            path = canvas.newPath()
            commands.forEach { it.apply(path) }
        }

        return false
    }

    override fun drawInner() {
        if (renderData?.fill != null) {
            canvas.fill(path)
        } else {
            canvas.stroke(path)
        }
    }

}