package `fun`.adaptive.grove.hydration

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment.AdaptiveAnonymous
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.grove.hydration.model.AfmFragment
import `fun`.adaptive.utility.checkIfInstance

/**
 * A fragment that builds its descendant tree from an [AfmFragment]. It creates an anonymous
 * fragment which contains the direct descendants. The anonymous fragment is necessary to
 * store the state of the hydrated fragment as the size of that state depends on the model.
 *
 * Declaration indices are organised as follows:
 *
 * - 0 = anonymous fragment with the state
 * - 1 = a sequence inside the anonymous fragment to store the children
 * - all others are for descendants (we have to decrease those by 2)
 */
@AdaptiveActual
class HydratedFragment(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment? = null,
    declarationIndex: Int
) : AdaptiveFragment(
    adapter, parent, declarationIndex, - 1, 1
) {

    companion object {
        const val ANONYMOUS_INDEX = 0
        const val SEQUENCE_INDEX = 1
        const val RESERVED_INDICES = 2
    }

    val model: AfmFragment
        get() = state[0].checkIfInstance<AfmFragment>()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? =
        when (declarationIndex) {

            ANONYMOUS_INDEX -> AdaptiveAnonymous(parent, ANONYMOUS_INDEX, model.variables.size, BoundFragmentFactory(this, SEQUENCE_INDEX))

            SEQUENCE_INDEX -> adapter.newSequence(this, SEQUENCE_INDEX)

            else -> {
                val items = model.build
                val itemIndex = declarationIndex - RESERVED_INDICES // to account for the sequence

                if (itemIndex < 0 || itemIndex >= items.size) invalidIndex(declarationIndex)

                val item = items[itemIndex]

                adapter.fragmentFactory.newInstance(item.key, this, declarationIndex)
            }

        }.also { it.create() }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {

        val dehydrated = model
        val fragmentDeclarationIndex = fragment.declarationIndex

        when (fragmentDeclarationIndex) {

            ANONYMOUS_INDEX -> {
                for (variable in model.variables) {
                    if (! variable.isExternal) continue
                    if (! haveToPatch(dirtyMask, variable.dependencyMask)) continue
                    fragment.setStateVariable(variable.index, variable.value)
                }
            }

            SEQUENCE_INDEX -> {
                val build = dehydrated.build
                fragment.setStateVariable(0, IntArray(build.size) { build[it].index })
            }

            else -> {
                val itemIndex = fragmentDeclarationIndex - RESERVED_INDICES

                val items = dehydrated.patchDescendant
                if (items.isEmpty()) return

                if (itemIndex < 0 || itemIndex >= items.size) invalidIndex(itemIndex)

                for (step in items[itemIndex].steps) {
                    if ((fragment.dirtyMask and step.dependencyMask) == 0) continue
                    val value = step.value
                    fragment.setStateVariable(step.stateVariableIndex, value)
                }
            }
        }
    }

    override fun genPatchInternal(): Boolean {

//        val fragment = children[0] // this is the anonymous fragment
//
//        for (variable in model.variables) {
//            if (variable.isExternal) continue
//            if (! haveToPatch(dirtyMask, variable.dependencyMask)) continue
//            fragment.setStateVariable(variable.index, variable.value)
//        }
//
//        for (step in model.patchInternal) {
//            if (! haveToPatch(dirtyMask, step.dependencyMask)) continue
//            val value = step.value
//            fragment.setStateVariable(step.stateVariableIndex, value)
//        }

        return true
    }
}