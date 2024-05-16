/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package stuff

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.registry.*
import hu.simplexion.adaptive.foundation.testing.*

@AdaptiveExpect
fun text() {
    manualImplementation()
}

class AdaptiveText<BT>(
    adapter: AdaptiveAdapter<BT>,
    parent: AdaptiveFragment<BT>?,
    index: Int
) : AdaptiveFragment<BT>(adapter, parent, index, 0) {

    override fun genBuild(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT>? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment<BT>) = Unit

    override fun genPatchInternal() = Unit

}


fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adapter.fragmentImplRegistry += AdaptiveFragmentImplRegistryEntry("stuff.AdaptiveText") { a, p, i -> AdaptiveText(a, p, i) }

    adaptive(adapter) {
        text()
    }

    return adapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveText", 3, "before-Create", ""),
        TraceEvent("AdaptiveText", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveText", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveText", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveText", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveText", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveText", 3, "before-Mount", "bridge: 1"),
        TraceEvent("AdaptiveText", 3, "after-Mount", "bridge: 1"),
        TraceEvent("<root>", 2, "after-Mount", "bridge: 1")
    ))
}