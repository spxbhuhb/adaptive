package `fun`.adaptive.grove.sheet.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.ui.fragment.layout.RawFrame
import kotlin.math.max
import kotlin.math.min

@Adat
class SheetSelection(
    val selected: List<SelectionInfo>
) {

    val containingFrame: RawFrame
        get() {
            if (selected.isEmpty()) return RawFrame.NaF

            var top = Double.MAX_VALUE
            var left = Double.MAX_VALUE

            var right = Double.MIN_VALUE
            var bottom = Double.MIN_VALUE

            for (item in selected) {
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
        val uuids = selected.map { it.uuid }
        return viewModel.fragments.filter { it.uuid in uuids }
    }

    fun isEmpty() = selected.isEmpty()

}