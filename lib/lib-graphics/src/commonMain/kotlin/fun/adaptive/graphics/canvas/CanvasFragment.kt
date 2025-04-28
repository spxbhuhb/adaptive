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

    open val isStructural
        get() = false

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? =
        null

    override fun genPatchDescendant(fragment: AdaptiveFragment) =
        Unit

    override fun genPatchInternal(): Boolean {
        if (haveToPatch(dirtyMask, 1)) {
            // this technique avoids unnecessary updates of render data, when draw is called
            // render data will be updated from the latest set of instructions
            renderData = null
            canvas.redrawNeeded()
        }
        return true
    }

    override fun mount() {
        super.mount()

        val safeParent = parent

        if (safeParent == null) {
            adapter.addActualRoot(this)
        } else {
            safeParent.addActual(this, if (isStructural) null else true)
        }
    }

    override fun unmount() {

        val safeParent = parent

        if (safeParent == null) {
            adapter.removeActualRoot(this)
        } else {
            safeParent.removeActual(this, if (isStructural) null else true)
        }

        super.unmount()
    }

    open fun draw() {
        canvas.save(id)

        if (renderData == null) {
            renderData = CanvasRenderData(canvasAdapter).also { rd -> instructions.applyTo(rd) }
        }

        canvas.apply(renderData)

        drawInner()

        canvas.restore(id)
    }

    abstract fun drawInner()

}