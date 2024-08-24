/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.graphics.svg.render.SvgRenderData
import `fun`.adaptive.utility.applyIfInstance

abstract class SvgFragment<T : SvgRenderData>(
    adapter: SvgAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize: Int
) : AdaptiveFragment(adapter, parent, declarationIndex, instructionsIndex, stateSize) {

    val svgAdapter = adapter

    lateinit var renderData: T

    val canvas
        get() = svgAdapter.rootContainer

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? =
        null

    override fun genPatchDescendant(fragment: AdaptiveFragment) =
        Unit

    override fun genPatchInternal(): Boolean {
        patchInstructions()
        return false
    }

    abstract fun newRenderData() : T

    inline operator fun <reified T : Any> T?.invoke(function : (it : T) -> Unit) {
        if (this != null) {
            canvas.save(id)
            function(this)
        }
    }

    fun patchInstructions() {
        if (instructionIndex != - 1 && haveToPatch(dirtyMask, 1 shl instructionIndex)) {
            renderData = newRenderData().also { rd -> instructions.forEach { it.apply(rd) } }
        }
    }

    // TODO Should we optimize crate and dispose of SVG fragments? They are mostly no-op.

    override fun mount() {
        super.mount()
        adapter.addActualRoot(this)
    }

    open fun draw() {
        children.forEach { it.applyIfInstance<SvgFragment<*>> { draw() } }
    }

    override fun unmount() {
        adapter.removeActualRoot(this)
        super.unmount()
    }

}