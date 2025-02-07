package `fun`.adaptive.grove.sheet.model

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.layout.RawFrame

class SheetItem(
    val index: Int,
    val model: LfmDescendant
) {
    var removed: Boolean = false

    lateinit var fragment: AdaptiveFragment

    val frame: RawFrame
        get() {
            val f = fragment

            return if (f !is AbstractAuiFragment<*>) {
                RawFrame.NaF
            } else {
                f.renderData.rawFrame
            }
        }

}