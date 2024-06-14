/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.grapics.svg

import hu.simplexion.adaptive.foundation.AdaptiveFragment

abstract class SvgFragment(
    adapter: SvgAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize: Int
) : AdaptiveFragment(adapter, parent, declarationIndex, instructionsIndex, stateSize) {

    val svgAdapter = adapter

    val canvas
        get() = svgAdapter.rootContainer

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? =
        null

    override fun genPatchDescendant(fragment: AdaptiveFragment) =
        Unit

    override fun genPatchInternal(): Boolean {
        return false
    }

    // TODO Should we optimize crate and dispose of SVG fragments? They are mostly no-op.

    override fun mount() {
        super.mount()
        adapter.addActualRoot(this)
    }

    abstract fun draw()

    override fun unmount() {
        adapter.removeActualRoot(this)
        super.unmount()
    }

}