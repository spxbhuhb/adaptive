package `fun`.adaptive.foundation.fragment

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.utility.checkIfInstance

/**
 * A fragment that stores contextual data other fragments may try to find and use.
 * This is useful for local structures such as forms where editors can find their
 * local context and make changes in it.
 */
class LocalContext(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveFragment(adapter, parent, index, 1, 3) {

    val context: Any?
        get() = get(0)

    val fakeInstructions: AdaptiveInstructionGroup
        get() = get(1)

    val content: BoundFragmentFactory
        get() = get(2)

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? {
        if (declarationIndex != 0) invalidIndex(declarationIndex)
        return AdaptiveAnonymous(this, declarationIndex, 0, content).apply { create() }
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal(): Boolean = true

}