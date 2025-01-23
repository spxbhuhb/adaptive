/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.instruction.layout.FitStrategy
import `fun`.adaptive.ui.render.model.AuiRenderData

abstract class AbstractAuiFragment<RT>(
    adapter: AbstractAuiAdapter<RT, *>,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    stateSize: Int
) : AdaptiveFragment(adapter, parent, declarationIndex, stateSize) {

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

    open val patchDescendants: Boolean
        get() = false

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? =
        null

    override fun genPatchDescendant(fragment: AdaptiveFragment) =
        Unit

    override fun genPatchInternal(): Boolean {

        previousRenderData = renderData

        patchInstructions()
        auiPatchInternal()

        if (isInit || renderData.layoutIndependentChanged(previousRenderData)) {
            uiAdapter.applyLayoutIndependent(this)
        }

        if (renderData.innerDimensionsChanged(previousRenderData)) {
            scheduleUpdate()
            return patchDescendants
        }

        if (renderData.gridChanged(previousRenderData)) {
            // the optimization for grid and non-grid updates are different, hence the separation
            scheduleUpdate()
            return patchDescendants
        }

        if (renderData.layoutChanged(previousRenderData)) {
            scheduleUpdate()
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

        if ( ! haveToPatch(dirtyMask, 1)) return

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

        if (updateBatchId == uiAdapter.updateBatchId) return

        updateBatchId = uiAdapter.updateBatchId
        uiAdapter.updateBatch += this
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
            layout?.fit?.horizontalStrategy == FitStrategy.Container -> proposedWidth
            innerWidth != null -> innerWidth + data.surroundingHorizontal
            proposedWidth.isFinite() -> proposedWidth
            else -> data.surroundingHorizontal
        }

        val instructedHeight = layout?.instructedHeight
        val innerHeight = data.innerHeight

        data.finalHeight = when {
            instructedHeight != null -> instructedHeight
            layout?.fit?.verticalStrategy == FitStrategy.Container == true -> proposedHeight
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

    open fun updateLayout(updateId: Long, item: AbstractAuiFragment<*>?) {
        if (updateBatchId == updateId) return

        // The previous render data is obsolete when the fragment was not patched in this batch,
        // but a descendant delegated the layout update. In this case contains data that belongs
        // to the one but last render, not the last render.

        val obsoletePreviousData = ((updateBatchId - updateId) != 1L)

        updateBatchId = updateId

        val layoutFragment = renderData.layoutFragment

        if (shouldUpdateSelf() || layoutFragment == null) {
            val layout = renderData.layout
            val baseData = if (obsoletePreviousData) renderData else previousRenderData

            computeLayout(
                layout?.instructedWidth ?: baseData.finalWidth,
                layout?.instructedHeight ?: baseData.finalHeight
            )
            placeLayout(
                layout?.instructedTop ?: baseData.finalTop,
                layout?.instructedLeft ?: baseData.finalLeft
            )
        } else {
            renderData.layoutFragment?.updateLayout(updateId, this)
        }
    }

    /**
     * Calculate if the fragment should handle the layout update by itself, or it should pass the update
     * to the parent.
     */
    open fun shouldUpdateSelf(): Boolean {
        val layout = renderData.layout

        // Default to update by parent when there are no layout instructions. In that case the fragment positions
        // may change and the parent can do the optimization if possible.

        if (layout == null) return false

        val fixHorizontal = layout.instructedWidth != null || layout.fit?.horizontalStrategy == FitStrategy.Container
        val fixVertical = layout.instructedHeight != null || layout.fit?.verticalStrategy == FitStrategy.Container

        val container = renderData.layoutFragment?.renderData?.container

        val alignHorizontal = container?.horizontalAlignment != null || layout.horizontalAlignment != null
        val alignVertical = container?.verticalAlignment != null || layout.verticalAlignment != null

        return fixHorizontal && fixVertical && ! alignVertical && ! alignHorizontal
    }

}