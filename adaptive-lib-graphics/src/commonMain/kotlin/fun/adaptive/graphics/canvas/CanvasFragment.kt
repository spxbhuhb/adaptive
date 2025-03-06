/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.graphics.canvas.platform.ActualCanvas
import `fun`.adaptive.graphics.canvas.render.CanvasRenderData

abstract class CanvasFragment(
    adapter: CanvasAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    stateSize: Int
) : AdaptiveFragment(adapter, parent, declarationIndex, stateSize) {

    val canvasAdapter = adapter

    val canvas: ActualCanvas
        get() = canvasAdapter.rootContainer

    var renderData: CanvasRenderData? = null

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? =
        null

    override fun genPatchDescendant(fragment: AdaptiveFragment) =
        Unit

    override fun genPatchInternal(): Boolean {
        if (haveToPatch(dirtyMask, 1)) {
            renderData = CanvasRenderData().also { rd -> instructions.applyTo(rd) }
        }
        return false
    }

    override fun mount() {
        super.mount()
        adapter.addActualRoot(this)
    }

    override fun unmount() {
        adapter.removeActualRoot(this)
        super.unmount()
    }

    open fun draw() {
        canvas.save(id)
        canvas.apply(renderData)

        drawInner()

        canvas.restore(id)
    }

    abstract fun drawInner()

}