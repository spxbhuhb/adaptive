package `fun`.adaptive.grove.sheet.model

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant

class SheetItem(
    val index: Int,
    val x1: Double,
    val y1: Double,
    val x2: Double,
    val y2: Double,
    val model: LfmDescendant
) {
    var removed: Boolean = false
    lateinit var fragment: AdaptiveFragment
}