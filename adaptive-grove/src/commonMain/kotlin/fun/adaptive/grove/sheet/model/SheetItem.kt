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
    var model: LfmDescendant
) {
    var group: ItemIndex? = null

    var removed: Boolean = false

    val isGroup: Boolean
        get() = members != null

    val isInGroup: Boolean
        get() = group != null

    var members: MutableList<ItemIndex>? = null

    val fragment: AdaptiveFragment
        get() = fragmentOrNull!!

    var fragmentOrNull : AdaptiveFragment? = null

    val frame: RawFrame
        get() {
            val f = fragmentOrNull ?: return RawFrame.NaF

            return if (f !is AbstractAuiFragment<*>) {
                RawFrame.NaF
            } else {
                f.renderData.rawFrame
            }
        }

    var beforeRemove : SheetClipboardItem? = null

    override fun toString(): String =
        "SheetItem(index=$index, name='$name', model=$model, group=$group, removed=$removed, members=$members, fragment=$fragmentOrNull, frame=$frame)"

}