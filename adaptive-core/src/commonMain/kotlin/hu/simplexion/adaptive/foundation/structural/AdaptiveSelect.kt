/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation.structural

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.internal.AdaptiveClosure

class AdaptiveSelect(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int,
) : AdaptiveFragment(adapter, parent, index, -1, 2) {

    override val createClosure: AdaptiveClosure
        get() = parent !!.thisClosure

    override val thisClosure = AdaptiveClosure(
        createClosure.fragments + this,
        createClosure.closureSize + state.size
    )

    val stateBranch
        get() = state[0] as Int

    var shownBranch // -1 means that there is nothing shown, like: `if (condition) div {  }`
        get() = state[1] as Int
        set(v) {
            state[1] = v
        }

    init {
        shownBranch = - 1
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

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? =
        null // genPatchInternal will create the child fragment if necessary

    override fun genPatchInternal(): Boolean {


        if (stateBranch == shownBranch) {
            return true // no change, let patchInternal patch the child
        }

        shownBranch = stateBranch  // new branch

        shownFragment?.let { // drop old branch
            if (mounted) it.unmount()
            it.dispose()
        }

        if (shownBranch == -1) { // nothing should be shown
            shownFragment = null
            return false
        }

        createClosure.owner.genBuild(this, shownBranch)?.also { // there is something to sow
            if (mounted) it.mount()
            shownFragment = it
        }

        return false

    }


}