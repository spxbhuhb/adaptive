package `fun`.adaptive.grove.sheet.model

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.layout.RawFrame

/**
 * @property  index    Index of the item in the `SheetViewController.items`. Never changes during the lifetime of the sheet.
 * @property  removed  When true, the item has been removed from the sheet. Undo can set this to `false` to restore the item.
 * @property  group    Index of the group item in `SheetViewController.items` or null when the item is not part of a group.
 */
class SheetItem(
    val index: ItemIndex,
    var name: String,
    val model: LfmDescendant
) {
    var group: ItemIndex? = null

    var removed: Boolean = false

    val isGroup: Boolean
        get() = members != null

    val isInGroup: Boolean
        get() = group != null

    var members: List<ItemIndex>? = null

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