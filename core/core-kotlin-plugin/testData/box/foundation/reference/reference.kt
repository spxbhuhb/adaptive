package `fun`.adaptive.sandbox.stuff.b

import `fun`.adaptive.foundation.*
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.testing.*

@Adaptive
fun tf(vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    T0()
    return fragment()
}

fun dialog(
    adapter: AdaptiveAdapter,
    content: @Adaptive (hide: () -> Unit) -> Unit
) {

}

@Adaptive
fun dialogMain() {
    tf() .. TInst { dialog(adapter(), ::dialogContent) }
}

@Adaptive
fun dialogContent(hide: () -> Unit) {

}

class TInst(
    val instFun: () -> Unit
) : AdaptiveInstruction {
    override fun toString() = "TInst"
}

fun box(): String {

    val adapter = AdaptiveTestAdapter()
    //adapter.printTrace = true

    adaptive(adapter) {
        dialogMain()
    }

    return adapter.assert(
        listOf(
            TraceEvent("<root>", 2, "before-Create", ""),
            TraceEvent("<root>", 2, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("<root>", 2, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null]"),
            TraceEvent("AdaptiveDialogMain", 3, "before-Create", ""),
            TraceEvent("AdaptiveDialogMain", 3, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveDialogMain", 3, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveDialogMain", 3, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveDialogMain", 3, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null]"),
            TraceEvent("AdaptiveTf", 4, "before-Create", ""),
            TraceEvent("AdaptiveTf", 4, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveTf", 4, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [[TInst]]"),
            TraceEvent("AdaptiveTf", 4, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [[TInst]]"),
            TraceEvent("AdaptiveTf", 4, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [[TInst]]"),
            TraceEvent("AdaptiveSequence", 5, "before-Create", ""),
            TraceEvent("AdaptiveSequence", 5, "before-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,null]"),
            TraceEvent("AdaptiveSequence", 5, "after-Patch-External", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,[1]]"),
            TraceEvent("AdaptiveSequence", 5, "before-Patch-Internal", "createMask: 0x00000000 thisMask: 0xffffffff state: [null,[1]]"),
            TraceEvent("AdaptiveT0", 6, "before-Create", ""),
            TraceEvent("AdaptiveT0", 6, "before-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT0", 6, "after-Patch-External", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT0", 6, "before-Patch-Internal", "createMask: 0xffffffff thisMask: 0xffffffff state: [null]"),
            TraceEvent("AdaptiveT0", 6, "after-Patch-Internal", "createMask: 0xffffffff thisMask: 0x00000000 state: [null]"),
            TraceEvent("AdaptiveT0", 6, "after-Create", ""),
            TraceEvent("AdaptiveSequence", 5, "after-Patch-Internal", "createMask: 0x00000000 thisMask: 0x00000000 state: [null,[1]]"),
            TraceEvent("AdaptiveSequence", 5, "after-Create", ""),
            TraceEvent("AdaptiveTf", 4, "after-Create", ""),
            TraceEvent("AdaptiveDialogMain", 3, "after-Create", ""),
            TraceEvent("<root>", 2, "after-Create", ""),
            TraceEvent("<root>", 2, "before-Mount", ""),
            TraceEvent("AdaptiveDialogMain", 3, "before-Mount", ""),
            TraceEvent("AdaptiveTf", 4, "before-Mount", ""),
            TraceEvent("AdaptiveSequence", 5, "before-Mount", ""),
            TraceEvent("AdaptiveT0", 6, "before-Mount", ""),
            TraceEvent("AdaptiveT0", 6, "after-Mount", ""),
            TraceEvent("AdaptiveSequence", 5, "after-Mount", ""),
            TraceEvent("AdaptiveTf", 4, "after-Mount", ""),
            TraceEvent("AdaptiveDialogMain", 3, "after-Mount", ""),
            TraceEvent("<root>", 2, "after-Mount", "")
        )
    )
}