package `fun`.adaptive.grove.sheet.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.grove.hydration.lfm.LfmConst
import `fun`.adaptive.grove.sheet.model.ItemInfo
import `fun`.adaptive.grove.sheet.model.SheetItem
import `fun`.adaptive.grove.sheet.model.SheetViewModel

/**
 * A fragment that contains hydrated fragments and supports adding, removing and patching them
 * independent of the standard adaptive fragment mechanisms.
 */
@AdaptiveActual
class GroveDrawingLayer(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment? = null,
    declarationIndex: Int
) : AdaptiveFragment(
    adapter, parent, declarationIndex, 2
) {

    val viewModel by stateVariable<SheetViewModel>()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal(): Boolean = false

    override fun create() {
        super.create()
        viewModel.root = this
    }

    operator fun plusAssign(item : SheetItem) {
        val model = item.model

        adapter.fragmentFactory.newInstance(model.key, this, -1, model.mapping.size).also { fragment ->

            children += fragment
            item.fragment = fragment

            for ((index, mapping) in model.mapping.withIndex()) {
                val value = (mapping.mapping as LfmConst).value
                fragment.setStateVariable(index, value)
            }

            fragment.create()

            if (isMounted) fragment.mount()
        }
    }

    operator fun minusAssign(item: SheetItem) {
        val info = ItemInfo(item.index)
        val index = children.indexOfFirst { info in it.instructions }
        val child = children.removeAt(index)
        if (isMounted) child.unmount()
        child.dispose()
    }

}