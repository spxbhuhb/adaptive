package `fun`.adaptive.ui.input.select.item

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.input.select.AbstractSelectInputViewBackend

@Adaptive
fun <OT> selectInputValueText(
    item: AbstractSelectInputViewBackend<*,*,OT>.SelectItem
) {
    row {
        item.valueContainerInstructions()
        text(item) .. item.valueTextInstructions()
    }
}