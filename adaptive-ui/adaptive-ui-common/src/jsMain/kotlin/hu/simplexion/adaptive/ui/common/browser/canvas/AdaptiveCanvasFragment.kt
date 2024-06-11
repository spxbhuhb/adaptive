/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.browser.canvas

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.instruction.DPixel
import hu.simplexion.adaptive.ui.common.instruction.Size
import hu.simplexion.adaptive.ui.common.layout.RawFrame
import hu.simplexion.adaptive.ui.common.layout.RawPoint
import hu.simplexion.adaptive.ui.common.layout.RawSize
import org.w3c.dom.CanvasRenderingContext2D

abstract class AdaptiveCanvasFragment(
    adapter: AdaptiveCanvasAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize: Int
) : AdaptiveFragment(adapter, parent, declarationIndex, instructionsIndex, stateSize) {

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

    abstract fun draw(ctx : CanvasRenderingContext2D)

}