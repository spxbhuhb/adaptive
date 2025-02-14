package `fun`.adaptive.grove

import `fun`.adaptive.grove.sheet.model.HandleInfo
import `fun`.adaptive.grove.sheet.model.SheetClipboardItem
import `fun`.adaptive.grove.sheet.model.SheetSnapshot
import `fun`.adaptive.grove.sheet.operation.*
import `fun`.adaptive.wireformat.WireFormatRegistry

fun groveCommon() {
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