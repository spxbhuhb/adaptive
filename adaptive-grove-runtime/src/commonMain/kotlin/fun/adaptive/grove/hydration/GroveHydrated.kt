package `fun`.adaptive.grove.hydration

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment.AdaptiveAnonymous
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.grove.hydration.lfm.LfmConst
import `fun`.adaptive.grove.hydration.lfm.LfmFragment
import `fun`.adaptive.utility.checkIfInstance

/**
 * A fragment that builds its descendant tree from an [LfmFragment]. It creates an anonymous
 * fragment which contains the direct descendants. The anonymous fragment is necessary to
 * store the internal state of the hydrated fragment as the size of that state depends on the model.
 *
 * Declaration indices are organised as follows:
 *
 * - 0 = anonymous fragment with the state
 * - 1 = a sequence inside the anonymous fragment to store the children
 * - all others are for descendants
 */
@AdaptiveActual
class GroveHydrated(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment? = null,
    declarationIndex: Int,
    stateSize : Int
) : AdaptiveFragment(
    adapter, parent, declarationIndex, - 1, stateSize
) {

    companion object {
        const val ANONYMOUS_INDEX = 0
        const val SEQUENCE_INDEX = 1
        const val RESERVED_INDICES = 2
    }

    val model: LfmFragment
        get() = state[0].checkIfInstance<LfmFragment>()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? =
        when (declarationIndex) {

            ANONYMOUS_INDEX -> AdaptiveAnonymous(parent, ANONYMOUS_INDEX, model.internalStateVariables.size, BoundFragmentFactory(this, SEQUENCE_INDEX))

            SEQUENCE_INDEX -> adapter.newSequence(this, SEQUENCE_INDEX)

            else -> {
                val items = model.descendants
                val itemIndex = declarationIndex - RESERVED_INDICES // to account for the anonymous and sequence

                if (itemIndex < 0 || itemIndex >= items.size) invalidIndex(declarationIndex)

                val item = items[itemIndex]

                adapter.fragmentFactory.newInstance(item.key, this, declarationIndex, item.mapping.size)
            }

        }.also { it.create() }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {

        val dehydrated = model
        val fragmentDeclarationIndex = fragment.declarationIndex

        when (fragmentDeclarationIndex) {

            ANONYMOUS_INDEX -> {
                for ((index, variable) in model.internalStateVariables.withIndex()) {
                    if (! haveToPatch(dirtyMask, variable.dependencyMask)) continue
                    fragment.setStateVariable(state.size + index, (variable.calculation as LfmConst).value)
                }
            }

            SEQUENCE_INDEX -> {
                val build = dehydrated.descendants
                fragment.setStateVariable(0, IntArray(build.size) { it + RESERVED_INDICES })
            }

            else -> {
                val itemIndex = fragmentDeclarationIndex - RESERVED_INDICES

                val items = dehydrated.descendants

                if (items.isEmpty()) return
                if (itemIndex < 0 || itemIndex >= items.size) invalidIndex(itemIndex)

                val init = fragment.isInit

                for ((index, mapping) in items[itemIndex].mapping.withIndex()) {
                    if ( ! init && (fragment.dirtyMask and mapping.dependencyMask) == 0) continue
                    val value = (mapping.mapping as LfmConst).value
                    println("patching $index with $value")
                    fragment.setStateVariable(index, value)
                }
            }
        }
    }

    /**
     * Patch internal is handled by patch descendant when the descendant is the anonymous fragment.
     */
    override fun genPatchInternal(): Boolean = true

}