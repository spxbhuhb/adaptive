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
) : AdaptiveFragment(adapter, parent, index, 2) {

    val key: String
        get() = get(1)

    var lastKey : String? = null

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? {
        if (declarationIndex != 0) invalidIndex(declarationIndex)
        return adapter.actualize(key, this, 0, -1)
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal(): Boolean {

        if (haveToPatch(getCreateClosureDirtyMask(), 1 shl 1)) {
            if (lastKey == key) return true // no need to patch

            lastKey = key
            throwChildrenAway()

            adapter.actualize(key, this, 0, -1).also {
                it.create()
                children += it
                if (isMounted) it.mount()
            }
        }

        return true
    }

}