/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation.structural

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.internal.AdaptiveClosure
import hu.simplexion.adaptive.foundation.AdaptiveFragment

class AdaptiveSlot(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveFragment(adapter, parent, index, - 1, 0) {

    override val createClosure: AdaptiveClosure
        get() = parent !!.thisClosure

    override val thisClosure = createClosure

    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        // the fragment state is set by `replace`, otherwise slot children are detached
    }

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? {
        return null //
    }

    override fun genPatchInternal(): Boolean = false // do not propagate changes, descendants are detached

    fun replace(origin: AdaptiveFragment, detachIndex: Int) {
        if (children.isNotEmpty()) {
            if (isMounted) children.forEach { it.unmount() }
            children.forEach { it.dispose() }
            children.clear()
        }

        val fragment = origin.genBuild(this, detachIndex)
        // genBuild calls create
        // create calls declaringFragment.genPatchDescendant
        // so the external state is correct at this point

        checkNotNull(fragment) { "${origin}.genBuild(this, $detachIndex) returned with null" }

        children.add(fragment)

        if (isMounted) {
            fragment.mount()
        }
    }

}