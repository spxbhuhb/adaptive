package `fun`.adaptive.grove.sheet

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.grove.sheet.model.HandleInfo
import `fun`.adaptive.grove.sheet.model.SheetClipboardItem
import `fun`.adaptive.grove.sheet.model.SheetSnapshot
import `fun`.adaptive.grove.sheet.operation.Add
import `fun`.adaptive.grove.sheet.operation.Copy
import `fun`.adaptive.grove.sheet.operation.Cut
import `fun`.adaptive.grove.sheet.operation.Group
import `fun`.adaptive.grove.sheet.operation.Move
import `fun`.adaptive.grove.sheet.operation.Paste
import `fun`.adaptive.grove.sheet.operation.Remove
import `fun`.adaptive.grove.sheet.operation.Resize
import `fun`.adaptive.grove.sheet.operation.SelectByFrame
import `fun`.adaptive.grove.sheet.operation.SelectByIndex
import `fun`.adaptive.grove.sheet.operation.SelectByPosition
import `fun`.adaptive.grove.sheet.operation.Undo
import `fun`.adaptive.grove.sheet.operation.Ungroup
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.wireformat.WireFormatRegistry

fun groveSheetCommon() {
    val r = WireFormatRegistry

    r += Add
    r += Copy
    r += Cut
    r += Group
    r += Move
    r += Paste
    r += Remove
    r += Resize
    r += SelectByFrame
    r += SelectByIndex
    r += SelectByPosition
    r += Undo
    r += Ungroup

    r += HandleInfo
    r += SheetSnapshot
    r += SheetClipboardItem
}

fun AdaptiveAdapter.groveSheetCommon() {
    fragmentFactory += arrayOf(SheetFragmentFactory)
}

fun Workspace.groveSheetCommon() {
    contexts += SheetViewContext(this)
}