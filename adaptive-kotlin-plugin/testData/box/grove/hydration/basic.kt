/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.grove.api

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.instruction.*
import `fun`.adaptive.foundation.testing.*

@AdaptiveHydrated("grove")
fun hydrated(source : String, vararg externalStateVariables : Any?) : AdaptiveFragment {
    manualImplementation(source, externalStateVariables)
}

@AdaptiveActual("grove")
class GroveHydrated(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    stateSize : Int
) : AdaptiveFragment(adapter, parent, declarationIndex, stateSize) {

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal() = true

}

fun box() : String {

    val adapter = AdaptiveTestAdapter()

    adapter.fragmentFactory.add("grove:hydrated") { p,i,s -> GroveHydrated(p.adapter as AdaptiveTestAdapter, p, i, s) }

    adaptive(adapter) {
        hydrated("Hello - 1")
        hydrated("Hello - 2", instructionsOf(), 12, "World")
    }

    return adapter.assert(listOf(
        //@formatter:off
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null]"),
        TraceEvent("AdaptiveSequence", 3, "before-Create", ""),
        TraceEvent("AdaptiveSequence", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,null]"),
        TraceEvent("AdaptiveSequence", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,[1, 2]]"),
        TraceEvent("AdaptiveSequence", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,[1, 2]]"),
        TraceEvent("GroveHydrated", 4, "before-Create", ""),
        TraceEvent("GroveHydrated", 4, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null]"),
        TraceEvent("GroveHydrated", 4, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, Hello - 1]"),
        TraceEvent("GroveHydrated", 4, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, Hello - 1]"),
        TraceEvent("GroveHydrated", 4, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [null, Hello - 1]"),
        TraceEvent("GroveHydrated", 4, "after-Create", ""),
        TraceEvent("GroveHydrated", 5, "before-Create", ""),
        TraceEvent("GroveHydrated", 5, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null, null, null, null]"),
        TraceEvent("GroveHydrated", 5, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [[], Hello - 2, 12, World]"),
        TraceEvent("GroveHydrated", 5, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [[], Hello - 2, 12, World]"),
        TraceEvent("GroveHydrated", 5, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [[], Hello - 2, 12, World]"),
        TraceEvent("GroveHydrated", 5, "after-Create", ""),
        TraceEvent("AdaptiveSequence", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null,[1, 2]]"),
        TraceEvent("AdaptiveSequence", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptiveSequence", 3, "before-Mount", ""),
        TraceEvent("GroveHydrated", 4, "before-Mount", ""),
        TraceEvent("GroveHydrated", 4, "after-Mount", ""),
        TraceEvent("GroveHydrated", 5, "before-Mount", ""),
        TraceEvent("GroveHydrated", 5, "after-Mount", ""),
        TraceEvent("AdaptiveSequence", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", "")
        //@formatter:on
    ))
}