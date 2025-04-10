/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.foundation.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.throwChildrenAway

@AdaptiveActual
open class FoundationActualize(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveFragment(adapter, parent, index, 3) {

    val key: String
        get() = get(1)

    val externalState: Array<out Any?>?
        get() = get(2)

    var lastKey: String? = null

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? =
        null // genPatchInternal handles building children

    override fun genPatchDescendant(fragment: AdaptiveFragment) {

        val safeExternalState = externalState ?: return

        if (fragment.declarationIndex != 0) invalidIndex(fragment.declarationIndex)

        val closureMask = fragment.getCreateClosureDirtyMask()

        if (haveToPatch(closureMask, 1 shl 2)) {
            for (value in safeExternalState.indices) {
                fragment.setStateVariable(value, safeExternalState[value])
            }
        }
    }

    override fun genPatchInternal(): Boolean {

        if (haveToPatch(getThisClosureDirtyMask(), 1 shl 1)) {
            if (lastKey == key) return true // no need to patch

            lastKey = key
            throwChildrenAway()

            adapter.actualize(key, this, 0, - 1).also {
                it.create()
                children += it
                if (isMounted) it.mount()
            }
        }

        return true
    }

}