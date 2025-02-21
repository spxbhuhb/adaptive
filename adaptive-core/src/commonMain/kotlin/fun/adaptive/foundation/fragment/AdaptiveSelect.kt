/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.foundation.fragment

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.internal.AdaptiveClosure
import `fun`.adaptive.foundation.throwAway

interface AdaptiveSelectLogic {

    fun makeBranch() : AdaptiveFragment?

    val createClosure: AdaptiveClosure

    val state : Array<Any?>

    val dirtyMask : Int

    val children : MutableList<AdaptiveFragment>

    val isMounted : Boolean

    val stateBranch
        get() = state[1] as Int

    var shownBranch // -1 means that there is nothing shown, like: `if (condition) div {  }`
        get() = (state[2] as? Int) ?: -1
        set(v) {
            state[2] = v
        }

    /**
     * The fragment that is currently shown.
     */
    var shownFragment: AdaptiveFragment?
        get() = children.firstOrNull()
        set(v) {
            when {
                v == null -> children.clear()
                children.isEmpty() -> children.add(v)
                else -> children[0] = v
            }
        }

    fun genPatchInternal() : Boolean {

        if (stateBranch == shownBranch) {
            return true // no change, let patchInternal patch the child
        }

        shownBranch = stateBranch  // new branch

        shownFragment?.throwAway()

        if (shownBranch == -1) { // nothing should be shown
            shownFragment = null
            return false
        }

        makeBranch()?.also { // there is something to show
            if (isMounted) it.mount()
            shownFragment = it
        }

        // do not patch the newly built fragment, it has been patched already by create
        return false
    }
}

class AdaptiveSelect(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int,
) : AdaptiveFragment(adapter, parent, index, 3), AdaptiveSelectLogic {

    override val createClosure: AdaptiveClosure
        get() = parent !!.thisClosure

    override val thisClosure = AdaptiveClosure(
        createClosure.fragments + this,
        createClosure.closureSize + state.size
    )

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? =
        null // genPatchInternal will create the child fragment if necessary

    override fun genPatchInternal(): Boolean =
        super<AdaptiveSelectLogic>.genPatchInternal()

    override fun makeBranch(): AdaptiveFragment? =
        createClosure.owner.genBuild(this, shownBranch, 0)

}