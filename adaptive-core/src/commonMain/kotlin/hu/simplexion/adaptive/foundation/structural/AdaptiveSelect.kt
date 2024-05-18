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
) : AdaptiveFragment(adapter, parent, index, 2) {

    override val createClosure : AdaptiveClosure
        get() = parent!!.thisClosure

    override val thisClosure = AdaptiveClosure(
        createClosure.fragments + this,
        createClosure.closureSize + state.size
    )

    val placeholder: AdaptiveFragment = adapter.createPlaceholder(this, -1)

    val stateBranch
        get() = state[0] as Int

    var shownBranch // -1 means that there is nothing shown, like: `if (condition) div {  }`
        get() = state[1] as Int
        set(v) { state[1] = v }

    var mounted = false

    /**
     * The fragment that is currently shown.
     */
    var shownFragment: AdaptiveFragment? = null

    override fun create() {
        if (trace) trace("before-Create")

        state[1] = -1 // initialize showBranch so that we don't have a shown branch

        patch()

        if (trace) trace("after-Create")
    }

    override fun mount() {
        if (adapter.trace) trace("before-Mount")

        mounted = true

        placeholder.mount()
        shownFragment?.mount()

        if (adapter.trace) trace("after-Mount")
    }

    override fun genPatchInternal() {
        if (stateBranch == shownBranch) {
            shownFragment?.patch()
        } else {
            if (mounted) shownFragment?.unmount()
            shownFragment?.dispose()

            if (stateBranch == - 1) {
                shownFragment = null
            } else {
                shownFragment = createClosure.owner.genBuild(this, stateBranch)
                if (mounted) shownFragment?.mount()
            }

            shownBranch = stateBranch
        }
    }

    override fun unmount() {
        if (trace) trace("before-Unmount")

        shownFragment?.unmount()
        placeholder.unmount()

        mounted = false

        if (trace) trace("after-Unmount")
    }

    override fun dispose() {
        if (trace) trace("before-Dispose")
        shownFragment?.dispose()
        if (trace) trace("after-Dispose")
    }

}