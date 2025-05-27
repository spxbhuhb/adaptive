package `fun`.adaptive.grove

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.grove.apm.groveApmCommon
import `fun`.adaptive.grove.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.grove.sheet.groveSheetCommon
import `fun`.adaptive.grove.ufd.groveUfdCommon
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace

suspend fun groveCommon() {
    groveRuntimeCommon()
    groveSheetCommon()
    commonMainStringsStringStore0.load()
}

fun AdaptiveAdapter.groveCommon() {
    groveApmCommon()
    groveRuntimeCommon()
    groveSheetCommon()
    groveUfdCommon()
}

fun MultiPaneWorkspace.groveCommon() {
    groveApmCommon()
    groveRuntimeCommon()
    groveSheetCommon()
    groveUfdCommon()
}