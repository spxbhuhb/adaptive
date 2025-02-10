package `fun`.adaptive.grove.sheet.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.grove.hydration.lfm.LfmConst
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.model.ItemInfo
import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.layout.AbstractBox
import `fun`.adaptive.ui.instruction.DPixel

/**
 * A fragment that contains hydrated fragments and supports adding, removing and patching them
 * independent of the standard adaptive fragment mechanisms.
 *
 * State:
 *
 * 1. instructions
 * 2. viewModel (never changes)
 * 3. content (not used, inherited from AbstractBox)
 */
@AdaptiveActual
class GroveDrawingLayer<RT, CRT : RT>(
    adapter: AbstractAuiAdapter<RT, CRT>,
    parent: AdaptiveFragment? = null,
    declarationIndex: Int
) : AbstractBox<RT, CRT>(
    adapter, parent, declarationIndex, 3
) {

    val controller
        get() = get<SheetViewController>(1)

    val updateBatch = mutableListOf<SheetItem>()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun create() {
        super.create()
        controller.drawingLayer = this
    }

    override fun addActualScheduleUpdate(itemFragment: AbstractAuiFragment<RT>) {
        if (! isMounted) {
            scheduleUpdate()
            return
        }

        // This call computes the layout of itemFragment and places it on the actual UI
        // At this point we are outside the general UI patching batch, but that's OK.
        // The reason for this is twofold: performance and having the actual frame
        // calculated before the operation finishes, so the item can be selected properly.

        updateLayout(updateBatchId, itemFragment)
    }

    override fun removeActualScheduleUpdate(itemFragment: AbstractAuiFragment<RT>) {
        // we do not need to schedule layout update for remove
    }

    operator fun plusAssign(item: SheetItem) {
        val model = item.model

        adapter.fragmentFactory.newInstance(model.key, this, - 1, model.mapping.size).also { fragment ->

            children += fragment
            item.fragment = fragment

            for ((index, mapping) in model.mapping.withIndex()) {
                val value = (mapping.mapping as LfmConst).value
                fragment.setStateVariable(index, value)
            }

            fragment.create()

            if (isMounted) fragment.mount()

            updateBatch += item
        }
    }

    operator fun minusAssign(item: SheetItem) {
        val info = ItemInfo(item.index)
        val index = children.indexOfFirst { info in it.instructions }
        val child = children.removeAt(index)
        if (isMounted) child.unmount()
        child.dispose()
    }

    fun updateLayout(item: SheetItem) {
        updateLayout(updateBatchId, item.fragment as AbstractAuiFragment<*>)
    }

    fun toPx(dp: DPixel) = uiAdapter.toPx(dp)

    fun toDp(px: Double) = uiAdapter.toDp(px)

}