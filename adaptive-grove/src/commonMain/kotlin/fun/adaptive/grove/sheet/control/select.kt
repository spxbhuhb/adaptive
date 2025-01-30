package `fun`.adaptive.grove.sheet.control

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.grove.sheet.model.DescendantInfo
import `fun`.adaptive.grove.sheet.model.SelectionInfo
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.grove.sheet.model.SheetViewModel
import `fun`.adaptive.grove.sheet.operation.Select
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.ui.render.model.AuiRenderData

fun SheetViewModel.select(x: Double, y: Double) {

    val box = root.parent?.parent as AbstractContainer<*, *>

    val items = mutableListOf<SelectionInfo>()

    for (child in box.layoutItems) {

        if (! child.renderData.contains(x, y)) continue

        val info = child.descendantInfo(box) ?: continue

        items += SelectionInfo(info.uuid, child.renderData.rawFrame())
    }

    // if the selection is already up to date we don't have to refresh it
    // this prevents unnecessary undo/redo operations if the user clicks
    // on the same item a few times

    if (selection.value == SheetSelection(items)) return

    this += Select(items)
}

fun SheetViewModel.refreshSelection() {
    val box = root.parent?.parent as AbstractContainer<*, *>
    val selected = selection.value.selected.map { it.uuid }
    val items = mutableListOf<SelectionInfo>()

    for (child in box.layoutItems) {
        val info = child.descendantInfo(box) ?: continue
        if (info.uuid !in selected) continue
        items += SelectionInfo(info.uuid, child.renderData.rawFrame())
    }

    selection.update(SheetSelection(items))
}

private fun AuiRenderData.contains(x: Double, y: Double) =
    when {
        y < finalTop -> false
        x < finalLeft -> false
        x >= finalLeft + finalWidth -> false
        y >= finalTop + finalHeight -> false
        else -> true
    }

private fun AuiRenderData.rawFrame() =
    RawFrame(finalTop, finalLeft, finalWidth, finalHeight)

private fun AdaptiveFragment?.descendantInfo(sheet: AbstractContainer<*, *>): DescendantInfo? {
    var current = this

    while (current != null) {
        val info = current.instructions.firstInstanceOfOrNull<DescendantInfo>()
        if (info != null) return info

        current = current.parent
        if (current == sheet) return null
    }

    return null
}