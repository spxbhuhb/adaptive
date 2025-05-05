package `fun`.adaptive.ui.fragment.util

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.FragmentTask
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.aui

@AdaptiveActual(aui)
class AuiAfterPatchBatch(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveFragment(adapter, parent, index, 3) {

    val once: Boolean
        get() = get(1) ?: true

    val task: (fragment : AdaptiveFragment) -> Unit
        get() = get(2)

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? {
        return null
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal(): Boolean {
        if (once && !isInit) return false

        (adapter as? AbstractAuiAdapter<*,*>)?.addAfterPatchBatchTask(
            FragmentTask(createClosure.owner, task)
        )

        return false
    }

}