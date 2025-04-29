package `fun`.adaptive.ui.input.select.item

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.input.select.AbstractSelectInputViewBackend

@Adaptive
fun <OT> selectInputValueIconAndText(
    item: AbstractSelectInputViewBackend<*,*,OT>.SelectItem
) {
    row {
        item.valueContainerInstructions()

        icon(item.icon()) .. item.valueIconInstructions()
        text(item) .. item.valueTextInstructions()
    }
}