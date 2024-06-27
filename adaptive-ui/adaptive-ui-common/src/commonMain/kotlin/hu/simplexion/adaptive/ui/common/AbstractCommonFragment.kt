/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.instruction.DPixel
import hu.simplexion.adaptive.ui.common.render.CommonRenderData

abstract class AbstractCommonFragment<RT>(
    adapter: AbstractCommonAdapter<RT, *>,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize: Int
) : AdaptiveFragment(adapter, parent, declarationIndex, instructionsIndex, stateSize) {

    abstract val receiver: RT

    /**
     * Use this field when accessing actual UI specific adapter functions.
     */
    open val uiAdapter = adapter

    var renderData = CommonRenderData(adapter)

    /**
     * Structural fragments (loop and select) set this to true to modify behaviour.
     */
    open val isStructural
        get() = false

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? =
        null

    override fun genPatchDescendant(fragment: AdaptiveFragment) =
        Unit

    override fun genPatchInternal(): Boolean {
        patchInstructions()
        return true
    }

    fun patchInstructions() {
        if (instructionIndex != - 1 && haveToPatch(dirtyMask, 1 shl instructionIndex)) {
            renderData = CommonRenderData(uiAdapter, instructions)
            uiAdapter.applyRenderInstructions(this)
        }
    }

    override fun mount() {
        super.mount()
        parent?.addActual(this, if (isStructural) null else true) ?: adapter.addActualRoot(this)
    }

    override fun unmount() {
        parent?.removeActual(this, if (isStructural) null else true) ?: adapter.removeActualRoot(this)
        super.unmount()
    }

    /**
     * Basic layout computation that is used for intrinsic UI fragments. Layout fragments
     * override this method to implement their own calculation algorithm.
     */
    open fun computeLayout(proposedWidth: Double, proposedHeight: Double) {
        val data = renderData
        val layout = data.layout

        data.finalWidth = select(layout?.instructedWidth, data.innerWidth, proposedWidth, data.surroundingHorizontal)
        data.finalHeight = select(layout?.instructedHeight, data.innerHeight, proposedHeight, data.surroundingVertical)
    }

    open fun placeLayout(top: Double, left: Double) {
        val data = renderData

        data.finalTop = top
        data.finalLeft = left

        if (trace) trace("layout", "top: ${data.finalTop}, left: ${data.finalLeft}, width: ${data.finalWidth}, height: ${data.finalHeight}")

        uiAdapter.applyLayoutToActual(this)
    }

    fun select(instructed: Double?, inner: Double?, proposed: Double, surrounding: Double) =
        when {
            instructed != null -> instructed
            inner != null -> inner + surrounding
            proposed.isFinite() -> proposed
            else -> surrounding
        }

    val DPixel.px
        get() = uiAdapter.toPx(this)

    val DPixel?.pxOrZero
        get() = if (this == null) 0.0 else uiAdapter.toPx(this)

}