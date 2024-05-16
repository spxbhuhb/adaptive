/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.base.structural

import hu.simplexion.adaptive.base.AdaptiveAdapter
import hu.simplexion.adaptive.base.AdaptiveBridge
import hu.simplexion.adaptive.base.internal.AdaptiveClosure
import hu.simplexion.adaptive.base.AdaptiveFragment

class AdaptiveSelect<BT>(
    adapter: AdaptiveAdapter<BT>,
    parent: AdaptiveFragment<BT>?,
    index: Int,
) : AdaptiveFragment<BT>(adapter, parent, index, 2) {

    override val createClosure : AdaptiveClosure<BT>
        get() = parent!!.thisClosure

    override val thisClosure = AdaptiveClosure(
        createClosure.components + this,
        createClosure.closureSize + state.size
    )

    val placeholder: AdaptiveBridge<BT> = adapter.createPlaceholder()

    val stateBranch
        get() = state[0] as Int

    var shownBranch // -1 means that there is nothing shown, like: `if (condition) div {  }`
        get() = state[1] as Int
        set(v) { state[1] = v }

    var mounted = false

    /**
     * The fragment that is currently shown.
     */
    var shownFragment: AdaptiveFragment<BT>? = null

    override fun create() {
        if (trace) trace("before-Create")

        state[1] = -1 // initialize showBranch so that we don't have a shown branch

        patch()

        if (trace) trace("after-Create")
    }

    override fun mount(bridge: AdaptiveBridge<BT>) {
        if (adapter.trace) trace("before-Mount", "bridge", bridge)

        mounted = true
        bridge.add(placeholder)
        shownFragment?.mount(placeholder)

        if (adapter.trace) trace("after-Mount", "bridge", bridge)
    }

    override fun genPatchInternal() {
        if (stateBranch == shownBranch) {
            shownFragment?.patch()
        } else {
            if (mounted) shownFragment?.unmount(placeholder)
            shownFragment?.dispose()

            if (stateBranch == - 1) {
                shownFragment = null
            } else {
                shownFragment = createClosure.owner.genBuild(this, stateBranch)
                if (mounted) shownFragment?.mount(placeholder)
            }

            shownBranch = stateBranch
        }
    }

    override fun unmount(bridge: AdaptiveBridge<BT>) {
        if (trace) trace("before-Unmount", "bridge", bridge)

        shownFragment?.unmount(placeholder)
        bridge.remove(placeholder)
        mounted = false

        if (trace) trace("after-Unmount", "bridge", bridge)
    }

    override fun dispose() {
        if (trace) trace("before-Dispose")
        shownFragment?.dispose()
        if (trace) trace("after-Dispose")
    }

}