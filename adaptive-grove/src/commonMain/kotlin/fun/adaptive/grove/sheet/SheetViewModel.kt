package `fun`.adaptive.grove.sheet

import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.ufd.Snapshot
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.ui.render.model.AuiRenderData

open class SheetViewModel {

    val fragments = autoCollectionOrigin(emptyList<LfmDescendant>())
    val snapshots = autoCollectionOrigin(emptyList<Snapshot>())

    val emptySelection = SheetSelection(emptyList())

    val selection = autoItemOrigin(emptySelection)
    val target = autoItemOrigin(emptySelection)

    fun select(sheet: AdaptiveFragment, x: Double, y: Double) {

        check(sheet is AbstractContainer<*, *>)

        val items = mutableListOf<SelectionInfo>()

        for (child in sheet.layoutItems) {

            if (! child.renderData.contains(x, y)) continue

            val info = child.descendantInfo(sheet) ?: continue

            items += SelectionInfo(info.uuid, child.renderData.rawFrame())
        }

        selection.update(SheetSelection(items))
    }

    fun AuiRenderData.contains(x: Double, y: Double) =
        when {
            y < finalTop -> false
            x < finalLeft -> false
            x >= finalLeft + finalWidth -> false
            y >= finalTop + finalHeight -> false
            else -> true
        }

    fun AuiRenderData.rawFrame() =
        RawFrame(finalTop, finalLeft, finalWidth, finalHeight)

    fun AdaptiveFragment?.descendantInfo(sheet: AbstractContainer<*, *>): DescendantInfo? {
        var current = this

        while (current != null) {
            val info = current.instructions.firstOrNullIfInstance<DescendantInfo>()
            if (info != null) return info

            current = current.parent
            if (current == sheet) return null
        }

        return null
    }

}