package `fun`.adaptive.grove.sheet.control

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.grove.sheet.model.DescendantInfo
import `fun`.adaptive.grove.sheet.model.SelectionInfo
import `fun`.adaptive.grove.sheet.model.SheetSelection
import `fun`.adaptive.grove.sheet.model.SheetViewModel
import `fun`.adaptive.grove.sheet.operation.Select
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.ui.fragment.layout.RawPosition
import `fun`.adaptive.ui.render.model.AuiRenderData
import kotlin.math.max
import kotlin.math.min

fun SheetViewModel.select(startPosition: RawPosition, x: Double, y: Double, add : Boolean) {
    val x1 = min(startPosition.left, x)
    val y1 = min(startPosition.top, y)
    val x2 = max(startPosition.left, x)
    val y2 = max(startPosition.top, y)

    select(add) { renderData ->
        val rx1 = renderData.finalLeft
        val ry1 = renderData.finalTop
        val rx2 = rx1 + renderData.finalWidth
        val ry2 = ry1 + renderData.finalHeight

        when {
            x1 >= rx2 || rx1 >= x2 -> false          // one rectangle is to the left of the other
            y1 >= ry2 || ry1 >= y2 -> false          // one rectangle is above the other
            else -> true
        }
    }
}

fun SheetViewModel.select(x: Double, y: Double, add : Boolean) {
    select(add) { it.contains(x, y) }
}

fun SheetViewModel.select(add : Boolean, condition: (renderData: AuiRenderData) -> Boolean) {

    val box = root.parent?.parent as AbstractContainer<*, *>

    val items = mutableListOf<SelectionInfo>()

    for (child in box.layoutItems) {

        if (! condition(child.renderData)) continue

        val info = child.descendantInfo(box) ?: continue

        items += SelectionInfo(info.uuid, child.renderData.rawFrame())
    }

    // if the selection is already up to date we don't have to refresh it
    // this prevents unnecessary undo/redo operations if the user clicks
    // on the same item a few times

    if (selection.value == SheetSelection(items)) return

    if (add) {
        val uuids = items.map { it.uuid }
        selection.value.selected.forEach {
            if (it.uuid in uuids) return@forEach
            items += it
        }
    }

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