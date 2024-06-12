/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package stuff

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.testing.*

@Adaptive
fun Public() {
    Internal()
}

@Adaptive
internal fun Internal() {
    Private()
}

@Adaptive
private fun Private() {
    T0()
}

@Collect("")
object testFactory : AdaptiveFragmentFactory()

fun box() : String {

    if (":stuff.public" !in testFactory) return "Fail: public"
    if (":stuff.internal" in testFactory) return "Fail: internal"
    if (":stuff.private" in testFactory) return "Fail: private"

    val adapter = AdaptiveTestAdapter()

    adaptive(adapter) {
        Public()
    }

    return adapter.assert(listOf(
        TraceEvent("<root>", 2, "before-Create", ""),
        TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: []"),
        TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptivePublic", 3, "before-Create", ""),
        TraceEvent("AdaptivePublic", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptivePublic", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptivePublic", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptivePublic", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveInternal", 4, "before-Create", ""),
        TraceEvent("AdaptiveInternal", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveInternal", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveInternal", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveInternal", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptivePrivate", 5, "before-Create", ""),
        TraceEvent("AdaptivePrivate", 5, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptivePrivate", 5, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptivePrivate", 5, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptivePrivate", 5, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveT0", 6, "before-Create", ""),
        TraceEvent("AdaptiveT0", 6, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveT0", 6, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveT0", 6, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: []"),
        TraceEvent("AdaptiveT0", 6, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: []"),
        TraceEvent("AdaptiveT0", 6, "after-Create", ""),
        TraceEvent("AdaptivePrivate", 5, "after-Create", ""),
        TraceEvent("AdaptiveInternal", 4, "after-Create", ""),
        TraceEvent("AdaptivePublic", 3, "after-Create", ""),
        TraceEvent("<root>", 2, "after-Create", ""),
        TraceEvent("<root>", 2, "before-Mount", ""),
        TraceEvent("AdaptivePublic", 3, "before-Mount", ""),
        TraceEvent("AdaptiveInternal", 4, "before-Mount", ""),
        TraceEvent("AdaptivePrivate", 5, "before-Mount", ""),
        TraceEvent("AdaptiveT0", 6, "before-Mount", ""),
        TraceEvent("AdaptiveT0", 6, "after-Mount", ""),
        TraceEvent("AdaptivePrivate", 5, "after-Mount", ""),
        TraceEvent("AdaptiveInternal", 4, "after-Mount", ""),
        TraceEvent("AdaptivePublic", 3, "after-Mount", ""),
        TraceEvent("<root>", 2, "after-Mount", "")
    ))

    return "OK"
}