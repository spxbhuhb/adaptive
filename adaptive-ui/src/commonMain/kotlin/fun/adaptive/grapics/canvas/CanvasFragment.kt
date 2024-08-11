/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.grapics.canvas

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.grapics.canvas.platform.ActualCanvas

abstract class CanvasFragment(
    adapter: CanvasAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize: Int
) : AdaptiveFragment(adapter, parent, declarationIndex, instructionsIndex, stateSize) {

    val canvasAdapter = adapter

    val canvas: ActualCanvas
        get() = canvasAdapter.rootContainer

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? =
        null

    override fun genPatchDescendant(fragment: AdaptiveFragment) =
        Unit

    override fun genPatchInternal(): Boolean {
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

    abstract fun draw()

}