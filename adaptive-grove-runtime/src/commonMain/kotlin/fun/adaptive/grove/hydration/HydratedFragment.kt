package `fun`.adaptive.grove.hydration

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.grove.hydration.model.DehydratedFragment
import `fun`.adaptive.utility.checkIfInstance

@AdaptiveActual
class HydratedFragment(
    adapter : AdaptiveAdapter,
    parent : AdaptiveFragment? = null,
    declarationIndex: Int
) : AdaptiveFragment(
    adapter, parent, declarationIndex, -1, 1
) {

    val dehydratedFragment : DehydratedFragment
        get() = state[0].checkIfInstance<DehydratedFragment>()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? {
        val items = dehydratedFragment.build
        if (declarationIndex < 0 || declarationIndex > items.size) invalidIndex(declarationIndex)

        if (declarationIndex == 0 && items.isEmpty()) return null

        val item = items[declarationIndex]

        return adapter.fragmentFactory.newInstance(item.key, this, declarationIndex).also { it.create() }
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        val index = fragment.declarationIndex
        val items = dehydratedFragment.patchDescendant
        if (items.isEmpty()) return

        if (index < 0 || index > items.size) invalidIndex(index)

        for (step in items[index].steps) {
            if ((fragment.dirtyMask and step.dependencyMask) == 0) continue
            val value = step.value
            fragment.setStateVariable(step.stateVariableIndex, value)
        }
    }

    override fun genPatchInternal() : Boolean {
        for (step in dehydratedFragment.patchInternal) {
            if ((dirtyMask and step.dependencyMask) == 0) continue
            val value = step.value
            setStateVariable(step.stateVariableIndex, value)
        }
        return true
    }
}