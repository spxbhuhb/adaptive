package `fun`.adaptive.grove

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.grove.api.GroveRuntimeFragmentFactory
import `fun`.adaptive.grove.apm.groveApmCommon
import `fun`.adaptive.grove.resources.commonMainStringsStringStore0
import `fun`.adaptive.grove.sheet.SheetFragmentFactory
import `fun`.adaptive.grove.sheet.SheetViewContext
import `fun`.adaptive.grove.sheet.groveSheetCommon
import `fun`.adaptive.grove.sheet.model.HandleInfo
import `fun`.adaptive.grove.sheet.model.SheetClipboardItem
import `fun`.adaptive.grove.sheet.model.SheetSnapshot
import `fun`.adaptive.grove.sheet.operation.*
import `fun`.adaptive.grove.ufd.UfdPaneFactory
import `fun`.adaptive.grove.ufd.groveUfdCommon
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.wireformat.WireFormatRegistry

suspend fun groveCommon() {
    groveSheetCommon()
    commonMainStringsStringStore0.load()
}

fun AdaptiveAdapter.groveCommon() {
    groveApmCommon()
    groveRuntimeCommon()
    groveSheetCommon()
    groveUfdCommon()
}

fun Workspace.groveCommon() {
    groveApmCommon()
    groveRuntimeCommon()
    groveSheetCommon()
    groveUfdCommon()
}