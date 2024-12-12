/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.render.model.AuiRenderData

abstract class AbstractAuiFragment<RT>(
    adapter: AbstractAuiAdapter<RT, *>,
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

    var updateBatchId = 0L

    var previousRenderData = adapter.emptyRenderData

    /**
     * The initial render data is read from the adapter. This makes it possible to apply
     * global styling by fragment type.
     */
    var renderData = AuiRenderData(adapter)

    /**
     * Structural fragments (loop and select) set this to true to modify behaviour.
     */
    open val isStructural
        get() = false

    /**
     * When true, the fragment does **NOT** call the `addActual/removeActual` of its parent
     * but calls `addActualRoot/removeActualRoot` of the adapter directly.
     */
    open val isRootActual
        get() = false

    open val invalidInput: Boolean
        get() = false

    val patchDescendants: Boolean
        get() = false

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? =
        null

    override fun genPatchDescendant(fragment: AdaptiveFragment) =
        Unit

    override fun genPatchInternal(): Boolean {

        patchInstructions()
        auiPatchInternal()

        if (isInit || renderData.layoutIndependentChanged(previousRenderData)) {
            uiAdapter.applyLayoutIndependent(this)
        }

        if (renderData.innerDimensionsChanged(previousRenderData)) {
            renderData.layoutFragment?.scheduleUpdate()
            return patchDescendants
        }

        if (renderData.gridChanged(previousRenderData)) {
            // the optimization for grid and non-grid updates are different, hence the separation
            renderData.layoutFragment?.scheduleUpdate()
            return patchDescendants
        }

        if (renderData.layoutChanged(previousRenderData)) {
            renderData.layoutFragment?.scheduleUpdate()
            return patchDescendants
        }

        return patchDescendants
    }

    /**
     * - create a new [AuiRenderData]
     * - applies [instructions] to it
     * - assigns it to [renderData]
     * - schedules layout update when necessary
     */
    open fun patchInstructions() {

        if (instructionIndex == - 1) return
        if (! haveToPatch(dirtyMask, 1 shl instructionIndex)) return

        previousRenderData = renderData
        renderData = AuiRenderData(uiAdapter, previousRenderData, uiAdapter.themeFor(this), instructions)

    }

    /**
     * Execute the fragment-dependent internal patching. Called after the instructions
     * are processed into [renderData].
     */
    abstract fun auiPatchInternal()

    override fun mount() {
        super.mount()

        val safeParent = parent

        if (isRootActual || safeParent == null) {
            adapter.addActualRoot(this)
        } else {
            safeParent.addActual(this, if (isStructural) null else true)
        }
    }

    override fun unmount() {
        val safeParent = parent

        if (isRootActual || safeParent == null) {
            adapter.removeActualRoot(this)
        } else {
            safeParent.removeActual(this, if (isStructural) null else true)
        }

        super.unmount()
    }

    open fun scheduleUpdate() {
        // When the fragment is not mounted it will be added to the layout or to the root fragment list.
        // Both cases put the actual container or this fragment (in case of root fragment) onto the update batch.

        if (! isMounted) return

        // When the fragment batch id is the same as the adapter batch id this fragment is already scheduled
        // for update. In that case we should not add it again.

        if (updateBatchId != uiAdapter.updateBatchId) return

        updateBatchId = uiAdapter.updateBatchId
        uiAdapter.updateBatch += this
    }

    override fun closePatchBatch() {
        uiAdapter.closePatchBatch()
    }

    /**
     * Basic layout computation that is used for intrinsic UI fragments. Layout fragments
     * override this method to implement their own calculation algorithm.
     */
    open fun computeLayout(proposedWidth: Double, proposedHeight: Double) {
        val data = renderData
        val layout = data.layout

        val instructedWidth = layout?.instructedWidth
        val innerWidth = data.innerWidth

        data.finalWidth = when {
            instructedWidth != null -> instructedWidth
            layout?.fillHorizontal == true -> proposedWidth
            innerWidth != null -> innerWidth + data.surroundingHorizontal
            proposedWidth.isFinite() -> proposedWidth
            else -> data.surroundingHorizontal
        }

        val instructedHeight = layout?.instructedHeight
        val innerHeight = data.innerHeight

        data.finalHeight = when {
            instructedHeight != null -> instructedHeight
            layout?.fillVertical == true -> proposedHeight
            innerHeight != null -> innerHeight + data.surroundingVertical
            proposedHeight.isFinite() -> proposedHeight
            else -> data.surroundingVertical
        }
    }

    open fun placeLayout(top: Double, left: Double) {
        val data = renderData

        data.finalTop = top
        data.finalLeft = left

        if (trace) trace("layout", "top: ${data.finalTop}, left: ${data.finalLeft}, width: ${data.finalWidth}, height: ${data.finalHeight}")

        uiAdapter.applyLayoutToActual(this)
    }

}