package `fun`.adaptive.grove.sheet.model

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.operation.Select
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.ui.fragment.layout.RawPosition
import `fun`.adaptive.ui.render.model.AuiRenderData
import kotlin.math.max
import kotlin.math.min

class SheetSelection(
    val items: List<SheetItem>
) {

    val containingFrame: RawFrame
        get() {
            if (items.isEmpty()) return RawFrame.NaF

            var top = Double.MAX_VALUE
            var left = Double.MAX_VALUE

            var right = Double.MIN_VALUE
            var bottom = Double.MIN_VALUE

            for (item in items) {
                val frame = item.frame

                val finalTop = frame.top
                val finalLeft = frame.left

                top = min(top, finalTop)
                left = min(left, finalLeft)
                bottom = max(finalTop + frame.height, bottom)
                right = max(finalLeft + frame.width, right)
            }

            return RawFrame(top, left, right - left, bottom - top)
        }

    fun fragments(viewModel: SheetViewModel): List<LfmDescendant> {
        val uuids = items.map { it.uuid }
        return viewModel.fragments.filter { it.uuid in uuids }
    }

    fun isEmpty() = items.isEmpty()

}