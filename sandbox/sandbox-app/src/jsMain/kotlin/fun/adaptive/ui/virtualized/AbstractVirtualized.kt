package `fun`.adaptive.ui.virtualized

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.fragment.layout.SizingProposal

abstract class AbstractVirtualized<RT, CRT : RT>(
    adapter: AbstractAuiAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
) : AbstractContainer<RT, CRT>(
    adapter, parent, declarationIndex, 3
) {

    companion object {
        const val MODEL_INDEX = 1
        const val ITEM_FUN_INDEX = 2
    }

    abstract val spacerHeight: Double

    val model: VirtualizationModel<*>
        get() = get(MODEL_INDEX)

    val itemFun: BoundFragmentFactory
        get() = get(ITEM_FUN_INDEX)

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? {
        return null
    }

//    override fun genPatchInternal(): Boolean {
//        super.genPatchInternal()
//        if (dirtyMask != cleanStateMask) {
//            patchStructure()
//        } else {
//            patchContent()
//        }
//        return true
//    }

    override fun computeLayout(
        proposal: SizingProposal
    ) {
        val height = renderData.layout?.instructedHeight ?: proposal.containerHeight
        check(height.isFinite()) { "height must be finite for virtualized containers" }

        renderData.finalWidth = 100.0
        renderData.finalHeight = 100.0

        renderData.sizingProposal = proposal
    }

    fun onScroll(scrollTop: Double) {
        println("onScroll: $scrollTop")
    }

    abstract fun moveViewport(viewportOffset: Double)

    abstract fun moveItem(itemOffset: Double, itemIndex: Int)

//    fun patchStructure() {
//        var index = 0
//
//        for (loopVariable in iterator) {
//            if (index >= children.size) {
//                val f = addAnonymous(loopVariable)
//                f.create()
//                if (isMounted) f.mount()
//            } else {
//                children[index].also {
//                    it.setStateVariable(1, loopVariable)
//                    it.patch()
//                }
//            }
//            index ++
//        }
//
//        val originalSize = children.size
//
//        while (index < originalSize) {
//            val f = children.removeLast()
//            f.throwAway()
//            index ++
//        }
//    }
//
//    fun patchContent() {
//        children.forEach { it.patch() }
//    }
//
//    fun addAnonymous(iteratorValue: IT): AdaptiveFragment =
//        AdaptiveAnonymous(this, 0, 2, itemFun).also {
//            children.add(it)
//            it.setStateVariable(1, iteratorValue)
//        }

}